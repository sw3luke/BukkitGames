package utilities.enums;

import utilities.BGFiles;

public enum Translation {	
	NO_PERMISSION("NO_PERMISSION"),
	GAME_BEGUN("GAME_BEGUN");

	private String path;
	
	Translation(String path) {
		this.path = path;
	}

	public String t() {
		return BGFiles.messageconf.getString(this.path);
	}
	
	public String p() {
		return this.path;
	}
	
}
