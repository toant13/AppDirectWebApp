<%@page import="com.app.dir.persistence.domain.Subscription"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.*"%>
 
<jsp:useBean id="subscriptionDao" type="com.app.dir.persistence.domain.dao.SubscriptionDao" scope="request" />
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>Subscriptions list</title>
    </head>
 
    <body>	
    	<h1>LIST OF SUBSCRIPTION</h1>
        <hr><ol> 
        <%
         	if (subscriptionDao.getAllAccounts().size()>0) {
         %>
	        <%
	        	for (Subscription subAccount : subscriptionDao.getAllAccounts()) {
	        %>
	            <%-- <li> <%= account %> </li> --%>
	             <li><p> 
	             	<b>Account Identifier:</b> <%= subAccount.getId() %> </br> 
	             	<b>Creator First Name:</b> <%= subAccount.getFirstName() %></br>
	             	<b>Creator Last Name:</b> <%= subAccount.getLastName() %></br>
	             	<b>Edition:</b> <%= subAccount.getEditionCode() %></br>
	             	<b>Current Number of Assigned Users: </b><%= subAccount.getUsers().size() %></br>
	             </p></li>
	            
	        <% } %>
	    <% } else { %>    
	    	<h3> There are currently no subscriptions for this app.</h3>
	    <% } %>
        </ol><hr>

	</body>
</html>