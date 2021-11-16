<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Underground Controller</title>
        <meta charset="UTF-8">
        <meta name="description" content="Controller to manage underground resources">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="resources/css/underground-core.css">
        <link rel="stylesheet" type="text/css" href="resources/css/underground-main.css">
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="common/header.jsp" %>

            <div id="main-content">
                <nav id="lunderground-controller-menu">
                    <menu>
                        <li class="controller-button"><a href="manage-stations">Manage Stations</a></li>
                        <li><hr></li>
                        <li class="init-stations"><a href="init">Initialise</a></li>
                    </menu>
                </nav>
            </div>
        </div>
    </body>
</html>
