<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>MakeMyDay</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="resources/js/jquery-3.1.0.min.js" type="text/javascript"></script>
        <link href="resources/css/bubble.css" type="text/css" rel="stylesheet">
        <script src="resources/js/bubble.js" type="text/javascript"></script>
        <link href="resources/css/loginAndRegister.css" type="text/css" rel="stylesheet">
        <script src="resources/js/loginAndRegister.js" type="text/javascript"></script>
        <script src="resources/js/bootstrap.min.js" type="text/javascript"></script>
        <link href="resources/css/bootstrap.min.css" type="text/css" rel="stylesheet">
        <link href="resources/css/bootstrap.min.css.map" type="text/css" rel="stylesheet">
        <link href="resources/css/main.css" type="text/css" rel="stylesheet">
        <link href="resources/css/form.css" type="text/css" rel="stylesheet">
        <link href="resources/css/table.css" type="text/css" rel="stylesheet">
        <link href="resources/css/popup.css" type="text/css" rel="stylesheet">
        <script src="resources/js/main.js" type="text/javascript"></script>
        <script src="resources/js/requests/events.js" type="text/javascript"></script>
        <script src="resources/js/requests/users.js" type="text/javascript"></script>
        <script src="resources/js/requests/sites.js" type="text/javascript"></script>
        <script src="resources/js/requests/eventTypes.js" type="text/javascript"></script>
        <script src="resources/js/entities/templateTreeCommonFunctions.js" type="text/javascript"></script>
        <script src="resources/js/entities/templateTreeRoot.js" type="text/javascript"></script>
        <script src="resources/js/entities/templateTreeElement.js" type="text/javascript"></script>
        <script src="resources/js/table.js" type="text/javascript"></script>
        <script src="resources/js/main.js" type="text/javascript"></script>
        <script src="resources/js/cursor.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="navbar navbar-default row">
                <h1 id="title">Make My Day</h1><br/>
                <div class="buttonNav lrButton col-xs-4 btn" onclick="user_namespace.toUserPage()">
                    <h3>Users</h3>
                </div>
                <div class="buttonNav lrButton col-xs-4 btn" onclick="sites_namespace.toSitePage()">
                    <h3>Sites</h3>
                </div>
                <div class="buttonNav lrButton col-xs-4 btn" onclick="event_namespace.toEventPage()">
                    <h3>Events</h3>
                </div>
            </div>
            <div class="row content">
                <div id="users" class="container">
                </div>
                <div id="sites" class="container">
                </div>
                <div id="events" class="container">
                </div>
            </div>
            <div class="navbar navbar-default navbar-fixed-bottom">
                <div class="alert messageBubble" style="display:none"></div>
                <div id = "createSite" class="buttonNav lrButton col-xs-4 btn" onclick="sites_namespace.toSiteCreate()" style="display: none">
                    <h3>Create site</h3>
                </div>
                <div id = "collectSites" class="buttonNav lrButton col-xs-4 btn" onclick="sites_namespace.collectSites()" style="display: none">
                    <h3>Collect events</h3>
                </div>
                <div id = "saveSite" class="buttonNav col-xs-4 btn btn-success" onclick="sites_namespace.saveSite()" style="display: none">
                    <h3>Save site</h3>
                </div>
                <div id = "cancelSite" class="buttonNav col-xs-4 btn btn-danger" onclick="sites_namespace.cancelSite()" style="display: none">
                    <h3>Cancel site</h3>
                </div>
                <div id="logout" class="buttonNav lrButton col-xs-12 btn" onclick="lr_namespace.logout()">
                    <h3>Logout</h3>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        bubble_namespace.init();
        event_types_namespace.getEventTypes();
    </script>
</html>
