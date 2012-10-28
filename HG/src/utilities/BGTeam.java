package utilities;

import java.util.ArrayList;
import java.util.HashMap;

import main.BGMain;

import org.bukkit.entity.Player;

public class BGTeam {

	@SuppressWarnings("unused")
	private BGMain plugin;
	
	static HashMap<Player, ArrayList<String>> teams = new HashMap<Player, ArrayList<String>>();
	
	public BGTeam(BGMain plugin){
		
		this.plugin = plugin;
	}
	
	public static void addMember(Player player, String memberName) {
		
		ArrayList<String> members = teams.get(player);
		members.add(memberName);
		teams.put(player, members);
	}
	
	public static void removeMember(Player player, String memberName) {
		
		ArrayList<String> members = teams.get(player);
		members.remove(memberName);
		teams.put(player, members);
	}
	
	public static String[] getTeamList(Player player) {
		
		ArrayList<String> members = teams.get(player);
		
		return (String[]) members.toArray();
	}
	
	public static boolean isInTeam(Player player, String memberName) {
		
		ArrayList<String> members = teams.get(player);
		
		if(members.contains(memberName))
			return true;
		
		return false;
	}
}
