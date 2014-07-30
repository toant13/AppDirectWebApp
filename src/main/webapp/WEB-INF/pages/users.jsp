<%@page import="com.app.dir.persistence.domain.User"%>
<%@page import="com.app.dir.persistence.domain.Subscription"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.*"%>
 
<jsp:useBean id="subscriptionDao" type="com.app.dir.persistence.domain.dao.SubscriptionDao" scope="request" />
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>User List</title>
    </head>
 
    <body>	
    	<h1>LIST OF USERS</h1>
        <hr><ol> 
        <%
         	if (subscriptionDao.getAllUsers().size()>0) {
         %>
	        <%
	        	for (User user : subscriptionDao.getAllUsers()) {
	        %>
	             <li><p> 	
	             	<b>First Name: </b><%= user.getFirstName() %></br>
	             	<b>Last Name: </b><%= user.getLastName() %></br>
	             	<b>Email: </b><%= user.getEmail() %></br>
	             	<b>Subscription Account Identifier: </b><%= user.getSubscription().getId() %></br>
	             </p></li>
	            
	        <% } %>
	    <% } else { %>    
	    	<h3> There are currently no users using app.</h3>
	    <% } %>
        </ol><hr>
	</body>
</html>