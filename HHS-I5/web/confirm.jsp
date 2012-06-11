<%--
    @author Groep3 2012 : Anthony Elbers, Philipp Gayret, Bas Mans, Stefan Schouten
--%>
<%@page import="paradise.jpa.PrivateJpaController"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.validation.ConstraintViolationException"%>
<%@page import="javax.validation.ConstraintViolation"%>
<%@page import="paradise.model.Private"%>
<%@page import="paradise.model.BookingExcursionPK"%>
<%@page import="paradise.model.BookingExcursion"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="paradise.jpa.BookingExcursionJpaController"%>
<%@page import="paradise.jpa.BookingJpaController"%>
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
            PrivateJpaController privateController = new PrivateJpaController(ut, emf);
            //TODO: Private id ; not implementing login system for demo.
            Private private1 = privateController.findPrivate(1);
            // -- Create booking
            Booking booking = new Booking();
            booking.setAmountOfAdults((short)amountAdults);
            booking.setAmountOfKids((short)amountKids);
            booking.setHasCancellationInsurance(cancellationInsurance);
            booking.setSalePrice(price);
            booking.setTrip(trip);
            booking.setPrivate1(private1);
            if(private1.getBookingList() == null){
                private1.setBookingList(new ArrayList<Booking>());
            }
            private1.getBookingList().add(booking);
            if(trip.getBookingList() == null){
                trip.setBookingList(new ArrayList<Booking>());
            }
            trip.getBookingList().add(booking);
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
            // -- Booking Excursion relationship
            List<BookingExcursion> excursions = new ArrayList<BookingExcursion>();
            for(Excursion e : excursionMap.keySet()){
                if(excursionMap.get(e) > 0){
                    BookingExcursion bk = new BookingExcursion(booking.getId(), e.getId());
                    bk.setBooking1(booking);
                    bk.setExcursion1(e);
                    bk.setAmountOfPeople(excursionMap.get(e));
                    excursions.add(bk);
                }
            }
            booking.setBookingExcursionList(excursions);
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
%>
    <div class="span6 offset3">
        <%-- TODO: Betalingssysteem; valt de scope van de demo --%>
        <h2>Boeking aangemaakt.</h2>
        <p>Uw bestelling is aangemaakt. Outdoor Paradise wenst u een goede reis!</p>
    </div>
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
        e.printStackTrace();
        response.sendRedirect("booking.jsp");
    }
%>