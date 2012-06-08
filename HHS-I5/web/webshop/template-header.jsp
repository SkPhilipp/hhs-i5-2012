<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Outdoor Paradise</title>
	<meta name="viewport" content="width=device-width">
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<style>
		body{
			padding-top: 60px;
                        background-image: url("img/logoOPStar.png");
                        background-position: 0px -50px;
                        background-repeat: no-repeat;
		}
	</style>
	<link rel="stylesheet" href="css/bootstrap-responsive.min.css">
    </head>
    <body>
	<div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="brand" href="/HHS-I5/" style="margin:0">
              <img src="img/logoOPSquare.png" style="width:40px; height:40px;"/>
          </a>
            <div class="subnav" style="margin-top:8px;">
                <ul class="nav nav-pills">
                    <li class="active"><a href="booking.jsp">Reizen Boeken</a></li>
                    <li><a href="#">Mijn Account ( Klant 1 )</a></li>
                </ul>
            </div>
        </div>
      </div>
    </div>
        <div class="container" style="padding:5px;">
            <div class="row">
                    <%
                        String msgType = request.getParameter("msg");
                        if(session.getAttribute("alert") != null){
                    %>
                <div class="span8 offset2">
                    <div class="alert alert-error">
                        <h4 class="alert-heading">Helaas..</h4>
                        <%= session.getAttribute("alert") %>
                    </div>
                </div>
            </div>
            <div class="row">
                    <%
                            session.removeAttribute("alert");
                        }
                    %>