<%@page import="paradise.model.Excursion"%>
<%@page import="paradise.controller.ExcursionJPAController"%>
<%@page import="paradise.model.Trip"%>
<%@page import="paradise.controller.TripJPAController"%>
<%@page import="java.util.Map.Entry"%>
<%
    try{
        String paramAmountKids = request.getParameter("amount-children");
        int amountKids = paramAmountKids == null ? 0 : Integer.parseInt(paramAmountKids);
        String paramAmountAdults = request.getParameter("amount-adults");
        int amountAdults = Integer.parseInt(paramAmountAdults);
        String paramTrip = request.getParameter("trip");

        TripJPAController tripController = new TripJPAController();
        ExcursionJPAController excursionController = new ExcursionJPAController();

        Trip trip = tripController.findEntity(Integer.parseInt(paramTrip));
        if(trip.getRemainingCount() < amountAdults + amountKids){
            // Not enough space in Trip
            session.setAttribute("alert", "U heeft een te hoog aantal personen ingevuld bij de reis; er is niet ruimte voor zoveel personen.");
            response.sendRedirect("excursions.jsp?trip="+paramTrip);
            return;
        }

        if(amountAdults + amountKids == 0){
            session.setAttribute("alert", "U kunt niet met 0 personen op reis..");
            response.sendRedirect("excursions.jsp?trip="+paramTrip);
            return;
        }

        for(String paramName : request.getParameterMap().keySet()){
            if(paramName.startsWith("excursion-")){
                // For each excursion parameter
                String stringExcursionId = paramName.substring("excursion-".length());
                Excursion excursion = excursionController.findEntity(Integer.parseInt(stringExcursionId));
                int reservations = Integer.parseInt(request.getParameter(paramName));
                if(excursion.getRemainingCount() < reservations){
                    // Not enough space in Excursion
                    session.setAttribute("alert", "U heeft een te hoog aantal personen ingevuld bij de excursie \""+excursion.getName()+"\"");
                    response.sendRedirect("excursions.jsp?trip="+paramTrip);
                    return;
                }
                if(reservations > amountAdults + amountKids){
                    // More excursion reservations than trip reservations
                    session.setAttribute("alert", "U heeft bij de excursie \""+excursion.getName()+"\" voor meer personen gereserveerd dan het aantal mensen die met de reis mee gaan.");
                    response.sendRedirect("excursions.jsp?trip="+paramTrip);
                    return;
                }
            }
        }
        /*
         * TODO:Systeem maakt boeking aan
         * TODO:Systeem laat confirm pagina zien met overzicht en totaal prijs     
         */
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.jsp"/>
<jsp:include page="template-footer.jsp"/>
<%
    }
    // Numberformat exception
    catch(NumberFormatException e){
         response.sendRedirect("excursions.jsp?trip="+request.getParameter("trip"));
    }
    // NotFound exception, etc.
    catch(Exception e){
         response.sendRedirect("booking.jsp");
    }
%>