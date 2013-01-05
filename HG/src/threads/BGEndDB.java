package threads;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import main.BGMain;

public class BGEndDB extends Thread{

	private BGMain plugin;
	private Logger log;
	private Connection con;
	
	public BGEndDB(BGMain plugin, Logger log, Connection con) {
		
		setDaemon(false);
		
		this.plugin = plugin;
		this.log = log;
		this.con = con;
	}
	
	@Override
	public void run() {
		plugin.lock.lock();
		try {
			log.warning("Disconnecting from MySQL database...");
			con.close();
		} catch (SQLException ex) {
			log.warning("Error while closing the connection...");
		} catch (NullPointerException ex) {
			log.warning("Error while closing the connection...");
		}

		if(!plugin.SQL_DSC){
			log.info("Reconnecting with MySQL database...");
			plugin.SQLconnect();
		}
		
		plugin.lock.unlock();
	}
}
