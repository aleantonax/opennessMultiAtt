package provider.sql_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import provider.sql_connection.exception.DriverNotSupportedException;
import provider.sql_connection.exception.SqlServerNotRechableException;

/**
 * It allows to establish the connection to the sql server
 * 
 * @author Francesco Centrone 608758
 *
 */
public class SqlAccess {
	private Connection conn;

	/**
	 * 
	 * @return connection to the Sql server
	 */
	Connection getConnection() {
		return conn;
	}

	/**
	 * Establish the connection with the Sql Server
	 * 
	 * @param driver
	 *            Driver of the JDBC
	 * @param connectionUrl
	 *            url to connect to the database
	 * @throws DriverNotSupportedException
	 * @throws SqlServerNotRechableException
	 */
	SqlAccess(String driver, String connectionUrl) throws DriverNotSupportedException, SqlServerNotRechableException {
		System.out.println("Carico il JDBC");
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(connectionUrl);
		} catch (ClassNotFoundException e) {
			throw new DriverNotSupportedException(
					"Driver " + driver + " is not supported. Try to add the driver jar to the project");
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			throw new SqlServerNotRechableException("Server Sql not reachable with " + connectionUrl);
		}
	}

}
