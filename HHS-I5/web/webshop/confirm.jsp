<%@page import="paradise.jpa.ExcursionJpaController"%>
<%@page import="paradise.jpa.TripJpaController"%>
<%@page import="paradise.model.Booking"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.HashMap"%>
<%@page import="paradise.model.Excursion"%>
<%@page import="paradise.model.Trip"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%
    try{
        double price = 0;
        String paramAmountKids = request.getParameter("amount-children");
        int amountKids = paramAmountKids == null ? 0 : Integer.parseInt(paramAmountKids);
        String paramAmountAdults = request.getParameter("amount-adults");
        int amountAdults = Integer.parseInt(paramAmountAdults);
        String paramTrip = request.getParameter("trip");
        boolean cancellationInsurance = "on".equals(request.getParameter("cancellation-insurance"));
        if(cancellationInsurance){
            price += Booking.CANCELLATION_INSURANCE_PRICE;
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HHS-I5PU");
        UserTransaction ut = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
        TripJpaController tripController = new TripJpaController(ut, emf);
        ExcursionJpaController excursionController = new ExcursionJpaController(ut, emf);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        Trip trip = tripController.findTrip(Integer.parseInt(paramTrip));
        if(trip.getRemainingCount() < amountAdults + amountKids){
            // Not enough space in Trip
            session.setAttribute("alert", "U heeft een te hoog aantal personen ingevuld bij de reis; er is niet ruimte voor zoveel personen.");
            response.sendRedirect("excursions.jsp?trip="+paramTrip);
            return;
        }
        price += trip.getPrice() * ( amountAdults + amountKids );

        if(amountAdults + amountKids <= 0 || amountAdults < 0 || amountKids < 0){
            session.setAttribute("alert", "Er moet minimaal 1 persoon op reis..");
            response.sendRedirect("excursions.jsp?trip="+paramTrip);
            return;
        }

        HashMap<Excursion, Integer> excursionMap = new HashMap<Excursion, Integer>();
        for(String paramName : request.getParameterMap().keySet()){
            if(paramName.startsWith("excursion-")){
                // For each excursion parameter
                String stringExcursionId = paramName.substring("excursion-".length());
                Excursion excursion = excursionController.findExcursion(Integer.parseInt(stringExcursionId));
                int reservations = Integer.parseInt(request.getParameter(paramName));
                if(excursion.getRemainingCount() < reservations){
                    // Not enough space in Excursion
                    session.setAttribute("alert", "U heeft een te hoog aantal personen ingevuld bij de excursie \""+excursion.getExcursionType().getName()+"\"");
                    response.sendRedirect("excursions.jsp?trip="+paramTrip);
                    return;
                }
                if(reservations > amountAdults + amountKids){
                    // More excursion reservations than trip reservations
                    session.setAttribute("alert", "U heeft bij de excursie \""+excursion.getExcursionType().getName()+"\" voor meer personen gereserveerd dan het aantal mensen die met de reis mee gaan.");
                    response.sendRedirect("excursions.jsp?trip="+paramTrip);
                    return;
                }
                if(reservations > 0){
                    price += excursion.getPrice() * ( reservations );
                    excursionMap.put(excursion, reservations);
                }
            }
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.jsp"/>
<%
        if(request.getParameter("confirm") == null){
%>
    <div class="span6 offset3">
        <form action="confirm.jsp" method="POST">
            <input type="hidden" name="confirm" value="1"/>
            <h1><%= trip.getTripType().getName() %></h1>
            <input type="hidden" name="trip" value="<%= trip.getId() %>"/>
            <p>Aantal volwassenen: <%= amountAdults %></p>
            <p>Aantal kinderen: <%= amountKids %></p>
            <input type="hidden" name="amount-adults" value="<%= amountAdults %>"/>
            <input type="hidden" name="amount-children" value="<%= amountKids %>"/>
            <input type="hidden" name="cancellation-insurance" value="<%= request.getParameter("cancellation-insurance") %>"/>
            <p><%= cancellationInsurance ? "inclusief" : "geen" %> annuleringsverzekering</p>
            <%
                if(excursionMap.isEmpty() == false){
            %>
            <h2>Excursies</h2>
            <%
                }
                for(Excursion e : excursionMap.keySet()){
            %>
            <p><%= excursionMap.get(e) %>x <%= e.getExcursionType().getName() %></p>
            <input type="hidden" name="excursion-<%= e.getId() %>" value="<%= excursionMap.get(e) %>"/>
            <%
                }
            %>
            <p>Totale kosten: <%= formatter.format(price) %></p>
            <input type="submit" class="btn btn-primary" value="Bevestigen"/>
        </form>
    </div>
<%
        }
        else{
            /*
            * TODO:Systeem maakt boeking aan  
            */
%>
    <h3>Boeking aangemaakt.</h3>
<%
        }
%>
<jsp:include page="template-footer.jsp"/>
<%
    }
    // Numberformat exception
    catch(NumberFormatException e){
        session.setAttribute("alert", "AUB alleen getallen invullen - velden op 0 laten staan als er niemand mee gaat op een reis of excursie.");
        response.sendRedirect("excursions.jsp?trip="+request.getParameter("trip"));
    }
    // NotFound exception, etc.
    catch(Exception e){
        response.sendRedirect("booking.jsp");
    }
%>