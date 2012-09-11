package me.ftbastler.BukkitGames;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BGReward extends JavaPlugin{

	private BGMain plugin;
	
	public ArrayList<String> rewardKits = new ArrayList<String>();
	public ArrayList<String> playerNames = new ArrayList<String>();
	
	public BGReward(BGMain plugin) {
		
		this.plugin = plugin;
		
		FileConfiguration rewardConfig = YamlConfiguration.loadConfiguration(
				new File(plugin.getDataFolder(), "reward.yml"));
		List<String> kits = rewardConfig.getStringList("KITS");
		for (String kit : kits) {
			kit = kit.toLowerCase();
			rewardKits.add(kit);
		}
	}
	
	public void createUser(String playerName) {
		
		if (plugin.getPlayerID(playerName) == null) {
			plugin.SQLquery("INSERT INTO WINNERS (REF_PLAYER, POINTS) VALUES ("+ plugin.getPlayerID(playerName) + ", 0)");
		}
	}
	
	public void givePoints(String playerName, int points) {
		
		plugin.SQLquery("UPDATE WINNERS SET points = (points+"+points+") WHERE REF_PLAYER=" + plugin.getPlayerID(playerName));
	}
	
	public void takePoints(String playerName, int points) {
		
		plugin.SQLquery("UPDATE WINNERS SET points = (points-"+points+") WHERE REF_PLAYER=" + plugin.getPlayerID(playerName));
	}
	
	public void coinUse(String playerName) {
		
		playerNames.add(playerName);
	}
	
	public boolean sendCoins(String sender, String dest, int coins) {
		
		int npoints = coins* plugin.getConfig().getInt("POINTS_FOR_COIN");
		int points = (int) plugin.getPoints(sender);
		if(points >= npoints) {
			takePoints(sender, npoints);
			givePoints(dest, npoints);
			return true;
		}
		return false;
	}
	
	public boolean sendPoints(String sender, String dest, int points) {
		
		int hpoints = (int) plugin.getPoints(sender);
		if(points <= hpoints) {
			takePoints(sender, points);
			givePoints(dest, points);
			return true;
		}
		return false;
	}
}
