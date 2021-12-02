package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JoinGameRoom extends NetworkAction {
	public final static byte CODE = 11;
	
	private int _roomID;
	private int _opponentID;
	private int _characterID;
	
	public JoinGameRoom() {
		super(CODE);
	}

	public JoinGameRoom(int roomId, int opponentId, int charID) {
		super(CODE);
		
		_roomID = roomId;
		_opponentID = opponentId;
		_characterID = charID;
	}
	
	@Override
	public int getSize() {
		return Byte.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES;
	}

	@Override
	public byte[] Serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize()).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(CODE);
		buffer.putInt(_roomID);
		buffer.putInt(_opponentID);
		buffer.putInt(_characterID);
		return buffer.array();
	}

	@Override
	public boolean Deserialize(ByteBuffer buffer) {
		if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
			_roomID = buffer.getInt();
			_opponentID = buffer.getInt();
			_characterID = buffer.getInt();
			return true;
		}
		return false;
	}

	@Override
	public void Execute() {}
}
