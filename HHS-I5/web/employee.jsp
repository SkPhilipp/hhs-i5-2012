<%@page import="java.util.List"%>
<%@page import="paradise.model.Employee"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="javax.annotation.Resource"%>
<%@page import="paradise.jpa.EmployeeJpaController"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.persistence.Persistence"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Outdoor Paradise</title>
	<meta name="viewport" content="width=device-width">
	<link rel="stylesheet" href="webshop/css/bootstrap.min.css">
	<style>
		body{
			padding-top: 60px;
                        background-image: url("webshop/img/logoOPStar.png");
                        background-position: 0px -50px;
                        background-repeat: no-repeat;
		}
	</style>
	<link rel="stylesheet" href="webshop/css/bootstrap-responsive.min.css">
    </head>
    <body>
        <div class="container" style="padding:5px;">
            <div class="row">
                <div class="span10 offset1">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID#</th>
                            <th>SS#</th>
                            <th>Email Address</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            EntityManagerFactory emf = Persistence.createEntityManagerFactory("HHS-I5PU");
                            UserTransaction transaction = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");

                            EmployeeJpaController ejc = new EmployeeJpaController(transaction, emf);
                            transaction.begin();
                            List<Employee> employees = ejc.findEmployeeEntities();
                            for(Employee e : employees){
                        %>
                            <tr>
                                <td><%= e.getId() %></td>
                                <td><%= e.getSocialSecurityNumber() %></td>
                                <td><%= e.getEmail() %></td>
                            </tr>
                        <%
                            }
                            transaction.commit();
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>