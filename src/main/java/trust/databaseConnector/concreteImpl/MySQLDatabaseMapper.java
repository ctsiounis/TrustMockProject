package trust.databaseConnector.concreteImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.mysql.cj.jdbc.exceptions.PacketTooBigException;

import trust.databaseConnector.databaseAbstraction.IDatabaseMapper;
import trust.infoManager.utils.ARValue;
import trust.infoManager.utils.RValue;
import trust.infoManager.utils.TDValue;

public class MySQLDatabaseMapper implements IDatabaseMapper {

	private Connection conn;

	public MySQLDatabaseMapper() {
		createDatabase();
	}

	@Override
	public Map<String, Map<String, TDValue>> executeTDQuery(String query) {
		Map<String, Map<String, TDValue>> result = new HashMap<String, Map<String, TDValue>>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String recommender = rs.getString("recommender");
				String recommendee = rs.getString("recommendee");
				double trust = rs.getDouble("trust");
				double distrust = rs.getDouble("distrust");
				int count = rs.getInt("count");

				Map<String, TDValue> recommenderMap = result.getOrDefault(recommender, new HashMap<String, TDValue>());
				recommenderMap.put(recommendee, new TDValue(trust, distrust, count));
				result.put(recommender, recommenderMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}
		}

		return result;
	}

	@Override
	public Map<String, Map<String, RValue>> executeRQuery(String query) {
		Map<String, Map<String, RValue>> result = new HashMap<String, Map<String, RValue>>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String recommender = rs.getString("recommender");
				String recommendee = rs.getString("recommendee");
				double value = rs.getDouble("value");
				int count = rs.getInt("count");

				Map<String, RValue> recommenderMap = result.getOrDefault(recommender, new HashMap<String, RValue>());
				recommenderMap.put(recommendee, new RValue(value, count));
				result.put(recommender, recommenderMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}
		}

		return result;
	}

	@Override
	public Map<String, ARValue> executeARQuery(String query) {
		Map<String, ARValue> result = new HashMap<String, ARValue>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String recommender = rs.getString("recommender");
				double aggregate = rs.getDouble("aggregate");
				int count = rs.getInt("count");

				result.put(recommender, new ARValue(aggregate, count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}
		}

		return result;
	}

	@Override
	public boolean executeUpdate(List<String> queries) {
		Statement stmt = null;
		boolean completed = false;
		int split = queries.size();
		int pieces = 1;
		
		
		try {
			stmt = conn.createStatement();
			while (!completed) {
				try {
					if (pieces == 1) {
						split = queries.size() / 2;
						pieces = 2;
						String query = String.join(" ", queries);
						stmt.executeUpdate(query);
					} else {
						for (int i = 0; i < pieces; i++) {
							int start = i * split;
							int end;
							if (i == (pieces - 1))
								end = queries.size();
							else
								end = (i + 1) * split;
							
							StringJoiner queryJoiner = new StringJoiner(" ");
							for (int j = start; j < end; j++) {
								queryJoiner.add(queries.get(j));
							}
							String query = String.join(" ", queryJoiner.toString());
							stmt.executeUpdate(query);
						}
					}
					completed = true;
				} catch (PacketTooBigException ptbe) {
					split /= 2;
					pieces *= 2;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}
		}
		
		return true;
	}

	public boolean populateDatabase(List<String> queries) {
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			
			// Drop tables to get rid of previous data
			String dropTable;
			dropTable = "DROP TABLE IF EXISTS rec2rec;";
			stmt.execute(dropTable);
			dropTable = "DROP TABLE IF EXISTS ar;";
			stmt.execute(dropTable);
			dropTable = "DROP TABLE IF EXISTS rec2sp;";
			stmt.execute(dropTable);
			// Create tables again
			createTables(stmt);
			
			// Add data to tables;
			for (String query: queries) {
				stmt.execute(query);
			}
			
		} catch (SQLException e) {
			System.out.println("MySQL Database population: " + e.getMessage());
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}
		}
		
		return true;
	}

	private void createDatabase() {
		String url = "jdbc:mysql://localhost:3306/recommendations?allowMultiQueries=true";
		String user = "root";
		String pass = "";
		Statement stmt = null;

		try {
			conn = DriverManager.getConnection(url, user, pass);
			stmt = conn.createStatement();
			createTables(stmt);
		} catch (SQLException e) {
			System.out.println("MySQL Database creation: " + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmt = null;
			}
		}
	}

	private void createTables(Statement stmt) throws SQLException {
		if (stmt != null) {
			String createTable;

			// Create table for R values
			createTable = "CREATE TABLE IF NOT EXISTS rec2rec (\n" + " recommender varchar(16) NOT NULL,\n"
					+ " recommendee varchar(16) NOT NULL,\n" + " value double,\n" + " count integer,\n"
					+ " PRIMARY KEY(recommender, recommendee)" + ");";
			stmt.execute(createTable);

			// Create table for AR values
			createTable = "CREATE TABLE IF NOT EXISTS ar (\n" + " recommender varchar(16) NOT NULL PRIMARY KEY,\n"
					+ " aggregate double,\n" + " count integer\n" + ");";
			stmt.execute(createTable);

			// Create table for TD values
			createTable = "CREATE TABLE IF NOT EXISTS rec2sp (\n" + " recommender varchar(16) NOT NULL,\n"
					+ " recommendee varchar(16) NOT NULL,\n" + " trust double,\n" + " distrust double,\n" + " count integer,\n"
					+ " PRIMARY KEY(recommender, recommendee)" + ");";
			stmt.execute(createTable);
		}
	}


}
