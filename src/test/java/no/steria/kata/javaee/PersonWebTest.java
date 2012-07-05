package no.steria.kata.javaee;

import static org.fest.assertions.Assertions.assertThat;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hibernate.cfg.Environment;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class PersonWebTest {
	@Test
	public void shouldcreateAndDisplayPerson() throws Exception {
		JDBCDataSource jdbcDataSource = new JDBCDataSource();
		jdbcDataSource.setDatabase("jdbc:hsqldb:mem:testDb");
		jdbcDataSource.setUser("sa");
		jdbcDataSource.setPassword("");
		System.setProperty(Environment.HBM2DDL_AUTO, "create");
		new EnvEntry("jdbc/personDs", jdbcDataSource);
		
		Server server = new Server(0);
		server.setHandler(new WebAppContext("src/main/webapp", "/"));
		server.start();
		
		int localPort = server.getConnectors()[0].getLocalPort();
		
		WebDriver browser = createBrowser();
		
		String url = "http://localhost:" + localPort + "/";
		browser.get(url);
		
		browser.findElement(By.linkText("Create person")).click();
		browser.findElement(By.name("full_name")).sendKeys("Darth Vader");
		browser.findElement(By.name("createPerson")).click();
		
		browser.findElement(By.linkText("Find people")).click();
		browser.findElement(By.name("name_query")).sendKeys("th vad");
		browser.findElement(By.name("findPeople")).click();
		
		assertThat(browser.getPageSource()).contains("<li>Darth Vader</li>");
		
		
	}

	private HtmlUnitDriver createBrowser() {
		return new HtmlUnitDriver() {
			@Override
			public WebElement findElement(By by) {
				try {
					return super.findElement(by);
				} catch (NoSuchElementException e) {
					throw new NoSuchElementException("Did not find " + by + " in " + getPageSource());
				}
			}
		};
	}
}
