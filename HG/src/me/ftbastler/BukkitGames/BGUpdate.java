package me.ftbastler.BukkitGames;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.entity.Player;

public class BGUpdate {

	
	@SuppressWarnings("unused")
	private BGMain plugin;
	
	public static String old = "1.1.0";
	
	public BGUpdate(BGMain plugin) {
		
		this.plugin = plugin;
	}
	
	public void update(Player player) throws Exception{
	
		URL url = new URL("http://nevuscraft.at/Update_Checker/BukkitGames");
		URLConnection con = url.openConnection();
		BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String version;
		
		while ((version = read.readLine()) != null) {
			if (!(version.equalsIgnoreCase(old))) {
				BGChat.printPlayerChat(player, "A new version (" + version + ") of The BukkitGames is available!");
			}
			
		}
		
		read.close();
	}
	
	public void updateOnCommand(Player player) throws Exception{
		
		URL url = new URL("http://nevuscraft.at/Update_Checker/BukkitGames");
		URLConnection con = url.openConnection();
		BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String version;
		
		while ((version = read.readLine()) != null) {
			if (!(version.equalsIgnoreCase(old))) {
				BGChat.printPlayerChat(player, "A new version (" + version + ") of The BukkitGames is available!");
			}else {
				
				BGChat.printPlayerChat(player, "There is no Update for The BukkitGames!");
			}
			
		}
		
		read.close();
	}
	
	public String getVersion() {
		
		return old;
	}
}
