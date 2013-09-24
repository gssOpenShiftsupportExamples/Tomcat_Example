package main.java;

public class MySQLServlet extends BaseServlet {

	public MySQLServlet() {
		super("java:comp/env/jdbc/MysqlDS", "show tables;");
	}
}
