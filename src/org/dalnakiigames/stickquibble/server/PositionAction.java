package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PositionAction extends NetworkAction {
	public final static byte CODE = 13;
	
	private double _posX;
	private double _posY;
	private int _ID;
	private int _characterID;
	
	public PositionAction() {
		super(CODE);
	}

	public PositionAction(double x, double y, int id, int charID) {
		super(CODE);
		
		_posX = x;
		_posY = y;
		_ID = id;
		_characterID = charID;
	}
	
	@Override
	public int getSize() {
		return Byte.BYTES + Double.BYTES + Double.BYTES + Integer.BYTES + Integer.BYTES;
	}

	@Override
	public byte[] Serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize()).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(CODE);
		buffer.putDouble(_posX);
		buffer.putDouble(_posY);
		buffer.putInt(_ID);
		buffer.putInt(_characterID);
		return buffer.array();
	}

	@Override
	public boolean Deserialize(ByteBuffer buffer) {
		if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
			_posX = buffer.getDouble();
			_posY = buffer.getDouble();
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
