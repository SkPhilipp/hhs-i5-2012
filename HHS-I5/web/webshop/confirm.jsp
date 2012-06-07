<%@page import="java.util.Map.Entry"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="template-header.html"/>
<%--
    TODO:
    --- webshop/confirm.jsp ---
    Actor bevestigt op overzicht van geselecteerde items
    Systeem maakt boeking aan
    Actor geeft per excursie aan hoeveel personen er mee gaan en bevestigt
    Als dit meer personen zijn dan is toegestaan komt er een foutmelding
--%>
<%
    for(Entry<String, String[]> entry : request.getParameterMap().entrySet()){
%>
    <h5>Key: <%= entry.getKey() %></h5>
    <% for(String s : entry.getValue()) { %>
        <p>Value: <%= s %></p>
    <% } %>
<%
    }
%>
<jsp:include page="template-footer.html"/>