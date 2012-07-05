package no.steria.kata.javaee;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PersonServlet extends HttpServlet {

	private static final long serialVersionUID = 7744195856599544243L;
	private PersonDao personDao;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if ("/createPerson.html".equals(req.getPathInfo())) {
			displayCreatePage(resp);
		} else {
			List<Person> people = personDao.findPeople(req.getParameter("name_query"));
			resp.setContentType("text/html");
			
			PrintWriter writer = resp.getWriter();
			writer.append("<html><body>");
			writer.append("<ul>");
			for(Person p: people){
				writer.append("<li>" + p.getName() + "</li>");
			}
			writer.append("</ul>");
			
			writer.append("<form method='GET' action='findPeople.html'>")
			.append("<input type='text' name='name_query' value=''/>")
			.append("<input type='submit' name='findPeople' value='Find person'/>")
			.append("</form>")
			.append("</body></html>");
		}
	}

	private void displayCreatePage(HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		
		PrintWriter writer = resp.getWriter();
		
		writer.append("<form method='POST' action='createPerson.html'>")
		.append("<input type='text' name='full_name' value=''/>")
		.append("<input type='submit' name='createPerson' value='Create person'/>")
		.append("</form>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		personDao.createPerson(Person.withName(req.getParameter("full_name")));
		resp.sendRedirect("/");
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	@Override
	public void init() throws ServletException {
		personDao = new HibernatePersonDao("jdbc/personDs");
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		personDao.beginTransaction();
		super.service(req, resp);
		personDao.endTransaction(true);
		
	}
	
	
	
}
