package no.steria.kata.javaee;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class PersonServletTest {
	
	PersonServlet servlet = new PersonServlet();
	
	PersonDao personDao = mock(PersonDao.class);
	HttpServletRequest req = mock(HttpServletRequest.class);
	HttpServletResponse resp = mock(HttpServletResponse.class);
	StringWriter htmlsource = new StringWriter();
	
	@Test
	public void shouldDisplayCreatePage() throws Exception {
	
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn("/createPerson.html");
		
		servlet.service(req, resp);
		
		verify(resp).setContentType("text/html");
		
		assertThat(htmlsource.toString())
		.contains("<form method='POST' action='createPerson.html'")
		.contains("<input type='text' name='full_name' value=''")
		.contains("<input type='submit' name='createPerson' value='Create person'");
	
		DocumentHelper.parseText(htmlsource.toString());
	}
	
	@Test
	public void shouldCreatePerson() throws Exception {
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getParameter("full_name")).thenReturn("Yoda");
		
		servlet.service(req, resp);
		
		InOrder order = inOrder(personDao);
		
		order.verify(personDao).beginTransaction();
		order.verify(personDao).createPerson(Person.withName("Yoda"));
		order.verify(personDao).endTransaction(true);
		verify(resp).sendRedirect("/");
	}
	
	@Before
	public void setup() throws IOException{
		servlet.setPersonDao(personDao);
		when(resp.getWriter()).thenReturn(new PrintWriter(htmlsource));
	}
	
	@Test
	public void shouldDisplaySearchPage() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn("/findPeople.html");
		
		servlet.service(req, resp);
		
		verify(resp).setContentType("text/html");
		
		assertThat(htmlsource.toString())
		.contains("<form method='GET' action='findPeople.html'")
		.contains("<input type='text' name='name_query' value=''")
		.contains("<input type='submit' name='findPeople' value='Find person'");
	
		DocumentHelper.parseText(htmlsource.toString());
	}
	
	@Test
	public void shouldSearchForPeople() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn("/findPeople.html");
		when(req.getParameter("name_query")).thenReturn("Darth");
		
		servlet.service(req, resp);

		verify(personDao).findPeople("Darth");
	}
	
	@Test
	public void shouldDisplaySearchResult() throws Exception {
		when(req.getMethod()).thenReturn("GET");
		when(req.getPathInfo()).thenReturn("/findPeople.html");
		when(req.getParameter("name_query")).thenReturn("Darth");
		when(personDao.findPeople(anyString())).thenReturn(Arrays.asList(Person.withName("Obi Wan"), Person.withName("R2D2")));
		
		servlet.service(req, resp);

		assertThat(htmlsource.toString()).contains("<li>Obi Wan</li>")
		.contains("<li>R2D2</li>");
		
		DocumentHelper.parseText(htmlsource.toString());
	}
}
