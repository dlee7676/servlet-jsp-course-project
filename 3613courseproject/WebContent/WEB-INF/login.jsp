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
<title>Login</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">
</head>
<body>
<%! Locale current = null; %>
<%  Cookie[] cookies = request.getCookies();
	String currentLanguage = "en";
	String currentCountry = "US";
	current = CheckLanguage.checkCookies(cookies, current); 
	if (current == null)
		current = new Locale(currentLanguage, currentCountry); %>
<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>
<p>${sessionScope.loginText}</p>
<form action="a00783233.a1.A1Controller" method="post">
	<input type="text" name="login" />
	<input type="submit" name="submitPassword" value="${sessionScope.loginButton}" class="btn" />
	<p><input type="checkbox" name="remember">${sessionScope.rememberText}</input></p>
</form>
<p><a href="about.jsp">${sessionScope.aboutLink}</a></p>
<footer class="fixed">
	<p>David Lee, A00783233<br>
	Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, current).format(new Date()) %></p>
</footer>
	
</body>
</html>