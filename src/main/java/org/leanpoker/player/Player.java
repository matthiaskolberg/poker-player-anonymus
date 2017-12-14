package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {
		
		List<Card> ourCards = new ArrayList<>();
		List<Card> communityCards = new ArrayList<>();
		
		
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
			
			JsonArray commCards = obj.get("community_cards").getAsJsonArray();
			for(int i = 0;i<commCards.size();i++) {
				JsonObject card = commCards.get(i).getAsJsonObject();
				String rank = card.get("rank").getAsString();
				String suit = card.get("suit").getAsString();
				System.out.println("CommunityKarte"+i+" rank:" + rank + " suit:" + suit);
				communityCards.add(new Card(rank, suit));
			}
			
			int hoechsterbet = obj.get("current_buy_in").getAsInt();
			
			// wir gehen immer mit
			int unserbet = 0;

			// wenn wir zwei bilder haben
			if (isAKQJ(ourCards.get(0)) && isAKQJ(ourCards.get(1))) {
				System.out.println("wir haben zwei Bilder");
				// verdoppeln des einsatzes 
				unserbet = hoechsterbet*2;
				
				//Paar mit Bildern!! Rock n Roll!!
				if (isPairOnHand(ourCards)) {
					unserbet = hoechsterbet*40;
				}
			} else if (isAKQJ(ourCards.get(0)) || isAKQJ(ourCards.get(1))) {
				// wire haben ein bild
				System.out.println("wir haben ein Bild");

				if (communityCards.size() == 0) {
					// und beide karten von einer farbe
					if (isSuitedOnHand(ourCards)) {
						// gleiche Farbe
						unserbet = hoechsterbet*2;
					}					
				} else {
					// paar mit community cards
					if (paarMitBild(ourCards, communityCards)) {
						unserbet = hoechsterbet*5;
					} 					
				}
			}
					
			return unserbet;
		} catch (Throwable e) {
			System.err.println(e);
			return 123;
		}
	}
	
	private static boolean paarMitBild(List<Card> handkarten, List<Card> commkarten) {
		boolean paarMitBild = false;
		
		String bildkarte = "";
		if (isAKQJ(handkarten.get(0))) {
			bildkarte = handkarten.get(0).getRank();
		} else {
			bildkarte = handkarten.get(0).getRank();
		}
		
		for(Card card : commkarten) {
			if (card.getRank().equals(bildkarte)) {
				paarMitBild = true;
			}
		}
		
		return paarMitBild;
	}
	
	private static boolean isAKQJ(Card card) {
		boolean isAKQJ = false;
		if (card.getRank().matches("[AKQJ]")) {
			isAKQJ = true;
		}
		System.out.println("card isAKQJ:" + isAKQJ);
		return isAKQJ;
	}
	
	// on hand
	private static boolean isSuitedOnHand(List<Card> cards) {
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