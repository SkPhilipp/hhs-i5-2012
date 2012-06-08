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
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.jsp"/>
<div class="span6 offset3">
    <form action="confirm.jsp" method="POST">
        <h1><%= trip.getTripType().getName() %></h1>
        <div class="control-group">
            <div class="controls">
                <label>Aantal Volwassenen</label>
                <input type="text" class="input-xlarge" name="amount-adults" value="0">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label>Aantal Kinderen</label>
                <input <% if(trip.getTripType().getKidsAllowed() == false) { %> disabled <% } %>type="text" class="input-xlarge" name="amount-children" value="0">
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
        <h2>Excursies</h2>
        <input type="hidden" name="trip" value="<%= id %>"/>
<%
    if(trip.getExcursionList() == null || trip.getExcursionList().size() == 0){
%>
        <p>Geen excursies bij deze trip.</p>
<%
    }
    else{
        for(Excursion e : trip.getExcursionList()){
%>
        <div class="control-group">
            <label class="control-label">
                <h4><%= e.getExcursionType().getName() %></h4>
                <p><%= e.getExcursionType().getDescription() %></p>
            </label>
            <div class="controls">
                <label>Aantal Personen</label>
                <input type="text" class="input-xlarge" name="excursion-<%= e.getId() %>" value="0">
                <p class="help-block">
                    <em>( <%= formatter.format(e.getPrice()) %> per persoon. Nog ruimte beschikbaar voor <%= e.getRemainingCount() %> personen. Gids: <%= e.getGuide() %> )</em>
                </p>
            </div>
        </div>
<%
       }
    }
%>
        <input type="submit" class="btn btn-primary" value="Bevestigen"/>
    </form>
</div>
<jsp:include page="template-footer.jsp"/>
<%
    }
    // Numberformat exception, NotFound exception, etc.
    catch(Exception e){
        response.sendRedirect("booking.jsp");
    }
%>