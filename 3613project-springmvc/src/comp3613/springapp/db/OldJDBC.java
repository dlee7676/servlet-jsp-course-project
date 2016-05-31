/* David Lee, A00783233 */
package comp3613.springapp.db;

import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

public class OldJDBC {
	private Connection con;
	private Statement stmt;
	
	public boolean tableExists(String tableName, String url, String user, String password) {
		try {
			con = DriverManager.getConnection(url, user, password);
			DatabaseMetaData metadata = con.getMetaData();
			ResultSet rs = metadata.getTables(null, null, tableName, null);
			if (rs.next()) {
				con.close();
				return true;
			}
			else {
				con.close();
				return false;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public void createTable(String tableName, String columns, String url, String user, String password) {
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			stmt.execute("IF OBJECT_ID('" + tableName + "') IS NOT NULL DROP TABLE " + tableName);
			String createTable = "CREATE TABLE " + tableName + columns;
			stmt.execute(createTable);
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public void dropTable(String tableName, String url, String user, String password) {
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			stmt.execute("DROP TABLE " + tableName);
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public ResultSet getResultSet(String query, String url, String user, String password) {
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return rs;
	}
	
	public int getRowCount(String tableName, String url, String user, String password) {
		int count = 0;
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			ResultSet rowCount = stmt.executeQuery("SELECT COUNT(*) AS rows FROM " + tableName);
			if (rowCount.next())
				count = rowCount.getInt("rows");
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return count;
	}
	
	public void insert(String tableName, String values, String url, String user, String password) {
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			stmt.execute("INSERT INTO " + tableName + " VALUES (" + values + ")");
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void update(String tableName, String set, String where, String url, String user, String password) {
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			stmt.execute("UPDATE " + tableName + " SET " + set + " WHERE " + where);
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void delete(String tableName, String where, String url, String user, String password) {
		try {
			con = DriverManager.getConnection(url, user, password);
			stmt = con.createStatement();
			stmt.execute("DELETE FROM " + tableName + " WHERE " + where);
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void close() {
		try {
			con.close();
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
