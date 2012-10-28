package utilities;

import java.util.HashMap;

import main.BGMain;

import org.bukkit.plugin.java.JavaPlugin;

public class BGReward extends JavaPlugin{

	private BGMain plugin;
	
	public HashMap<String, String> BOUGHT_KITS = new HashMap<String, String>();
	
	public BGReward(BGMain plugin) {
		this.plugin = plugin;
	}
	
	public void createUser(String playerName) {
		
		if (plugin.getPlayerID(playerName) == null) {
			plugin.SQLquery("INSERT INTO REWARD (REF_PLAYER, COINS) VALUES ("+ plugin.getPlayerID(playerName) + ", 0)");
		}
	}
	
	public void giveCoins(String playerName, int coins) {
		
		plugin.SQLquery("UPDATE REWARD SET COINS = (COINS+"+coins+") WHERE REF_PLAYER=" + plugin.getPlayerID(playerName));
	}
	
	public void takeCoins(String playerName, int coins) {
		
		plugin.SQLquery("UPDATE REWARD SET COINS = (COINS-"+coins+") WHERE REF_PLAYER=" + plugin.getPlayerID(playerName));
	}
	
	public void coinUse(String playerName, String kitName) {
		
		BOUGHT_KITS.put(playerName, kitName);
	}
	
	public boolean sendCoins(String sender, String dest, int coins) {
		
		int scoins = (int) plugin.getCoins(plugin.getPlayerID(sender));
		if(scoins >= coins) {
			takeCoins(sender, coins);
			giveCoins(dest, coins);
			return true;
		}
		return false;
	}
}
