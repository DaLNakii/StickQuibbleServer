package org.dalnakiigames.stickquibble.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {
	private final static Map<Integer, GameRoom> RoomCache = new ConcurrentHashMap<Integer, GameRoom>();
	
	public static boolean has(int roomID) {
		return RoomCache.containsKey(roomID);
	}
	
	public static GameRoom get(int roomID) {
		return RoomCache.get(roomID);
	}
	
	private static int _ID = 1;
	
	public final int ID;
	
	public final Session[] Sessions;
	
	public GameRoom(Session creator, Session opponent) {
		ID = _ID++;
		
		Sessions = new Session[2];
		Sessions[0] = creator;
		Sessions[1] = opponent;
		
		RoomCache.put(ID, this);
		
		for(Session s : Sessions) s.Join(this);
		
		creator.Send(new JoinGameRoom(ID, opponent.ID, 0).Serialize());
		opponent.Send(new JoinGameRoom(ID, creator.ID, 1).Serialize());
		
		System.out.println("Room " + ID + " created.");
	}
	
	public void Send(byte[] data) {
		for(Session s : Sessions) s.Send(data);
	}
	
	public void Close(int id) {
		for(Session s : Sessions) {
			s.Leave();
			
			if (s.ID == id) continue;
			s.Send(new LeaveGameRoom(ID).Serialize());
		}
		
		Sessions[0] = Sessions[1] = null;
		
		RoomCache.remove(ID);
		
		System.out.println("Room " + ID + " closed.");
	}
}
