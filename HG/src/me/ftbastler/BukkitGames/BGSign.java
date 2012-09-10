package me.ftbastler.BukkitGames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

public class BGSign extends JavaPlugin{

	@SuppressWarnings("unused")
	private BGMain plugin;
	
	public BGSign(BGMain plugin) {
		
		this.plugin = plugin;
	}
	
	public void createSign (Location loc, String firstLine, String secondLine, String thirdLine, String fourthLine) {
		
		String fline = firstLine;
		String sline = secondLine;
		String tline = thirdLine;
		String foline = fourthLine;
		
		if(fline != null && fline.length() > 15) {
			fline = fline.substring(0, 15);
		}
		if(sline != null && sline.length() > 15) {
			sline = sline.substring(0, 15);
		}
		if(tline != null && tline.length() > 15) {
			tline = tline.substring(0, 15);
		}
		if(foline != null && foline.length() > 15) {
			foline = foline.substring(0, 15);
		}
		
		int flength = 0;
		int slength = 0;
		int tlength = 0;
		int folength = 0;
		
		if(fline != null)	
			flength = fline.length();
		if(sline != null)	
			slength = sline.length();
		if(tline != null)
			tlength = tline.length();
		if(foline != null)
			folength = foline.length();
		
		String fl = "";
		String sl = "";
		String tl = "";
		String fol = "";
		
		for (int i=1; i <= ((int) ((15-flength)/2)); i++) {
			
			fl = fl + " ";
		}
		if (fline != null)
			fl = fl + fline;
		for (int i=15; i > fl.length(); fl = fl + " ");
		
		for (int i=1; i <= ((int) ((15-slength)/2)); i++) {
			
			sl = sl + " ";
		}
		if (sline != null)
			sl = sl + sline;
		for (int i=15; i > sl.length(); sl = sl + " ");
		
		for (int i=1; i <= ((int) ((15-tlength)/2)); i++) {
			
			tl = tl + " ";
		}
		if(tline != null)
			tl = tl + tline;
		for (int i=15; i > tl.length(); tl = tl + " ");
		
		for (int i=1; i <= ((int) ((15-folength)/2)); i++) {
			
			fol = fol + " ";
		}
		if(foline != null)
			fol = fol + foline;
		for (int i=15; i > fol.length(); fol = fol + " ");
		
		Block block = Bukkit.getWorld("world").getBlockAt(loc);
		block.setType(Material.SIGN_POST);
		Sign sign = (Sign) block.getState();
		
		sign.setLine(0, fl);
		sign.setLine(1, sl);
		sign.setLine(2, tl);
		sign.setLine(3, fol);
		
		sign.update(true);
	}
}
