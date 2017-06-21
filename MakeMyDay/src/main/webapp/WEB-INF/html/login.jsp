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
        <script src="resources/js/loginAndRegister.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="navbar navbar-default navbar-fixed-top">
                <h1 id="title">Make My Day</h1>
            </div>
            <div class="lDiv lrDiv row">
                <div>
                    <h3>Login</h3>
                </div>
                <form class="lrForm form-group">
                    <label class="control-label" for="lEmailInput">
                        Email
                    </label>
                    <input type="text" id="lEmailInput" value="gabordragos@gmail.com"><br/>
                    <label class="control-label" for="lPassInput">
                        Password
                    </label>
                    <input type="password" id="lPassInput" value="muci"><br/>
                </form>
            </div>
            <div class="rDiv lrDiv row" style="display:none">
                <div>
                    <h3>Register</h3>
                </div>
                <form class="lrForm form-group">
                    <label class="control-label" for="rEmailInput">
                        Email
                    </label>
                    <input type="text" id="rEmailInput" value="gabordragos@gmail.com"><br/>
                    <label class="control-label" for="rPassInput">
                        Password
                    </label>
                    <input type="password" id="rPassInput" value="muci"><br/>
                    <label class="control-label" for="rRepPassInput">
                        Retype Password
                    </label>
                    <input type="password" id="rRepPassInput" value="muci"><br/>
                </form>
            </div>
            <div class="navbar navbar-default navbar-fixed-bottom ">
                <div class="alert messageBubble" style="display:none"></div>
                <div class="lDiv">
                    <div class="lrButton col-xs-6 btn" onclick="lr_namespace.login()">
                        <h3>Login</h3>
                    </div>
                    <div class="lrButton col-xs-6 btn" onclick="lr_namespace.switchLR(false)">
                        <h3>No account? Register</h3>
                    </div>
                </div>
                <div class="rDiv" style="display:none">
                    <div class="lrButton col-xs-6 btn" onclick="lr_namespace.register()">
                        <h3>Register</h3>
                    </div>
                    <div class="lrButton col-xs-6 btn" onclick="lr_namespace.switchLR(true)">
                        <h3>Already have an account? Login</h3>
                    </div>
                </div>
            </div>
            
        </div>
        <script type="text/javascript">
            bubble_namespace.init();
        </script>
    </body>
</html>