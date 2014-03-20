package main.java;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

//import org.apache.log4j.Logger;
import java.util.logging.Logger;

import java.util.Map;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SimpleListener implements ServletContextListener
{
  private Logger log = Logger.getLogger(this.getClass().getName());

  public void contextInitialized(ServletContextEvent event)
  {
    log.info("Context initialized!");
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    String jndiName = "java:comp/env/jdbc/PostgreSQLDS";
    String showTablesSQL = "SELECT * FROM pg_catalog.pg_tables";

        try {
            con = getConnection(jndiName);
            stmt = con.createStatement();
            rs = stmt.executeQuery(showTablesSQL);
            int totalRows = 0;
            
            log.info("Connected to DB");
            while(rs.next()) {
            	totalRows++;
            }
            log.info("Number of tables in tomcat database: " + totalRows);
        } catch(Exception e) {
            log.info("Can Not connect to Database.");
            Map<String, String> env = System.getenv();
            for (String envName : env.keySet()) {
                log.info("" +  envName + ":" + env.get(envName));
            }
            e.printStackTrace();
        } finally {
            log.info("Connection to DB closed.");
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(con);
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

  public void contextDestroyed(ServletContextEvent event)
  {
  }
}
