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
<title>About</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">
</head>
<body>

<%! Locale current = null; %>
<%  Cookie[] cookies = request.getCookies();
	String currentLanguage = "en";
	String currentCountry = "US";
	if (request.getAttribute("language") == null) {
		response.sendRedirect("a00783233.a1.A1Controller");
		current = Locale.getDefault(); 
		request.setAttribute("about", "This is the about page."); 
	}
	current = CheckLanguage.checkCookies(cookies, current);
	if (request.getAttribute("language") != null) {
		currentLanguage = request.getAttribute("language").toString();
		currentCountry = request.getAttribute("country").toString();
		current = new Locale(currentLanguage, currentCountry);
	} %>

<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>
<br>
<p>${sessionScope.about}</p>
<p>${sessionScope.buttons}</p>
<form action="a00783233.a1.A1Controller" method="post">
	<select name="language">
		<option value="english" <% if(currentLanguage != null && currentLanguage.compareTo("en") == 0) { %>
			selected="selected" <% } %>>English</option>
		<option value="french" <% if(currentLanguage != null && currentLanguage.compareTo("fr") == 0) { %>
			selected="selected" <% } %>>Français</option>
		<option value="greek" <% if(currentLanguage != null && currentLanguage.compareTo("el") == 0) { %>
			selected="selected" <% } %>>ελληνικά</option>
	</select>
	<input type="submit" name="chooseLanguage" value="${sessionScope.chooseLanguageText}" />
	<br>
	<p><input type="submit" name="select" value="${sessionScope.selectText}" class="btn" /></p>
</form> 
<footer class="fixed">
	<p>David Lee, A00783233<br>
	Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, current).format(new Date()) %></p>
</footer>

</body>
</html>