package utilities;

import java.util.HashMap;

import main.BGMain;

public class BGReward {
	public static HashMap<String, String> BOUGHT_KITS = new HashMap<String, String>();
	
	public static void createUser(String playerName) {
		
		if (BGMain.getCoins(BGMain.getPlayerID(playerName)) == null) {
			BGMain.SQLquery("INSERT INTO REWARD (REF_PLAYER, COINS) VALUES ("+ BGMain.getPlayerID(playerName) + ", 0)");
		}
	}
	
	public static void giveCoins(String playerName, int coins) {
		
		BGMain.SQLquery("UPDATE REWARD SET COINS = (COINS+"+coins+") WHERE REF_PLAYER=" + BGMain.getPlayerID(playerName));
	}
	
	public static void takeCoins(String playerName, int coins) {
		
		BGMain.SQLquery("UPDATE REWARD SET COINS = (COINS-"+coins+") WHERE REF_PLAYER=" + BGMain.getPlayerID(playerName));
	}
	
	public static void coinUse(String playerName, String kitName) {
		
		BOUGHT_KITS.put(playerName, kitName);
	}
	
	public static boolean sendCoins(String sender, String dest, int coins) {
		
		int scoins = (int) BGMain.getCoins(BGMain.getPlayerID(sender));
		if(scoins >= coins) {
			takeCoins(sender, coins);
			giveCoins(dest, coins);
			return true;
		}
		return false;
	}
}
