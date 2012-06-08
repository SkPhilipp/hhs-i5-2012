<%@page import="paradise.model.Employee"%>
<%@page import="paradise.controller.EmployeeJPAController"%>
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
                            EmployeeJPAController employeeController = new EmployeeJPAController();
                            for(Employee e : employeeController.findEntities()){
                        %>
                        <tr>
                            <td><%= e.getID() %></td>
                            <td><%= e.getSocialSecurityNumber() %></td>
                            <td><%= e.getEmail() %></td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>