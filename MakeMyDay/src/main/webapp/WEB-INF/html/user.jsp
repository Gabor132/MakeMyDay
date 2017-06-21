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
        <link href="resources/css/user.css" type="text/css" rel="stylesheet">
        <link href="resources/css/table.css" type="text/css" rel="stylesheet">
        <script src="resources/js/main.js" type="text/javascript"></script>
        <script src="resources/js/requests/events.js" type="text/javascript"></script>
        <script src="resources/js/requests/eventTypes.js" type="text/javascript"></script>
        <script src="resources/js/filterPage.js" type="text/javascript"></script>
        <script src="resources/js/preferencesPage.js" type="text/javascript"></script>
        <script src="resources/js/homePage.js" type="text/javascript"></script>
        <script src="resources/js/planPage.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="navbar navbar-default row">
                <h1 id="title">Make My Day</h1><br/>
                <div class="buttonNav lrButton col-xs-3 btn" onclick="home_namespace.toHomePage()">
                    <h3>Home</h3>
                </div>
                <div class="buttonNav lrButton col-xs-3 btn" onclick="filter_namespace.toFilterPage()">
                    <h3>Filter</h3>
                </div>
                <div class="buttonNav lrButton col-xs-3 btn" onclick="preferences_namespace.toPreferencePage()">
                    <h3>Preferences</h3>
                </div>
                <div class="buttonNav lrButton col-xs-3 btn" onclick="plan_namespace.toPlanPage()">
                    <h3>Plans</h3>
                </div>
            </div>
            <div class="row content">
                <div id="home" class="container">
                    <div class="homeEvents">
                        <div>
                            <h3>We recommend you: </h3>
                        </div>
                        <div id="homeContent">
                            
                        </div>
                    </div>
                </div>
                <div id="filter" class="container" style="display:none">
                    <div class="row filter-form">
                        <div>
                            <h3>When do you want to have fun?</h3>
                        </div>
                        <form class="form-group">
                            <label class="control-label" for="inputDay">
                                Day: 
                            </label>
                            <input id="inputDay" type="date" value="8/1/2016"><br/>
                            <label class="control-label" for="inputHourS">
                                From:  
                            </label>
                            <select id="inputHourS">
                            </select>
                            <label class="control-label" for="inputHourF">
                                to  
                            </label>
                            <select id="inputHourF">
                            </select><br/>
                            <label class="control-label" for="inputType">
                                Event type:
                            </label>
                            <select id="inputType">
                                <option value="ANY">Any</option>
                            </select>
                        </form>
                    </div>
                    <div id="filterContent">
                        
                    </div>
                </div>
                <div id="preferences" class="container" style="display:none">
                    <div>
                        <h3>Here you can set your preferences</h3>
                    </div>
                    <form id="preferences_form" class="form-group">
                    </form>
                </div>
                <div id="plans" class="container"  style="display:none">
                        <div>
                            <h3>Your plans: </h3>
                        </div>
                        <div id="planContent">
                            
                        </div>
                </div>
            </div>
            
            <div class="navbar navbar-default navbar-fixed-bottom">
                <div class="alert messageBubble" style="display:none"></div>
                <div id="filterButton" class="buttonNav lrButton col-xs-9 btn" onclick="filter_namespace.findEvents()" style="display:none">
                    <h3>Find Events</h3>
                </div>
                <div id="preferencesButton" class="buttonNav lrButton col-xs-9 btn" onclick="preferences_namespace.savePreferences()" style="display:none">
                    <h3>Change preferences</h3>
                </div>
                <div id="logout" class="buttonNav lrButton col-xs-12 btn" onclick="lr_namespace.logout()">
                    <h3>Logout</h3>
                </div>
            </div>
    </body>
    <script type="text/javascript">
        bubble_namespace.init();
        event_types_namespace.getEventTypes();
        filter_namespace.init();
    </script>
</html>
