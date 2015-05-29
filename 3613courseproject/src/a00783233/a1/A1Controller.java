/* David Lee, A00783233 */

package a00783233.a1;

import a00783233.a1.db.DatabaseAccess;
import a00783233.a1.util.ValidatorBean;
import a00783233.a1.util.EncryptionBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class A1Controller
 */

public class A1Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DatabaseAccess dbAccess;
    private Properties dbProps;
	private ResourceBundle resourceBundle;
	private String bundlePath;
	private ArrayList<String> summary;
	private byte[] salt;
	private int iterationCount;
	private boolean validInput;
	private boolean authenticated;
	private String insert, update;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public A1Controller() {
        super();
    }
    
    public void init() {
    	dbAccess = new DatabaseAccess();
    	// set DAO at application scope
    	getServletContext().setAttribute("dao", dbAccess);
		bundlePath = "a00783233.a1.language";
		resourceBundle = ResourceBundle.getBundle(bundlePath);
		salt = new byte[]{ (byte) 0xf5, (byte) 0x33, (byte) 0x01,
    			(byte) 0x2a, (byte) 0xb2, (byte) 0xcc, (byte) 0xe4, (byte) 0x7f };
    	iterationCount = 100;
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		dbProps = new Properties();
		String propertiesPath = getServletContext().getRealPath("/WEB-INF/resources/dbprops.properties");
		validInput = true;
		authenticated = false;
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Cookie[] cookies = request.getCookies();
		if (session.getAttribute("summary") == null)
			summary = new ArrayList<String>();
		else summary = (ArrayList<String>)session.getAttribute("summary");
		
		handleLanguages(cookies, session, request, response);
		processAuthentication(propertiesPath, dbProps, cookies, session, request, response);
		// get information for displaying and modifying the database table
		if (authenticated && (request.getParameter("select") != null || request.getParameter("add") != null || 
			request.getParameter("update") != null || request.getParameter("delete") != null || 
			request.getParameter("login") != null)) {
			// read the input parameters and construct the necessary insert or update statements
			Enumeration input = request.getParameterNames();
			insert = "";
			update = "";
			readInput(input, request, response);
			// apply CRUD actions
			updateTable(session, request, response, insert, update);
			// read data from the table to determine what is shown in the jsp page
			readTable(request, response);
		}
		// setup requestDispatcher
		handleDispatch(request, response);
	}
	
	public synchronized DatabaseAccess getDAO() {
		return (DatabaseAccess)getServletContext().getAttribute("dao");
	}
	
	/* Determine the locale to use based on language settings */
	public void handleLanguages(Cookie[] cookies, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		String currentLanguage = "en";
		String currentCountry = "US";
		// check if language settings are stored in a cookie
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().compareTo("languageCookie") == 0) {
				   currentLanguage = cookies[i].getValue();
			    }
			    if (cookies[i].getName().compareTo("countryCookie") == 0) {
				   currentCountry = cookies[i].getValue();
			    }
			}
		}
		// handle changes to language settings from About page
		if (request.getParameter("language") != null) {
			if (request.getParameter("language").compareTo("english") == 0) {
				currentLanguage = "en";
				currentCountry = "US";
			}
			if (request.getParameter("language").compareTo("french") == 0) {
				currentLanguage = "fr";
				currentCountry = "FR";
			}
			if (request.getParameter("language").compareTo("greek") == 0) {
				currentLanguage = "el";
				currentCountry = "EL";
			}
		}
		request.setAttribute("language", currentLanguage);
		request.setAttribute("country", currentCountry);
		// set text values from resource bundle
		setStaticText(session, currentLanguage, currentCountry);
		// set language cookies with current settings
		Cookie languageCookie = null;
		Cookie countryCookie = null;
		languageCookie = new Cookie("languageCookie", currentLanguage);
		countryCookie = new Cookie("countryCookie", currentCountry);
		languageCookie.setMaxAge(100);
		countryCookie.setMaxAge(100);
		response.addCookie(languageCookie);
		response.addCookie(countryCookie);
	}
	
	/* Set static text based on the chosen language settings */
	public void setStaticText(HttpSession session, String currentLanguage, String currentCountry)  {
		resourceBundle = ResourceBundle.getBundle(bundlePath, new Locale(currentLanguage, currentCountry));
		session.setAttribute("about", resourceBundle.getString("About_Text"));
		session.setAttribute("buttons", resourceBundle.getString("Buttons_Text"));
		session.setAttribute("loginText", resourceBundle.getString("Login_Text"));
		session.setAttribute("loginButton", resourceBundle.getString("Login_Button_Text"));
		session.setAttribute("rememberText", resourceBundle.getString("Remember_Text"));
		session.setAttribute("chooseLanguageText", resourceBundle.getString("ChooseLanguage_Text"));
		session.setAttribute("selectText", resourceBundle.getString("Select_Text"));
		session.setAttribute("aboutLink", resourceBundle.getString("About_Link_Text"));
		session.setAttribute("summaryLink", resourceBundle.getString("Summary_Link_Text"));
		session.setAttribute("indexLink", resourceBundle.getString("Index_Link_Text"));
		session.setAttribute("summaryHeading", resourceBundle.getString("Summary_Heading"));
		session.setAttribute("tableHeading", resourceBundle.getString("Table_Heading"));
		session.setAttribute("tableText", resourceBundle.getString("Table_Text"));
		session.setAttribute("idText", resourceBundle.getString("id_text"));
		session.setAttribute("fNameText", resourceBundle.getString("fName_text"));
		session.setAttribute("lNameText", resourceBundle.getString("lName_text"));
		session.setAttribute("addressText", resourceBundle.getString("address_text"));
		session.setAttribute("cityText", resourceBundle.getString("city_text"));
		session.setAttribute("codeText", resourceBundle.getString("code_text"));
		session.setAttribute("countryText", resourceBundle.getString("country_text"));
		session.setAttribute("phoneText", resourceBundle.getString("phone_text"));
		session.setAttribute("emailText", resourceBundle.getString("email_text"));
		session.setAttribute("updateText", resourceBundle.getString("Update_Text"));
		session.setAttribute("deleteText", resourceBundle.getString("Delete_Text"));
		session.setAttribute("insertText", resourceBundle.getString("Insert_Text"));
		session.setAttribute("resetText", resourceBundle.getString("Reset_Text"));
		session.setAttribute("actionText", resourceBundle.getString("Action_Text"));
		session.setAttribute("autofillText", resourceBundle.getString("Autofill_Text"));
		session.setAttribute("fNameError", resourceBundle.getString("fName_Error"));
		session.setAttribute("lNameError", resourceBundle.getString("lName_Error"));
		session.setAttribute("phoneError", resourceBundle.getString("phone_Error"));
		session.setAttribute("emailError", resourceBundle.getString("email_Error"));
		session.setAttribute("deleteConfirm", resourceBundle.getString("delete_Confirm"));
	}
	
	/* Read the database properties file if the correct password is used at the login */
	public void processAuthentication(String propertiesPath, Properties dbProps, Cookie[] cookies, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// handle authentication if user has logged in already
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().compareTo("authenticationCookie") == 0 && cookies[i].getValue().compareTo("authenticated") == 0) {
					authenticated = true;
					InputStream is = EncryptionBean.decryptFile(propertiesPath, "java3613", salt, iterationCount);
					session.setAttribute("authenticated", true);
					session.setAttribute("login", "java3613");
					session.setAttribute("properties", dbProps);
					dbProps.load(is);
					try {
						Class.forName(dbProps.getProperty("Driver"));
					} 
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (session.getAttribute("authenticated") != null) {
			authenticated = (boolean)session.getAttribute("authenticated");
			InputStream is = EncryptionBean.decryptFile(propertiesPath, session.getAttribute("login").toString(), salt, iterationCount);
			dbProps.load(is);
		}
		// handle authentication if the user hasn't logged in yet; set up database information if the login is correct
		if (request.getParameter("login") != null) {
			InputStream is = EncryptionBean.decryptFile(propertiesPath, request.getParameter("login"), salt, iterationCount);
			dbProps.load(is);
			if(dbProps.getProperty("Driver") == null) {
				response.sendError(400, "Decryption of the database properties failed; your password may be incorrect.");
				validInput = false;
			}
			else {
				authenticated = true;
				session.setAttribute("authenticated", true);
				session.setAttribute("login", request.getParameter("login"));
				if (request.getParameter("remember") != null) {
					Cookie authenticationCookie = new Cookie ("authenticationCookie", "authenticated");
					authenticationCookie.setMaxAge(100);
					response.addCookie(authenticationCookie);
				}
				try {
					Class.forName(dbProps.getProperty("Driver"));
				} 
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				// create the members table if it doesn't exist
				if (!getDAO().tableExists("a00783233_members", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"))) {
					getDAO().createTable("a00783233_members", "(id INT IDENTITY PRIMARY KEY, fName CHAR(64) NOT NULL, "
							+ "lName CHAR(64) NOT NULL, address CHAR(128), city CHAR(128), code CHAR(32), "
							+ "country CHAR(128), phone CHAR(32), email CHAR(128))", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
				}
			}
		}
	}
	
	/* Read and validate any form input */
	public void readInput(Enumeration input, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				update = update + nextParam + "='" + nextValue + "'";
				insert = insert + " '" + nextValue + "'";
				if (nextParam.compareTo("email") != 0) {
					insert = insert + ", ";
					update = update + ", ";
				}
			}
		}
	}
	
	/* Perform any CRUD operations on the table */
	public void updateTable(HttpSession session, HttpServletRequest request, HttpServletResponse response, 
			String insert, String update) throws ServletException, IOException {
		if (request.getParameter("add") != null && validInput && authenticated) {
			getDAO().insert("a00783233_members", insert, dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
			summary.add("INSERT INTO a00783233_members " + insert);
		}
		if (request.getParameter("update") != null && validInput) {
			getDAO().update("a00783233_members", update, "id='" + request.getParameter("id") + "'", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
			summary.add("UPDATE a00783233_members SET " + update + " WHERE id='" + request.getParameter("id") + "'");
			int a = 0;
			a = 1;
		}
		if (request.getParameter("delete") != null && validInput) {
			getDAO().delete("a00783233_members", "id='" + request.getParameter("id") + "'", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
			summary.add("DELETE FROM a00783233_members WHERE id='" + request.getParameter("id") + "'");
		}
		session.setAttribute("summary", summary);
		session.setAttribute("summaryLength", summary.size());
	}
	
	/* Read the current contents of the table */
	public void readTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// read data from the table to determine what is shown in the jsp page
		int rowCount = getDAO().getRowCount("a00783233_members", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
		rowCount = getDAO().getRowCount("a00783233_members", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
		request.setAttribute("rowCount", rowCount);
		
		ArrayList<ArrayList<String>> tableContents = new ArrayList<ArrayList<String>>(); 
		ResultSet status;
		try {
			status = getDAO().getResultSet("SELECT * FROM a00783233_members", dbProps.getProperty("URL"), dbProps.getProperty("User"), dbProps.getProperty("Password"));
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
			getDAO().close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	/* Dispatch request and response to the appropriate page */
	public void handleDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (validInput) {
			response.setContentType("text/html");
			// disable caching, so the database information will be freshly retrieved when the page is accessed
			response.setHeader("Cache-Control", "no-cache, no-store");
			
			RequestDispatcher dispatcher;	
			if (!authenticated && (request.getParameter("login") != null || request.getParameter("select") != null || 
					request.getParameter("add") != null || request.getParameter("update") != null || 
					request.getParameter("delete") != null))
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/login.jsp");
			else if (request.getParameter("login") != null || request.getParameter("select") != null || 
					request.getParameter("add") != null || request.getParameter("update") != null || 
					request.getParameter("delete") != null) {
				dispatcher = getServletContext().getRequestDispatcher("/viewDatabase.jsp");
			}
			else dispatcher = getServletContext().getRequestDispatcher("/about.jsp");
			dispatcher.forward(request, response);
		}
	}
}
