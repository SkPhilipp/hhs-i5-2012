<%@page import="paradise.model.Trip"%>
<%@page import="paradise.controller.TripJPAController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.html"/>
<%--
    --- webshop/booking.jsp
    Actor kiest reis uit lijst van alle reizen
    [X]Als de reis vol is, hide de reis en geef weer dat ie vol is
    Actor vult in : Aantal volwassenen, Aantal kinderen en bevestigt
        Check de ingevulde waardes, anders redirect terug naar #trip
        Geef gerelateerde producten weer - open in tab : /webshop/product.jsp
--%>
<%
    TripJPAController controller = new TripJPAController();
    for(Trip trip : controller.findEntities()){
        if(trip.getRemainingCount() > 0){
%>
    <div class="span4">
        <div class="thumbnail">
            <div class="caption">
            <h5><%= trip.getID() %> : <%= trip.getTripType().getName() %></h5>
            <p><em>Nog <%= trip.getRemainingCount() %> plekken vrij.</em></p>
            <p><%= trip.getTripType().getDescription() %></p>
            <p><a href="excursions.jsp?trip=<%= trip.getID() %>" class="btn btn-primary">Boeken</a>
            </div>
        </div>
        <p>


        </p>
    </div>
<%
        }
    }
%>
<jsp:include page="template-footer.html"/>