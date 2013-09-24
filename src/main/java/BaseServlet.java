package main.java;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

import java.util.List;
import java.util.ArrayList;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class BaseServlet extends HttpServlet 
{

	private static boolean initialized = false;

	private String jndiName;
	private String showTablesSQL;

	public BaseServlet(String jndiName, String showTablesSQL) {
		this.jndiName = jndiName;
		this.showTablesSQL = showTablesSQL;
	}
	
	private static void initDatabaseDriver(PrintWriter out) {

		try {
			Class c = Class.forName("org.postgresql.Driver");
			out.println("Loaded class: " + c.getName());
			out.println("Trying to register driver...");

			if(initialized)
				return;
			DriverManager.registerDriver(new org.postgresql.Driver());
			initialized = true;
		}
		catch(Exception e) {
			e.printStackTrace(out);
		}
	}
	
	public void init(ServletConfig config) throws ServletException
	{
	}

	//public abstract List<String> showTables() throws Exception;

	private List<String> showTables() throws Exception {
	// the database name is tomcat configured in META-INF/context.xml
	List<String> tableNames = new ArrayList<String>();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		con = getConnection(jndiName);
		stmt = con.createStatement();
		rs = stmt.executeQuery(showTablesSQL);
		int totalRows = 0;

		while(rs.next()) {
			totalRows++;
			System.out.println("Table in tomcat database: " + rs.getString(1));
			tableNames.add(rs.getString(1));
		}
		System.out.println("Number of tables in tomcat database: " + totalRows);
	} catch(Exception e) {
		e.printStackTrace();
		throw e;
	} finally {
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(con);
	}
	return tableNames;
}
	
//	private List<String> showTables() throws Exception {
//		// the database name is tomcat configured in META-INF/context.xml
//		List<String> tableNames = new ArrayList<String>();
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		try {
//			con = getConnection("java:comp/env/jdbc/postgres");
//			stmt = con.createStatement();
//			rs = stmt.executeQuery("SELECT * FROM pg_catalog.pg_tables");
//			int totalRows = 0;
//
//			while(rs.next()) {
//				totalRows++;
//				System.out.println("Table in tomcat database: " + rs.getString(1));
//				tableNames.add(rs.getString(1));
//			}
//			System.out.println("Number of tables in tomcat database: " + totalRows);
//		} catch(Exception e) {
//			e.printStackTrace();
//			throw e;
//		} finally {
//			closeResultSet(rs);
//			closeStatement(stmt);
//			closeConnection(con);
//		}
//		return tableNames;
//	}

	private void listSystemProperties() {
		System.out.println("System Properties");
		System.getProperties().list(System.out);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 "
				+ "Transitional//EN\">\n");
		out.println("doGet");

		out.println("System Properties");
		System.getProperties().list(out);
		out.println("<hr/>");

		initDatabaseDriver(out);
		out.println("<hr/>");

		try {
			List<String> tableNames = showTables();
			out.println("<html>");
			out.println("<body>");
			for (String tableName : tableNames)
				out.println("table: " + tableName);

		} catch (Exception e) {
			e.printStackTrace(out);
		}

		out.println("<hr/>");

		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

  public static void closeResultSet(ResultSet rs) {
    try {
      rs.close();
    } catch (Exception e) {
    }
  }

  public static void closeConnection(Connection con) {
    try {
      con.close();
    } catch (Exception e) {
    }
  }

  public static void closeStatement(Statement stmt) {
    try {
      stmt.close();
    } catch (Exception e) {
    }
  }

  public static Connection getConnection(String dataSourceJNDI) throws SQLException, Exception
  {
      try
      {
          InitialContext jndiCntx = new InitialContext();
          DataSource ds = (DataSource) jndiCntx.lookup(dataSourceJNDI);
          return ds.getConnection();
      }
      catch (NamingException ne)
      {
          throw new Exception(ne);
      }
  }
}
