package no.steria.kata.javaee;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.context.ThreadLocalSessionContext;

public class HibernatePersonDao implements PersonDao {

	private SessionFactory sessionFactory;
	
	
	public HibernatePersonDao(String jndi) {
		Configuration configuration = new Configuration();
		configuration.setProperty(Environment.DATASOURCE, jndi);
		configuration.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, ThreadLocalSessionContext.class.getName());
		configuration.addAnnotatedClass(Person.class);
		sessionFactory = configuration.buildSessionFactory();
		
	}

	@Override
	public void createPerson(Person withName) {
		session().save(withName);
	}

	@Override
	public void beginTransaction() {
		session().beginTransaction();
	}

	@Override
	public void endTransaction(boolean b) {
		session().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> findPeople(String query) {
		return session().createCriteria(Person.class).list();
	}
	
	private Session session(){
		return sessionFactory.getCurrentSession();
	}

}
