package org.dalnakiigames.stickquibble.server;

public class Main {

	public static void main(String[] args) {
		ServerHandler server = new ServerHandler(8192);
		server.run();
	}

}
