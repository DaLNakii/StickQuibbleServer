package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class InputAction extends NetworkAction {
	public final static byte CODE = 14;
	
	private byte _keyState;
	private int _keyCode;
	private int _ID;
	private int _characterID;
	
	public InputAction() {
		super(CODE);
	}

	public InputAction(boolean state, int keycode, int id, int charID) {
		super(CODE);
		
		_keyState = (byte) (state ? 1 : 0);
		_keyCode = keycode;
		_ID = id;
		_characterID = charID;
	}
	
	@Override
	public int getSize() {
		return Byte.BYTES + Byte.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES;
	}

	@Override
	public byte[] Serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(CODE);
		buffer.put(_keyState);
		buffer.putInt(_keyCode);
		buffer.putInt(_ID);
		buffer.putInt(_characterID);
		return buffer.array();
	}

	@Override
	public boolean Deserialize(ByteBuffer buffer) {
		if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
			_keyState = buffer.get();
			_keyCode = buffer.getInt();
			_ID = buffer.getInt();
			_characterID = buffer.getInt();
			return true;
		}
		return false;
	}

	@Override
	public void Execute() {
		GameRoom room = getSession().getRoom();
		if (room != null) room.Send(Serialize());
	}

}
