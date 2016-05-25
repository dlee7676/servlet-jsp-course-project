<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>About</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">
</head>
<body>

<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>
<br>
<p>${sessionScope.about}</p>
<p>${sessionScope.buttons}</p>
<form:form action="/3613project-springmvc/changeLanguage" method="post">
	<select name="language">
		<option value="english" <% if(session.getAttribute("language") != null && session.getAttribute("language").toString().compareTo("en") == 0) { %>
			selected="selected" <% } %>>English</option>
		<option value="french" <% if(session.getAttribute("language") != null && session.getAttribute("language").toString().compareTo("fr") == 0) { %>
			selected="selected" <% } %>>Français</option>
		<option value="greek" <% if(session.getAttribute("language") != null && session.getAttribute("language").toString().compareTo("el") == 0) { %>
			selected="selected" <% } %>>ελληνικά</option>
	</select>
	<input type="submit" name="chooseLanguage" value="${sessionScope.chooseLanguageText}" />
	<br>
</form:form>
<form action="/3613project-springmvc/addMember" method="post">
	<p><input type="submit" name="select" value="${sessionScope.selectText}" class="btn" /></p>
</form> 

<footer class="fixed">
	<p>David Lee, A00783233<br>
	Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale(session.getAttribute("language").toString(), session.getAttribute("country").toString())).format(new Date()) %></p>
</footer>

</body>
</html>