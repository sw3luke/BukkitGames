package utilities.enums;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import main.BGMain;

public enum Translation {	
	NO_PERMISSION,
	GAME_BEGUN,
	CMD_ONLY_PLAYER_ACCESS,
	TELEPORTED_SPAWN, 
	PLAYER_NOT_ONLINE, 
	TEAM_FUNC_ADDED_PLAYER, 
	TEAM_FUNC_PLAYER_ALREADY_TEAM, 
	TEAM_FUNC_CMDS, 
	TEAM_FUNC_REMOVED_PLAYER, 
	TEAM_FUNC_YOUR_TEAM, TELEPORT_FUNC_CMDS,
	TEAM_FUNC_DISABLED,
	TELEPORT_FUNC_COORDS_NOT_VALID, 
	TELEPORT_FUNC_TELEPORTED_COORDS,
	TELEPORT_FUNC_TELEPORTED_PLAYER, 
	UPDATE_CHECK_DISABLED, 
	UPDATE_DOWNLOAD_DISABLED, 
	UPDATE_DOWNLOAD_VERSION, 
	UPDATE_DOWNLOAD_COMPLETE, 
	UPDATE_DOWNLOAD_ERROR, 
	UPDATE_NO_UPDATE, 
	REWARD_FUNC_DISABLED, 
	REWARD_FUNC_CMDS, 
	REWARD_FUNC_NOT_ALLOWED, 
	REWARD_FUNC_ALL_KITS, 
	REWARD_FUNC_ALREADY_BOUGHT, 
	KIT_NOT_EXIST, 
	REWARD_FUNC_KIT_NO_BUY, 
	REWARD_FUNC_BOUGHT_KIT, 
	REWARD_FUNC_NOT_ENOUGH_COINS, 
	REWARD_FUNC_INVALID_INT, 
	REWARD_FUNC_SUCCESS_COIN_SENT, 
	REWARD_FUNC_SUCCESS_COIN_RECEIVED, 
	REWARD_FUNC_SUCCESS_TRANSACTION, 
	REWARD_FUNC_STATS, 
	COMPASS_TRACK,
	COMPASS_NOT_TRACK, 
	BLOCK_DESTROY_NOT_ALLOWS, 
	SPECTATORS_CHAT_NOT_ALLOWS, 
	MESSAGE_TOO_LONG, 
	SPECTATOR_IN_THE_WAY, 
	MAY_ODDS_BE_IN_YOUR_FAVOR, 
	GAMES_HAVE_BEGUN, 
	INVINCIBLE_FOR,
	NOW_GAMEMAKER, 
	NOW_SPECTATOR;

	private String path;
	private FileConfiguration e = null;
	
	Translation() {
		this.path = toString();
	}

	public String t() {
		if(e == null) {
			if(BGMain.LANGUAGE == Language.ENGLISH)
				e = YamlConfiguration.loadConfiguration(
					new File(BGMain.instance.getDataFolder(), "en.yml"));
			else if(BGMain.LANGUAGE == Language.GERMAN)
				e = YamlConfiguration.loadConfiguration(
					new File(BGMain.instance.getDataFolder(), "de.yml"));
			else
				e = YamlConfiguration.loadConfiguration(
					new File(BGMain.instance.getDataFolder(), "en.yml"));
		}
		
		return e.getString(this.path);
	}
	
	public String p() {
		return this.path;
	}
	
}
