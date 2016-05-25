package comp3613.springapp;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.tutorial.domain.MembersTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import comp3613.springapp.db.DatabaseAccess;
import comp3613.springapp.util.ValidatorBean;

@Controller
public class DatabaseFormController {
	DatabaseAccess dao;
	Properties dbProps;
	
	@RequestMapping(value="/viewDatabase", method=RequestMethod.GET)
	public ModelAndView viewDatabase() {
		return new ModelAndView("viewDatabase", "command", new Member());
	}
	
	@RequestMapping(value="/addMember", method=RequestMethod.POST)
	public String addMember(@ModelAttribute("members")Member member, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String insert = "'" + member.getfName() + "', " +
						"'" + member.getlName() + "', " +
						"'" + member.getAddress() + "', " +
						"'" + member.getCity() + "', " +
						"'" + member.getCode() + "', " +
						"'" + member.getCountry() + "', " +
						"'" + member.getPhone() + "', " +
						"'" + member.getEmail() + "'";
		String update = "fName='" + member.getfName() + "', " +
						"lName='" + member.getlName() + "', " +
						"address='" + member.getAddress() + "', " +
						"city='" + member.getCity() + "', " +
						"code='" + member.getCode() + "', " +
						"country='" + member.getCountry() + "', " +
						"phone='" + member.getPhone() + "', " +
						"email='" + member.getEmail() + "'";
						
		HttpSession session = request.getSession();
		Enumeration input = request.getParameterNames();
		boolean validInput = readInput(input, request, response);
		if (validInput) {
			updateTable(session, request, insert, update, member);
			request.setAttribute("insert", insert);
			request.setAttribute("update", update);
		}
		readTable(request);
		return "viewDatabase";
	}
	
	/* Read and validate any form input */
	public boolean readInput(Enumeration input, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean validInput = true;
		while(input.hasMoreElements()) {
			String nextParam = ((String) input.nextElement()).trim();
			if(nextParam.compareTo("id") != 0 && nextParam.compareTo("add") != 0 && 
					nextParam.compareTo("update") != 0 && nextParam.compareTo("delete") != 0) {
				String nextValue = request.getParameterValues(nextParam)[0].trim();
				// capitalize input properly
				if (nextParam.compareTo("code") == 0) {
					nextValue = nextValue.toUpperCase();
				}
				if (nextParam.compareTo("fName") == 0 || nextParam.compareTo("lName") == 0 || 
						nextParam.compareTo("city") == 0 || nextParam.compareTo("country") == 0) {
					if (nextValue.length() < 2)
						nextValue = nextValue.toUpperCase();
					else 
						nextValue = Character.toUpperCase(nextValue.charAt(0)) + nextValue.substring(1);
				}
				// server-side validation
				if(nextValue == null) {
					response.sendError(400, "Invalid input: " + nextParam + " cannot be a null value");
					validInput = false;
					break;
				}
				if (nextParam.compareTo("fName") == 0 && nextValue.isEmpty()) {
					response.sendError(400, "Invalid input: please enter a first name");
					validInput = false;
					break;
				}
				if (nextParam.compareTo("lName") == 0 && nextValue.isEmpty()) {
					response.sendError(400, "Invalid input: please enter a last name");
					validInput = false;
					break;
				}
				if (nextParam.compareTo("phone") == 0 && !nextValue.isEmpty() && !ValidatorBean.isValidInput(nextValue, "^\\(?(\\d{3})\\)?[\\.\\-\\/ ]?(\\d{3})[\\.\\-\\/ ]?(\\d{4})$")) {
					response.sendError(400, "Invalid input: '" + nextValue + "' is not a valid phone number; "
						+ "must be in the format ###-###-####");
					validInput = false;
					break;
				}
				if (nextParam.compareTo("email") == 0 && !nextValue.isEmpty() && !ValidatorBean.isValidInput(nextValue, "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")) {
					response.sendError(400, "Invalid input: '" + nextValue + "' is not a valid email address; "
						+ "must be in the format (name)@(domain).xxx");
					validInput = false;
					break;
				}
			}
		}
		return validInput;
	}
	
