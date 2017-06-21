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
        <link href="resources/css/main.css" type="text/css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid">
            <div class="navbar navbar-default navbar-fixed-top">
                <div class="alert messageBubble" style="display:none"></div>
                <h1 id="title">Make My Day</h1>
            </div>
            <div class="navbar navbar-default navbar-fixed-bottom">
                <div class="messageBubble" style="display:none"></div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        bubble_namespace.init();
    </script>
</html>
