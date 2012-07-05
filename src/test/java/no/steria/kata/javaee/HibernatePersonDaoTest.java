package no.steria.kata.javaee;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.hibernate.cfg.Environment;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Test;

public class HibernatePersonDaoTest {
	
	@Test
	public void shouldSavePerson() throws Exception {
		PersonDao personDao = createPersonDao();
		personDao.beginTransaction();
		Person person = Person.withName("C3P0");
		personDao.createPerson(person);
		List<Person> people = personDao.findPeople(null);
		assertThat(people).contains(person);
		
	}
	
	private PersonDao createPersonDao() throws NamingException{
		JDBCDataSource jdbcDataSource = new JDBCDataSource();
		jdbcDataSource.setDatabase("jdbc:hsqldb:mem:testDb");
		jdbcDataSource.setUser("sa");
		jdbcDataSource.setPassword("");
		System.setProperty(Environment.HBM2DDL_AUTO, "create");
		new EnvEntry("jdbc/personDaoTest", jdbcDataSource);
		return new HibernatePersonDao("jdbc/personDaoTest");
	}

}
