package no.steria.kata.javaee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person {
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	public static Person withName(String name) {
		Person person = new Person();
		person.name = name;
		return person;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "person: " + name;
	}
	
	@Override
	public boolean equals(Object other) {
		return name.equals(((Person) other).name);
	}
	
	@Override
	public int hashCode() {
		return 1;
	}

}
