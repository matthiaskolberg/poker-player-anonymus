package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {
		
		Card[] ourCards = new Card[2];
		Card[] communityCards = new Card[5];
		
		try {
			JsonObject obj = request.getAsJsonObject();
			JsonArray players = obj.get("players").getAsJsonArray();
			
			for(int i = 0;i<players.size();i++) {
				JsonObject player = players.get(i).getAsJsonObject();
				System.out.println("here is the player" + player);
				
				
			}
			
			return 100;
		} catch (Throwable e) {
			System.err.println(e);
			return 50;
		}
	}

	public static void showdown(JsonElement game) {
	}
}
