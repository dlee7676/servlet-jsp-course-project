<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Members Table</title>
<LINK REL=STYLESHEET HREF="mainstyle.css" TYPE="text/css">

<SCRIPT language="javascript" type="text/javascript">
	re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
	regexp = /^\(?(\d{3})\)?[\.\-\/ ]?(\d{3})[\.\-\/ ]?(\d{4})$/


	function submitIt(myForm)
	{
		errMsg = ""
		if(myForm.fName.value == ""){
			errMsg = errMsg + "${sessionScope.fNameError}\n"
		}
		if(myForm.lName.value == ""){
			errMsg = errMsg + "${sessionScope.lNameError}\n"
		}
		if (myForm.phone.value.trim() != "" && !regexp.test(myForm.phone.value.trim())) {
			errMsg = errMsg + "${sessionScope.phoneError}\n"
		}
		if (myForm.email.value.trim() != "" && !re.test(myForm.email.value.trim())) {
			errMsg = errMsg + "${sessionScope.emailError}\n"
		}

		if(errMsg != ""){
			alert(errMsg)
			myForm.focus()
			return false
		}
		return true
	}
</SCRIPT>

</head>
<body>

<header>
	<h1>COMP 3613 Assignment 2</h1>
</header>

<p> The row count is ${rowCount}</p>
<p> You typed in ${insert}</p>
<p> You updated ${update}</p>

<p> The table existed, so fName = ${fName} </p>

<p><a href="/3613project-springmvc/summary">${sessionScope.summaryLink}</a></p>
<p><a href="/3613project-springmvc/about">${sessionScope.aboutLink}</a></p>
<p><a href="/3613project-springmvc/">${sessionScope.indexLink}</a></p>

<div id="main">
	<br>
	<table border="1">
		<tr>
			<td>${sessionScope.idText}</td>
			<td>${sessionScope.fNameText}</td>
			<td>${sessionScope.lNameText}</td>
			<td>${sessionScope.addressText}</td>
			<td>${sessionScope.cityText}</td>
			<td>${sessionScope.codeText}</td>
			<td>${sessionScope.countryText}</td>
			<td>${sessionScope.phoneText}</td>
			<td>${sessionScope.emailText}</td>
			<td>${sessionScope.actionText}</td>
		</tr>
		<c:if test="${requestScope.tableList != null}">
	    	<c:forEach var="row" items="${requestScope.tableList}">
			   	<form:form action="/springappv3a/addMember" method="post">
				<tr>
					<td><input type="text" name="id" value="${row[0]}" readonly /></td>
					<td><input type="text" name="fName" value="${row[1]}" /></td>
					<td><input type="text" name="lName" value="${row[2]}" /></td>
					<td><input type="text" name="address" value="${row[3]}" /></td>
					<td><input type="text" name="city" value="${row[4]}" /></td>
					<td><input type="text" name="code" value="${row[5]}" /></td>
					<td><input type="text" name="country" value="${row[6]}" /></td>
					<td><input type="text" name="phone" value="${row[7]}" /></td>
					<td><input type="text" name="email" value="${row[8]}" /></td>
					<td><input type="submit" name="update" value="${sessionScope.updateText}" class="btn" />
						<input type="submit" name="delete" value="${sessionScope.deleteText}" class="btn" 
							onClick="return confirm('${sessionScope.deleteConfirm}')"/></td>
				</tr>
		       	</form:form>
		    </c:forEach>
		</c:if>
		<form:form method="POST" modelAttribute="add" action="/springappv3a/addMember" onSubmit="submitIt(this)">
			<tr>
				<td>Autofill</td>
				<td><input type="text" name="fName" /></td>
				<td><input type="text" name="lName" /></td>
				<td><input type="text" name="address" /></td>
				<td><input type="text" name="city" /></td>
				<td><input type="text" name="code" /></td>
				<td><input type="text" name="country" /></td>
				<td><input type="text" name="phone" /></td>
				<td><input type="text" name="email" /></td>
				<td><input type="submit" name="add" value="${sessionScope.insertText}" class="btn" />
					<input type="reset" class="btn" value="${sessionScope.resetText}" /></td>
			</tr>
		</form:form>
	</table>
</div>
<br>

<c:if test="${requestScope.rowCount <= 3}">
	<footer class="fixed">
		<p>David Lee, A00783233<br>
		Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale(session.getAttribute("language").toString(), session.getAttribute("country").toString())).format(new Date()) %></p>
	</footer>
</c:if>

<c:if test="${requestScope.rowCount > 3}">
	<footer>
		<p>David Lee, A00783233<br>
		Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale(session.getAttribute("language").toString(), session.getAttribute("country").toString())).format(new Date()) %></p>
	</footer>
</c:if>

</body>
</html>