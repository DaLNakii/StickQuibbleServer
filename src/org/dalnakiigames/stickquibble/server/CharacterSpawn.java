package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CharacterSpawn extends NetworkAction {
	public final static byte CODE = 12;
	
	private int _ID;
	
	public CharacterSpawn() {
		super(CODE);
	}

	public CharacterSpawn(int id) {
		super(CODE);
		
		_ID = id;
	}
	
	@Override
	public int getSize() {
		return Byte.BYTES + Integer.BYTES;
	}

	@Override
	public byte[] Serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize()).order(ByteOrder.LITTLE_ENDIAN);
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
	public void Execute() {
		
	}
}
