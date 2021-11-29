package org.dalnakiigames.stickquibble.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ServerHandler implements Runnable {
	private ServerSocket _socket;
	
	private Queue<Session> _battleQueue;
	
	public ServerHandler(final int port) {
		_battleQueue = new LinkedList<Session>();
		
		try {
			_socket = new ServerSocket();
			_socket.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Socket sock = _socket.accept();
				
				while (!_battleQueue.isEmpty()) {
					if (!_battleQueue.peek().Connected()) {
						_battleQueue.poll();
					} else break;
				}
				
				Session session = new Session(sock);
				_battleQueue.add(session);
				
				System.out.println("Players in battle queue " + _battleQueue.size());
				
				if (_battleQueue.size() > 1) {
					new GameRoom(_battleQueue.poll(), _battleQueue.poll());
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
