package org.dalnakiigames.stickquibble.server;

import java.nio.ByteBuffer;

public abstract class NetworkAction {
	
	public final int ActionCode;
	
	private Session _session;
	
	public NetworkAction(int code) {
		ActionCode = code;
	}

	protected Session getSession() {
		return _session;
	}
	
	public void setSession(final Session session) {
		_session = session;
	}

	public abstract int getSize();
	
	public abstract byte[] Serialize();
	public abstract boolean Deserialize(ByteBuffer buffer);
	
	public abstract void Execute();
}
