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
			
			JsonObject player;
			for(int i = 0;i<players.size();i++) {
				player = players.get(i).getAsJsonObject();
				System.out.println("here is one player" + player);
				
				if (player.get("name").equals("Anonymus")) {
					System.out.println("this is our player");
					break;
				}
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
