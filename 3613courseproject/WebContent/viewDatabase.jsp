<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="a00783233.a1.CheckLanguage" %>
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

<% /* prevent this page from being accessed without getting the table values from the servlet */
   if (request.getAttribute("tableContents") == null) 
       response.sendRedirect("a00783233.a1.A1Controller"); %>
       
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

<div id="main">
	<br>
	<h2>${sessionScope.tableHeading}</h2>
	<p>${sessionScope.tableText}</p>
	<p><a href="summary.jsp">${sessionScope.summaryLink}</a></p>
	<p><a href="about.jsp">${sessionScope.aboutLink}</a></p>
	<p><a href="index.jsp">${sessionScope.indexLink}</a></p>
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
		
		<%  if (request.getAttribute("tableContents") != null) {
		    	for (int i = 0; i < Integer.parseInt(request.getAttribute("rowCount").toString()); i++) { 
					request.setAttribute("count", i);%>
				   	<form action="a00783233.a1.A1Controller" method="post" onSubmit="return submitIt(this)">
					<tr>
						<td><input type="text" name="id" value="${requestScope.tableContents[count][0]}" readonly /></td>
						<td><input type="text" name="fName" value="${requestScope.tableContents[count][1]}" /></td>
						<td><input type="text" name="lName" value="${requestScope.tableContents[count][2]}" /></td>
						<td><input type="text" name="address" value="${requestScope.tableContents[count][3]}" /></td>
						<td><input type="text" name="city" value="${requestScope.tableContents[count][4]}" /></td>
						<td><input type="text" name="code" value="${requestScope.tableContents[count][5]}" /></td>
						<td><input type="text" name="country" value="${requestScope.tableContents[count][6]}" /></td>
						<td><input type="text" name="phone" value="${requestScope.tableContents[count][7]}" /></td>
						<td><input type="text" name="email" value="${requestScope.tableContents[count][8]}" /></td>
						<td><input type="submit" name="update" value="${sessionScope.updateText}" class="btn" />
							<input type="submit" name="delete" value="${sessionScope.deleteText}" class="btn" 
								onClick="return confirm('${sessionScope.deleteConfirm}')"/></td>
					</tr>
			       	</form>
		<%     } 
		   }%>
		<form action="a00783233.a1.A1Controller" method="post" onSubmit="return submitIt(this)">
			<tr>
				<td>${sessionScope.autofillText}</td>
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
		</form>
	</table>
</div>
<br>

<c:if test="${requestScope.rowCount <= 3}">
	<footer class="fixed">
		<p>David Lee, A00783233<br>
		Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, current).format(new Date()) %></p>
	</footer>
</c:if>

<c:if test="${requestScope.rowCount > 3}">
	<footer>
		<br>
		<p>David Lee, A00783233<br>
		Current time: <%= DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, current).format(new Date()) %></p>
	</footer>
</c:if>

</body>
</html>