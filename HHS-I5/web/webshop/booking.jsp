<%@page import="paradise.jpa.TripJpaController"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="paradise.model.Trip"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.jsp"/>
<%
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("HHS-I5PU");
    UserTransaction ut = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
    TripJpaController controller = new TripJpaController(ut, emf);
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
    for(Trip trip : controller.findTripEntities()){
%>
    <div class="span4" style="margin:5px;">
        <div class="thumbnail">
            <div class="caption">
                <h5><%= trip.getId() %> : <%= trip.getTripType().getName() %></h5>
                <p><%= trip.getTripType().getDescription() %></p>
                <% if(trip.getTripType().getKidsAllowed()) { %>
                <p><em>Kinderen niet toegestaan bij deze reis.</em></p>
                <% } %>
                <p><em>Nog <%= trip.getRemainingCount() %> plekken vrij.</em></p>
                <p><em><%= formatter.format(trip.getStartDate()) %> tot en met <%= formatter.format(trip.getEndDate()) %></em></p>
                <% if(trip.getRemainingCount() > 0){ %>
                <p><a href="excursions.jsp?trip=<%= trip.getId() %>" class="btn btn-primary">Boeken</a>
                <% }else{ %>
                <p><a href="#" class="btn btn-primary disabled">Uitverkocht</a>
                <% } %>
            </div>
        </div>
    </div>
<%
    }
%>
<jsp:include page="template-footer.jsp"/>