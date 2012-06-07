<%@page import="java.text.NumberFormat"%>
<%@page import="paradise.model.Excursion"%>
<%@page import="paradise.model.Trip"%>
<%@page import="paradise.controller.TripJPAController"%>
<%
    String param = request.getParameter("trip");
    try{
        int id = Integer.parseInt(param);
        TripJPAController controller = new TripJPAController();
        Trip trip = controller.findEntity(id);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.html"/>
<%--
    --- webshop/excursion.jsp ---
    Actor kiest meerdere excuries uit lijst van excursies binnen geselecteerde trip
    Actor geeft per excursie aan hoeveel personen er mee gaan en bevestigt
        Als dit meer personen zijn dan is toegestaan komt er een foutmelding
--%>
<div class="span8 offset2">
    <form action="confirm.jsp">
        <h1>Trip</h1>
        <div class="control-group">
            <div class="controls">
                Aantal Personen <input type="text" class="input-xlarge" name="people" value="0">
                <p class="help-block">
                    <em>( <%= formatter.format(trip.getPrice()) %> per persoon. Maximaal <%= trip.getRemainingCount() %> )</em>
                </p>
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
            <label class="control-label"><h4><%= e.getName() %></h4></label>
            <div class="controls">
                Aantal Personen <input type="text" class="input-xlarge" name="<%= e.getID() %>" value="0">
                <p class="help-block">
                    <em>( <%= formatter.format(e.getPrice()) %> per persoon. Maximaal <%= e.getMaxAmountOfPeople() %> personen. Gids: <%= e.getGuide() %> )</em>
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
<jsp:include page="template-footer.html"/>
<%
    }
    // Numberformat exception, NotFound exception, etc.
    catch(Exception e){
        response.sendRedirect("booking.jsp");
    }
%>