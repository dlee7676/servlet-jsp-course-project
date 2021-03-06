<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Session Summary</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">
</head>
<body>

<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>
<h2>${sessionScope.summaryHeading}</h2>
<c:if test="${sessionScope.summary != null}">
	<c:forEach var="statement" items="${sessionScope.summary}">
		<p><c:out value="${statement}" /></p>
	</c:forEach>
</c:if>
<form action="/3613project-springmvc/addMember" method="post">
	<p><input type="submit" name="select" value="${sessionScope.selectText}" class="btn" /></p>
</form> 
<footer class="fixed">
	<p>David Lee, A00783233<br>
	Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale(session.getAttribute("language").toString(), session.getAttribute("country").toString())).format(new Date()) %></p>
</footer>

</body>
</html>