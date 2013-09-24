package main.java;

/**
 * @author bmaxwell
 *
 */
public class PostgresServlet extends BaseServlet {

	/**
	 * 
	 */
	public PostgresServlet() {
		super("java:comp/env/jdbc/PostgreSQLDS", "SELECT * FROM pg_catalog.pg_tables");
	}
}
