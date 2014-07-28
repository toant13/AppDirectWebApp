<%@page import="com.app.dir.persistence.domain.SubscriptionAccount"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.*"%>
 
<jsp:useBean id="subscriptionAccountDao" type="com.app.dir.persistence.domain.dao.SubscriptionAccountDao" scope="request" />
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>List of Subscription accounts</title>
    </head>
 
    <body>	
    	<h1>LIST OF SUBSCRIPTION ACCOUNTS</h1>
        <hr><ol> 
        <% if (subscriptionAccountDao.getAllAccounts().size()>0) { %>
	        <% for (SubscriptionAccount subAccount : subscriptionAccountDao.getAllAccounts()) { %>
	            <%-- <li> <%= account %> </li> --%>
	             <li><p> 
	             	<b>Account Identifier:</b> <%= subAccount.getId() %> </br> 
	             	<b>First Name:</b> <%= subAccount.getFirstName() %></br>
	             	<b>Last Name:</b> <%= subAccount.getLastName() %></br>
	             	<b>Edition:</b> <%= subAccount.getEditionCode() %></br>
	             	
	             </p></li>
	            
	        <% } %>
	    <% } else { %>    
	    	<h3> There are currently no account subscriptions.</h3>
	    <% } %>
        </ol><hr>

	</body>
</html>