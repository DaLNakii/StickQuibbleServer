package org.dalnakiigames.stickquibble.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session implements Runnable {
	private static Map<Integer, Session> _sessions = new ConcurrentHashMap<Integer, Session>();
	
	private static int _ID = 1;
	
	private final Socket _socket;
	private final InputStream _input;
	private final OutputStream _output;
	
	private boolean _connected;
	
	private byte[] _buffer = new byte[1024];
	
	private Thread _dataHandler;
	
	private GameRoom _gameRoom;
	
	public final int ID;
	
	public Session(final Socket sock) throws IOException {
		ID = _ID++;
		
		_connected = true;
		
		_socket = sock;
		
		_input = _socket.getInputStream();
		_output = _socket.getOutputStream();
		
		_sessions.put(ID, this);
		
		_gameRoom = null;
		
		_dataHandler = new Thread(this);
		_dataHandler.start();
		
		Send(new ConnectAction(ID).Serialize());
	}

	public boolean Connected() {
		return _connected;
	}
	
	public void Disconnect() {
		if (!_connected) return;
		
		
		_connected = false;
		
		if (_gameRoom != null) {
			_gameRoom.Close(ID);
			_gameRoom = null;
		}
		
		try {
			_dataHandler.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			_input.close();
			_output.close();
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_sessions.remove(ID);
	}
	
	public void Send(final NetworkAction action) {
		Send(action.Serialize());
	}
	
	public void Send(final byte[] payload) {
		if (_socket.isClosed()) return;
		
		try {
			_output.write(payload, 0, payload.length);
		} catch (IOException e) {
			Disconnect();
		}
	}
	
	public GameRoom getRoom() {
		return _gameRoom;
	}
	
	public void Join(GameRoom room) {
		_gameRoom = room;
	}
	
	public void Leave() {
		_gameRoom = null;
	}
	
	@Override
	public void run() {
		try {
			while(!_socket.isClosed()) {
				int received = _input.read(_buffer);
				
				if (received > 0) {
					handleAction(_buffer, 0, received);
				}
			}
		} catch (IOException e) {
			Disconnect();
		}
	}
	
	private void handleAction(byte[] buff, int offset, int len) {
		ByteBuffer buffer = ByteBuffer.wrap(buff, offset, len).order(ByteOrder.LITTLE_ENDIAN);
		
		int actionCode = buffer.get();
		
		NetworkAction action = null;
		
		switch(actionCode) {
		case ConnectAction.CODE:
			action = new ConnectAction();
			break;
			
		case JoinGameRoom.CODE:
			action = new JoinGameRoom();
			break;
			
		case CharacterSpawn.CODE:
			action = new CharacterSpawn();
			break;
			
		case PositionAction.CODE:
			action = new PositionAction();
			break;
			
		case InputAction.CODE:
			action = new InputAction();
			break;
		}
		
		if (action != null) {
			action.setSession(this);
			if (action.Deserialize(buffer))
			{
				action.Execute();
			}
			System.out.println("[" + ID + "] ActionCode: " + actionCode + ", " + action.getSize() + " bytes.");
			
			if (len > action.getSize()) {
				handleAction(buff, offset + action.getSize(), buffer.remaining());
			}
		} else {
			System.out.println("Could not deserialize " + len + " bytes.");
		}
	}
}
