package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConnectAction extends NetworkAction {
	public final static byte CODE = 10;
	
	private int _ID;
	
	public ConnectAction() {
		super(CODE);
	}

	public ConnectAction(int id) {
		super(CODE);
		
		_ID = id;
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
		buffer.putInt(_ID);
		return buffer.array();
	}

	@Override
	public boolean Deserialize(ByteBuffer buffer) {
		if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
			_ID = buffer.getInt();
			return true;
		}
		return false;
	}

	@Override
	public void Execute() {}

}
