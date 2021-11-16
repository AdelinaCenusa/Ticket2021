<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Ticket Machine</title>
        <meta charset="UTF-8">
        <meta name="description" content="Ticket Machine">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="resources/css/ticket-core.css">
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="common/header.jsp" %>

            <div id="main-content">
                <h2>Ticket - Checkout</h2>
                <form method="post">
                    <p id="start-station">
                        <label class="col-label">From:</label>
                        <label class="col-value">${ticket.startStation.name} - Zone ${ticket.startStation.zone}</label>
                        <input type="hidden" name="startStation" value="${ticket.startStation.name}">
                    </p>
                    <p id="dest-station">
                        <label class="col-label">To:</label>
                        <label class="col-value">${ticket.destStation.name} - Zone ${ticket.destStation.zone}</label>
                        <input type="hidden" name="destStation" value="${ticket.destStation.name}">
                    </p>
                    <p id="sales-timestamp">
                        <label class="col-label">Valid From:</label>
                        <label class="col-value">${ticket.validFrom} - (${ticket.rateBand})</label>
                        <input type="hidden" name="issueDate" value="${ticket.validFrom}">
                    </p>
                    <p id="expiry-timestamp">
                        <label class="col-label">Valid To:</label>
                        <label class="col-value">${ticket.validTo}</label>
                    </p>
                    <p id="journey-price">
                        <label class="col-label">Ticket Price:</label>
                        <label class="col-value"><fmt:formatNumber value="${ticket.price}" type="currency" currencySymbol="£" /></label>
                    </p>
                    <div id="row-buttons">
                        <button class="sale-button" formaction="buyTicket?cardValid=true" >Buy - Using Valid Card</button>
                        <button class="sale-button" formaction="buyTicket?cardValid=false">Buy - Using Invalid Card</button>
                        <button class="sale-button" formmethod="get" formaction="sales">Cancel</button>
                    </div>
                </form>
            </div>

            <%@ include file="common/footer.jsp" %>
        </div>
    </body>
</html>
