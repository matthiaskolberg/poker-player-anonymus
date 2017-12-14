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
			
			int hoechsterbet = obj.get("minimum_raise").getAsInt();
			
			// wir gehen immer mit
			int unserbet = 0;
			String preflopDecision = getPreflopDecision(ourCards);

			if (communityCards.size() == 0) {
				if (preflopDecision.equals("2BS")) {
					System.out.println("wir haben zwei Bilder auf der hand suited");
					unserbet = hoechsterbet*2;
				} else if (preflopDecision.equals("2BP")) {
					System.out.println("Paar Rock'n'Roll!");
					unserbet = hoechsterbet*4;
				} else if (preflopDecision.equals("2BU")) {
					System.out.println("wir haben zwei Bilder auf der hand unsuited");
					unserbet = hoechsterbet*2;
				} else if (preflopDecision.equals("1BS")) {
					System.out.println("wir haben ein Bild suited auf der hand");
					unserbet = hoechsterbet*2;
				} else if (preflopDecision.equals("1BU")) {
					System.out.println("wir haben ein Bild unsuited auf der hand");
					unserbet = hoechsterbet;
				} else if (preflopDecision.equals("0P")) {
					System.out.println("wir haben ein paar, kein Bild");
					unserbet = hoechsterbet*4;
				} else if (preflopDecision.equals("0S")) {
					System.out.println("wir haben kein Bild, suited zahl");
					unserbet = hoechsterbet;
				}
			} else if (communityCards.size() > 0) {
				int karte1matches = countMatchingCardsInComm(ourCards.get(0), communityCards);
				int karte2matches = countMatchingCardsInComm(ourCards.get(0), communityCards);
				
				if (preflopDecision.equals("2BS")) {
					System.out.println("wir haben zwei Bilder auf der hand suited");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*3;
					}
				} else if (preflopDecision.equals("2BP")) {
					System.out.println("Paar Rock'n'Roll!");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*6;
					}
				} else if (preflopDecision.equals("2BU")) {
					System.out.println("wir haben zwei Bilder auf der hand unsuited");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*3;
					}
				} else if (preflopDecision.equals("1BS")) {
					System.out.println("wir haben ein Bild suited auf der hand");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*2;
					}
				} else if (preflopDecision.equals("1BU")) {
					System.out.println("wir haben ein Bild unsuited auf der hand");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*2;
					}
				} else if (preflopDecision.equals("0P")) {
					System.out.println("wir haben ein paar, kein Bild");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*6;
					}
				} else if (preflopDecision.equals("0S")) {
					System.out.println("wir haben kein Bild, suited zahl");
					if (karte1matches >= 1 || karte2matches >= 1) {
						unserbet = hoechsterbet*3;
					}
				}
			}
					
			return unserbet;
		} catch (Throwable e) {
			System.err.println(e);
			return 123;
		}
	}
	
	private static int countMatchingCardsInComm(Card card, List<Card> communityCards) {
		int count = 0;
		for(Card karte: communityCards) {
			if (karte.getRank().equals(card.getRank())) {
				count++;
			}
		}
		return count;
	}
	
	private static String getPreflopDecision(List<Card> cards) {
		String decision = "";
		if (isAKQJ(cards.get(0)) && isAKQJ(cards.get(1))) {
			if (isPairOnHand(cards)) {
				decision = "2BP";
			} else if (isSuitedOnHand(cards)) {
				decision = "2BS";
			} else {
				decision = "2BU";
			}
		} else if (isAKQJ(cards.get(0)) || isAKQJ(cards.get(1))) {
			if (isSuitedOnHand(cards)) {
				decision = "1BS";
			} else {
				decision = "1BU";
			}
		} else if (isPairOnHand(cards)) {
			decision = "0P";
		} else if (isSuitedOnHand(cards)) {
			decision = "0S";
		}
		return decision;
	}
	
	private static String getFlopDecision(List<Card> ourCards, List<Card> communityCards) {
		String decision = "";
		
		return decision;
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
	
	private static boolean isDrilling(List<Card> handkarten, List<Card> commkarten) {
		boolean drillingMitBild = false;
		
		String bildkarte = "";
		if (isAKQJ(handkarten.get(0))) {
			bildkarte = handkarten.get(0).getRank();
		} else {
			bildkarte = handkarten.get(0).getRank();
		}
		
		for(Card card : commkarten) {
			if (card.getRank().equals(bildkarte)) {
				drillingMitBild = true;
			}
		}
		
		return drillingMitBild;
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
