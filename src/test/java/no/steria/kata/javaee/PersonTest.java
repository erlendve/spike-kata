package no.steria.kata.javaee;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class PersonTest {

	@Test
	public void factoryShouldReturnPersonWithGivenName() throws Exception {
		assertThat(Person.withName("Darth").getName()).isEqualTo("Darth");
	}
}
