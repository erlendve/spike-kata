package no.steria.kata.javaee;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hibernate.cfg.Environment;
import org.hsqldb.jdbc.JDBCDataSource;

public class JettyServer {
	public static void main(String[] args) throws Exception {
		JDBCDataSource jdbcDataSource = new JDBCDataSource();
		jdbcDataSource.setDatabase("jdbc:hsqldb:mem:testDb");
		jdbcDataSource.setUser("sa");
		jdbcDataSource.setPassword("");
		System.setProperty(Environment.HBM2DDL_AUTO, "create");
		new EnvEntry("jdbc/personDs", jdbcDataSource);
		
		Server server = new Server(8081);
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
		
		System.out.println("http://localhost:8081/");
	}
}
