<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="a00783233.a1.CheckLanguage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>COMP 3613 Assignment 2</title>
	<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">
</head>
<body>

<%! Locale current = null; %>
<%  Cookie[] cookies = request.getCookies();
	String currentLanguage = "en";
	String currentCountry = "US";
	current = CheckLanguage.checkCookies(cookies, current); 
	if (current != null) {
		currentLanguage = current.getLanguage();
		currentCountry = current.getCountry();
	}
	else current = new Locale(currentLanguage, currentCountry); %>

<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>
<h2 class="center">
	<% /* Translations for the welcome page are hard-coded, since it won't have been able to get 
		  the ResourceBundle from the servlet yet */
	   if(currentLanguage.compareTo("en") == 0) { %>Welcome!  To view the database, press the button below.<% } %>
	<% if(currentLanguage.compareTo("fr") == 0) { %>Bienvenue! Pour voir la base de données, appuyez sur le bouton ci-dessous.<% } %>
	<% if(currentLanguage.compareTo("el") == 0) { %>Καλώς ήρθατε! Για να δείτε τη βάση δεδομένων, πατήστε το παρακάτω κουμπί.<% } %></h2>
<form action="a00783233.a1.A1Controller" method="post">
	<p class="center"><input type="submit" name="select" 
	<% if(currentLanguage.compareTo("en") == 0) { %>value="Select the members table"<% } %>
	<% if(currentLanguage.compareTo("fr") == 0) { %>value="Aller à la table des membres"<% } %>
	<% if(currentLanguage.compareTo("el") == 0) { %>value="Πηγαίνετε στον πίνακα των μελών"<% } %> class="btn" /></p>
</form>
<p class="center"><a href="about.jsp">
	<% if(currentLanguage.compareTo("en") == 0) { %>About this application<% } %>
	<% if(currentLanguage.compareTo("fr") == 0) { %>A propos de cette application<% } %>
	<% if(currentLanguage.compareTo("el") == 0) { %>Σχετικά με αυτήν την εφαρμογή<% } %></a>
<footer class="fixed">
	<p>David Lee, A00783233<br>
	Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, current).format(new Date()) %></p>
</footer>
	
</body>
</html>