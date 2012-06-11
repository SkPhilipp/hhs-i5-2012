<%--
    @author Groep3 2012 : Anthony Elbers, Philipp Gayret, Bas Mans, Stefan Schouten
--%>
<%@page import="paradise.model.Product"%>
<%@page import="java.util.List"%>
<%@page import="paradise.jpa.TripJpaController"%>
<%@page import="paradise.model.Booking"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="paradise.model.Excursion"%>
<%@page import="paradise.model.Trip"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%
    String param = request.getParameter("trip");
    try{
        int id = Integer.parseInt(param);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HHS-I5PU");
        UserTransaction ut = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
        TripJpaController controller = new TripJpaController(ut, emf);
        Trip trip = controller.findTrip(id);
        List<Product> products = trip.getTripType().getProductList();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.jsp"/>
<div class="span4 offset<%= products.size() > 0 ? 1 : 4 %>">
    <form action="confirm.jsp" method="POST">
        <h1><%= trip.getTripType().getName() %></h1>
        <div class="control-group">
            <div class="controls">
                <label>Aantal Volwassenen</label>
                <input type="text" class="input" name="amount-adults" value="0">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label>Aantal Kinderen</label>
                <input <% if(trip.getTripType().getKidsAllowed() == false) { %> disabled <% } %>type="text" class="input" name="amount-children" value="0">
                <p class="help-block">
                    <em>( <%= formatter.format(trip.getPrice()) %> per persoon. Maximaal <%= trip.getRemainingCount() %> )</em>
                </p>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" name="cancellation-insurance">
                    Annuleringsverzekering: <%= formatter.format(Booking.CANCELLATION_INSURANCE_PRICE) %> —
                </label>
            </div>
        </div>
        <input type="hidden" name="trip" value="<%= id %>"/>
<%
    if(trip.getExcursionList() == null || trip.getExcursionList().size() > 0){
        for(Excursion e : trip.getExcursionList()){
%>
        <div class="control-group">
            <label class="control-label">
                <h3>Excursie: <%= e.getExcursionType().getName() %></h3>
                <p><%= e.getExcursionType().getDescription() %></p>
            </label>
            <div class="controls">
                <label>Aantal Personen</label>
                <input type="text" <%= e.getRemainingCount() == 0 ? "disabled" : "" %> class="input" name="excursion-<%= e.getId() %>" value="0">
                <p class="help-block">
                    <em>( <%= formatter.format(e.getPrice()) %> per persoon. Nog ruimte beschikbaar voor <%= e.getRemainingCount() %> personen. Gids: <%= e.getGuide() %> )</em>
                </p>
            </div>
        </div>
        <hr/>
<%
       }
    }
%>
        <input type="submit" class="btn btn-primary" value="Bevestigen &raquo;"/>
    </form>
</div>
<% if(products.size() > 0){ %>
    <div class="span6">
        <h2>Gerelateerde Producten</h2>
        <ul class="thumbnails">
        <% for(Product product : products){ %>
            <li class="span3">
                <div class="thumbnail">
                    <img src="http://placehold.it/260x180" alt="">
                    <div class="caption">
                        <h5><%= product.getProductName() %></h5>
                        <p><a href="#" class="btn btn-primary">Kopen</a></p>
                    </div>
                </div>
            </li>
        <% } %>
        </ul>
    </div>
<% } %>
<jsp:include page="template-footer.jsp"/>
<%
    }
    // Numberformat exception, NotFound exception, etc.
    catch(Exception e){
        response.sendRedirect("booking.jsp");
    }
%>