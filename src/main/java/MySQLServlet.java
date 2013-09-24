package main.java;

public class MySQLServlet extends BaseServlet {

	public MySQLServlet() {
		super("jdbc/MysqlDS", "show tables;");
	}
}
