
/* global bubble_namespace, Storage */

var lr_namespace = {};

lr_namespace.userIsAdmin = false;

lr_namespace.switchLR = function(forLogin){
    if(forLogin){
        $(".rDiv").hide();
        $(".lDiv").show();
    }else{
        $(".rDiv").show();
        $(".lDiv").hide();
    }
};

lr_namespace.login = function(){
    var email = $("#lEmailInput").val();
    var password = $("#lPassInput").val();
    
    if(email.empty || password.empty){
        bubble_namespace.showValidationMessage("Email and password are obligatory");
        return;
    }
    
    var hashedPass = lr_namespace.hashFunction(password);
    
    lr_namespace.loginUser(email,hashedPass);
};

lr_namespace.logout = function(){
    lr_namespace.logoutUser();
};

lr_namespace.register = function(){
    var email = $("#rEmailInput").val();
    var password = $("#rPassInput").val();
    var rpassword = $("#rRepPassInput").val();
    
    if(password !== rpassword){
        bubble_namespace.showValidationMessage("Please write the same password in both fields");
        return;
    }
    
    var hashedPass = lr_namespace.hashFunction(password);
    
    lr_namespace.registerUser(email,hashedPass);
    
};

lr_namespace.hashFunction = function(pass){
    return pass;
};

lr_namespace.loginUser = function(email, password){
    var data = {
        email:email,
        password:password
    };
    $.ajax({
        url : "login/login",
        type : "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            if(result.succes){
                lr_namespace.saveToken(result.data[0]);
                lr_namespace.setupAjaxHeader(result.data[0].email, result.data[0].token);
                lr_namespace.manageRedirect();
            }
        },fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        }
    });
};

lr_namespace.registerUser = function(email, password){
    var data = {
        email:email,
        password:password
    };
    $.ajax({
        url : "login/register",
        type : "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            if(result.succes){
                lr_namespace.switchLR(true);
            }
        },fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        }
    });
};

lr_namespace.logoutUser = function(){
    var emailToken = lr_namespace.loadToken();
    if(emailToken === undefined){
            lr_namespace.manageRedirect();
    }
    var data = {
        email:emailToken.email,
        token:emailToken.token
    };
    $.ajax({
        url : "login/logout",
        type : "PUT",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            lr_namespace.deleteToken();
            lr_namespace.clearAjaxHeader();
            lr_namespace.redirect("/login", 2000);
        },fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        }
    });
};

//This function saves the email and token from the server to the browser session
lr_namespace.saveToken = function(email_token){
    if(typeof(Storage) !== undefined){
        sessionStorage.setItem("email", email_token.email);
        sessionStorage.setItem("token", email_token.token);
    }else{
        bubble_namespace.create_message(-1, "Browser-ul dumneavoastra nu suporta stocare de sesiune", bubble_namespace.IS_WARNING);
    }
};

//This function deletes the email and token from the browser session
lr_namespace.deleteToken = function(){
    if(typeof(Storage) !== undefined){
        sessionStorage.clear();
    }else{
        bubble_namespace.create_message(-1, "Browser-ul dumneavoastra nu suporta stocare de sesiune", bubble_namespace.IS_WARNING);
    }
};

//This function loads the email and token from the browser session
lr_namespace.loadToken = function(){
    if(typeof(Storage) !== undefined){
        if(sessionStorage.getItem("email") !== null && sessionStorage.getItem("token") !== null) {
            var email_token = {
                email: sessionStorage.getItem("email"),
                token: sessionStorage.getItem("token")
            };
            lr_namespace.setupAjaxHeader(email_token.email, email_token.token);
            return email_token;
        }else{
            lr_namespace.clearAjaxHeader();
            return undefined;
        }
    }else{
        bubble_namespace.create_message(-1, "Browser-ul dumneavoastra nu suporta stocare de sesiune", bubble_namespace.IS_WARNING);
        return undefined;
    }
};

//This function attaches the email and token from the browser session to the data that will be sent in the request
lr_namespace.attachToken = function(data){
    var email_token = lr_namespace.loadToken();
    if(email_token !== undefined){
        data.email = email_token.email;
        data.token = email_token.token;
    }
    return data;
};

lr_namespace.setupAjaxHeader = function(email, token){
    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader("Auth-Token", token);
            xhr.setRequestHeader("Auth-Email", email);
        }
    });
};

lr_namespace.clearAjaxHeader = function(){
    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            xhr.setRequestHeader("Auth-Token", undefined);
            xhr.setRequestHeader("Auth-Email", undefined);
        }
    });
};

lr_namespace.redirect = function(url, time){
    setTimeout(function(){
        window.location = window.location.origin + url;
    }, time);
};

lr_namespace.isLoggedIn = function(){
    return lr_namespace.loadToken() !== undefined;
};

lr_namespace.isAdmin = function(){
    var emailToken = lr_namespace.loadToken();
    if(emailToken === undefined){
        return;
    }
    $.ajax({
        async: false,
        url : "login/isAdmin",
        type : "GET",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            if(result.succes){
                lr_namespace.userIsAdmin = true;
            }else{
                lr_namespace.userIsAdmin = false;
            }
        },fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        }
    });
};

lr_namespace.manageRedirect = function(){
    lr_namespace.isAdmin();
    if(!lr_namespace.isLoggedIn() && !lr_namespace.isAtLogin()){
        lr_namespace.redirect("/login", 0);
    }
    if(lr_namespace.userIsAdmin && !lr_namespace.isAtAdmin()){
        lr_namespace.redirect("/admin", 0);
    }
    if(lr_namespace.isLoggedIn() && !lr_namespace.userIsAdmin && !lr_namespace.isAtUser()){
        lr_namespace.redirect("/user", 0);
    }
};

lr_namespace.isAtLogin = function(){
    var href = window.location.href;
    return href.split("/login")[0] !== href;
};

lr_namespace.isAtAdmin = function(){
    var href = window.location.href;
   return href.split("/admin")[0] !== href;
};

lr_namespace.isAtUser = function(){
    var href = window.location.href;
    return href.split("/user")[0] !== href;
};

lr_namespace.manageRedirect();