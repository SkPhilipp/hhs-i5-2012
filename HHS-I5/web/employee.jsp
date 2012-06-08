<%--
    @author Groep3 2012 : Anthony Elbers, Philipp Gayret, Bas Mans, Stefan Schouten
--%>
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
<jsp:include page="template-header.jsp"/>
<div class="span6 offset3">
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
        %>
        </tbody>
    </table>
</div>
<jsp:include page="template-footer.jsp"/>