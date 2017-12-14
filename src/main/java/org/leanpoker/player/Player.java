package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {

		try {
			JsonObject obj = request.getAsJsonObject();
			JsonArray players = obj.get("players").getAsJsonArray();
			
			return 50;
		} catch (Throwable e) {
			System.err.println(e);
			return 50;
		}
	}

	public static void showdown(JsonElement game) {
	}
}
