package provider.sql_connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import provider.sql_connection.exception.DriverNotSupportedException;
import provider.sql_connection.exception.SqlServerNotRechableException;

/**
 * It allows to execute queries on a sql server
 * 
 * @author Francesco Centrone 608758
 *
 */
public class SqlReader {
	private Connection conn;
	private SqlAccess access;

	/**
	 * Constructor that allows to configure the SqlReader
	 * 
	 * @param conf
	 *            string containing the configuration parameters for the
	 *            SqlReader
	 * @throws SqlServerNotRechableException
	 * @throws DriverNotSupportedException
	 */
	public SqlReader(String driver, String connectionUrl)
			throws DriverNotSupportedException, SqlServerNotRechableException {
		assert (driver != null) : "Null parameter driver";
		assert (connectionUrl != null) : "Null parameter connectionUrl";
		

		access = new SqlAccess(driver, connectionUrl);
	}

	/**
	 * Execute a query passed from parameter and returns an arraylist of
	 * hashmaps containing the result rows.
	 * 
	 * @param query
	 *            that will be execute on the database
	 * @return ArrayList of HashMaps. In each HashMap the keys are the names of
	 *         the coloums
	 * @throws SQLException
	 *             if there are any errors in the query
	 */
	public ArrayList<HashMap<String, String>> execute(String query) throws SQLException {
		assert (query != null) : "Null parameter";
		this.newConnection();
		ResultSet rs = null;
		PreparedStatement prepStat = null;
		ArrayList<HashMap<String, String>> al = null;

		prepStat = conn.prepareStatement(query);
		System.out.println(prepStat);
		rs = prepStat.executeQuery();
		al = this.resultSetToHashMap(rs);

		this.closeConnection();
		return al;
	}

	/**
	 * Opens a new connection
	 */
	private void newConnection() {
		conn = access.getConnection();
	}

	/**
	 * Closes the connection
	 */
	private void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Trasform the result set in an HashMap
	 * 
	 * @param rs
	 *            ResultSet of a executed query
	 * @return ArrayList of HashMaps rappresenting the rows. In each HashMap the
	 *         keys are the names of the coloums
	 * @throws SQLException
	 *             if there are any errors in the ResultSet
	 */
	private ArrayList<HashMap<String, String>> resultSetToHashMap(ResultSet rs) throws SQLException {
		assert (rs != null) : "Null parameter";
		ResultSetMetaData rsmd = rs.getMetaData();
		ArrayList<HashMap<String, String>> h = new ArrayList<HashMap<String, String>>();
		while (rs.next()) {
			HashMap<String, String> row = new HashMap<String, String>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String coloumnName = rsmd.getColumnName(i);

				row.put(coloumnName, rs.getString(coloumnName));
			}
			h.add(row);

		}
		return h;
	}

}