	/* Perform any CRUD operations on the table */
	public void updateTable(HttpSession session, HttpServletRequest request, String insert, String update, Member member) throws ServletException, IOException {
		dao = Initializer.getDao();
		dbProps = Initializer.getProps();
		//Session hSession = Initializer.getSessionFactory().getCurrentSession();
		ArrayList<String> summary;
		if (session.getAttribute("summary") == null)
			summary = new ArrayList<String>();
		else summary = (ArrayList<String>)session.getAttribute("summary");
		if (request.getParameter("add") != null) {
			Session hSession = Initializer.getSessionFactory().getCurrentSession();
			try {
				hSession.beginTransaction();
				MembersTable mt = new MembersTable();
				mt.setfName("Hibernate");
				mt.setlName("Test");
				hSession.save(mt);
				hSession.getTransaction().commit();
				/*dao.insert("a00783233_members", insert, "jdbc:sqlserver://Beangrinder.bcit.ca", "javastudent", "compjava");*/
				summary.add("INSERT INTO a00783233_members " + insert);
			}
			catch (HibernateException ex) {
				if (hSession.getTransaction() != null)
					hSession.getTransaction().rollback();
				ex.printStackTrace();
			}
		}
		if (request.getParameter("update") != null) {
			Session hSession = Initializer.getSessionFactory().getCurrentSession();
			try {
				hSession.beginTransaction();
				Long id = new Long(request.getParameter("id"));
				MembersTable mt = hSession.get(MembersTable.class, id); 
				mt.setfName(member.getfName());
				mt.setlName("Hibernate");
				hSession.update(mt);
				hSession.getTransaction().commit();
				//dao.update("a00783233_members", update, "id='" + member.getId() + "'", "jdbc:sqlserver://Beangrinder.bcit.ca", "javastudent", "compjava");
				summary.add("UPDATE a00783233_members SET " + update + " WHERE id='" + request.getParameter("id") + "'");
			}
			catch (HibernateException ex) {
				if (hSession.getTransaction() != null)
					hSession.getTransaction().rollback();
				ex.printStackTrace();
			}
		}
		if (request.getParameter("delete") != null) {
			Session hSession = Initializer.getSessionFactory().getCurrentSession();
			try {
				hSession.beginTransaction();
				Long id = new Long(request.getParameter("id"));
				MembersTable mt = hSession.get(MembersTable.class, id); 
				hSession.delete(mt);
				hSession.getTransaction().commit();
				//dao.delete("a00783233_members", "id='" + member.getId() + "'", "jdbc:sqlserver://Beangrinder.bcit.ca", "javastudent", "compjava");
				summary.add("DELETE FROM a00783233_members WHERE id='" + request.getParameter("id") + "'");
			}
			catch (HibernateException ex) {
				if (hSession.getTransaction() != null)
					hSession.getTransaction().rollback();
				ex.printStackTrace();
			}
		}
		session.setAttribute("summary", summary);
		session.setAttribute("summaryLength", summary.size());
	}
	
	/* Read the current contents of the table */
	public void readTable(HttpServletRequest request) throws ServletException, IOException {
		// read data from the table to determine what is shown in the jsp page
		Session hSession = Initializer.getSessionFactory().getCurrentSession();
		try {
			hSession.beginTransaction();
			List result = hSession.createQuery("from MembersTable").list();
			hSession.getTransaction().commit();
			request.setAttribute("tableList", result);
		}
		catch (HibernateException ex) {
			if (hSession.getTransaction() != null)
				hSession.getTransaction().rollback();
			ex.printStackTrace();
		}
		
		/*dao = Initializer.getDao();
		dbProps = Initializer.getProps();
		int rowCount = dao.getRowCount("a00783233_members", "jdbc:sqlserver://Beangrinder.bcit.ca", "javastudent", "compjava");
		rowCount = dao.getRowCount("a00783233_members", "jdbc:sqlserver://Beangrinder.bcit.ca", "javastudent", "compjava");
		request.setAttribute("rowCount", rowCount);
		ArrayList<ArrayList<String>> tableContents = new ArrayList<ArrayList<String>>(); 
		ResultSet status;
		try {
			status = dao.getResultSet("SELECT * FROM a00783233_members", "jdbc:sqlserver://Beangrinder.bcit.ca", "javastudent", "compjava");
			int row = 0;
			while (status.next()) {
				int column = 0;
				tableContents.add(row, new ArrayList<String>());
				tableContents.get(row).add(column++, Integer.toString(status.getInt("id")));
				tableContents.get(row).add(column++, status.getString("fName"));
				tableContents.get(row).add(column++, status.getString("lName"));
				tableContents.get(row).add(column++, status.getString("address"));
				tableContents.get(row).add(column++, status.getString("city"));
				tableContents.get(row).add(column++, status.getString("code"));
				tableContents.get(row).add(column++, status.getString("country"));
				tableContents.get(row).add(column++, status.getString("phone"));
				tableContents.get(row).add(column++, status.getString("email"));
				row++;
			}
			request.setAttribute("tableContents", tableContents);
			dao.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}*/
	}
}
