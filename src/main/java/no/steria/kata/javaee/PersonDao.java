package no.steria.kata.javaee;

import java.util.List;

public interface PersonDao {

	void createPerson(Person withName);

	void beginTransaction();

	void endTransaction(boolean b);

	List<Person> findPeople(String query);

}
