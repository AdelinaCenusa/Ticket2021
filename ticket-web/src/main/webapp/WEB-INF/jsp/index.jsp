<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <c:if test="${empty uuid}">
                    <form method="post">
                        <button type="submit" formaction="register">Register Ticket Machine</button>
                    </form>
                </c:if>
                <c:if test="${not empty uuid}">
                    <nav id="ticket-machine-menu">
                        <menu>
                            <li class="ticket-button"><a href="configure">Reconfigure Ticket Machine</a></li>
                            <li class="ticket-button"><a href="sales">Start Sales Kiosk</a></li>
                            <li class="gate-button"><a href="gate">Gate Machine</a></li>
                        </menu>
                    </nav>
                </c:if>
            </div>

            <%@ include file="common/footer.jsp" %>
        </div>
    </body>
</html>
