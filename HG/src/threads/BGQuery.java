package threads;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import main.BGMain;

public class BGQuery extends Thread{
	
	private String sql;
	private Logger log;
	private Connection con;
	private BGMain plugin;
	
	public BGQuery(String sql, Logger log, Connection con, BGMain plugin) {
		
		setDaemon(true);
		
		this.sql = sql;
		this.log = log;
		this.con = con;
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		plugin.lock.lock();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException ex) {
			log.warning("Error with following query: "
					+ sql);
			log.warning("MySQL-Error: " + ex.getMessage());
			plugin.SQLdisconnect();
		} catch (NullPointerException ex) {
			log.warning("Error while performing a query. (NullPointerException)");
		}
		plugin.lock.unlock();
	}
}
