package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {
		
		List<Card> ourCards = new ArrayList();
		List<Card> communityCards = new ArrayList();
		
		
		try {
			JsonObject obj = request.getAsJsonObject();
			JsonArray players = obj.get("players").getAsJsonArray();
			
			JsonObject player = null;
			for(int i = 0;i<players.size();i++) {
				player = players.get(i).getAsJsonObject();
				System.out.println("here is one player" + player);
				
				if (player.get("name").getAsString().equals("Anonymus")) {
					System.out.println("this is our player");
					break;
				}
			}
			
			JsonArray holeCards = player.get("hole_cards").getAsJsonArray();
			for(int i = 0;i<holeCards.size();i++) {
				JsonObject card = holeCards.get(i).getAsJsonObject();
				String rank = card.get("rank").getAsString();
				String suit = card.get("suit").getAsString();
				System.out.println("Karte"+i+" rank:" + rank + " suit:" + suit);
				ourCards.add(new Card(rank, suit));
			}
			
			int hoechsterbet = obj.get("current_buy_in").getAsInt();
			// wir gehen immer mit
			int unserbet = 50;

			if (isAKQJ(ourCards.get(0)) && isAKQJ(ourCards.get(1))) {
				System.out.println("wir haben zwei Bilder");
				// verdoppeln des einsatzes 
				unserbet = hoechsterbet*20;
				
				if (isPairOnHand(ourCards)) {
					//Paar mit Bildern!! Rock n Roll!!
					unserbet = hoechsterbet*40;
				}
			} else if (isAKQJ(ourCards.get(0)) || isAKQJ(ourCards.get(1))) {
				System.out.println("wir haben ein Bild");
				
				if (isSuited(ourCards)) {
					// gleiche Farbe
					unserbet = hoechsterbet*10;
				}
			}
					
			return unserbet;
		} catch (Throwable e) {
			System.err.println(e);
			return 123;
		}
	}
	
	private static boolean isAKQJ(Card card) {
		boolean isAKQJ = false;
		if (card.getRank().matches("[AKQJ]")) {
			isAKQJ = true;
		}
		System.out.println("card isAKQJ:" + isAKQJ);
		return isAKQJ;
	}
	
	private static boolean isSuited(List<Card> cards) {
		boolean isSuited = false;
		if (cards.get(0).getSuit().equals(cards.get(1).getSuit())) {
			isSuited = true;
		}
		System.out.println("isSuited: " + isSuited);
		return isSuited;
	}
	
	private static boolean isPairOnHand(List<Card> cards) {
		boolean isPair = false;
		if (cards.get(0).getRank().equals(cards.get(1).getRank())) {
			isPair = true;
		}
		System.out.println("isPair: " + isPair);
		return isPair;
	}

	public static void showdown(JsonElement game) {
	}
}
