package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LeaveGameRoom extends NetworkAction {
	public final static byte CODE = 15;
	
	private int _roomID;
	
	public LeaveGameRoom() {
		super(CODE);
	}

	public LeaveGameRoom(int roomID) {
		super(CODE);
		
		_roomID = roomID;
	}
	
	@Override
	public int getSize() {
		return Byte.BYTES + Integer.BYTES;
	}

	@Override
	public byte[] Serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(CODE);
		buffer.putInt(_roomID);
		return buffer.array();
	}

	@Override
	public boolean Deserialize(ByteBuffer buffer) {
		if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
			_roomID = buffer.getInt();
			return true;
		}
		return false;
	}

	@Override
	public void Execute() {

	}

}
