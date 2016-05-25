<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>COMP 3613 Assignment 2</title>
	<LINK REL=STYLESHEET HREF="resources/mainstyle.css" TYPE="text/css">
</head>
<body>

<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>
<h2 class="center">${sessionScope.welcome}</h2>
<form action="/3613project-springmvc/addMember" method="post">
	<p class="center"><input type="submit" name="select" value = "${sessionScope.selectText}" /></p>
</form>
<p class="center"><a href="/3613project-springmvc/about">${sessionScope.aboutLink}</a>
<footer class="fixed">
	<p>David Lee, A00783233<br>
	Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale(session.getAttribute("language").toString(), session.getAttribute("country").toString())).format(new Date()) %></p>
</footer>
	
</body>
</html>