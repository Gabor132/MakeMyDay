
/* global bubble_namespace, lr_namespace */

var user_namespace = {};

user_namespace.userEmail;

user_namespace.toUserPage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", true);
    $("#logout").toggleClass("col-xs-4", false);
    $(".container").empty().hide();
    $("#users").show();
    user_namespace.getUsers();
};

user_namespace.headerUserArray = {0:"ID", 1:"EMAIL", 2:"HAS PLANS", 3:"TYPE", 4:"EDIT BUTTONS"};

user_namespace.getUsers = function(){
    $.ajax({
        url : "admin/user",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            generateTable("#users", result.data, user_namespace.headerUserArray, user_namespace.User, false, true);
        }
    });
};

user_namespace.User = function(jsonObject, hasEdit, hasDelete){
    var div = $("<tr></tr>");
    div.append($("<td></td>").append(jsonObject.id));
    div.append($("<td></td>").append(jsonObject.email));
    var hasPlans = "NO";
    if(jsonObject.plans.length > 0){
        hasPlans = "YES";
    }
    div.append($("<td></td>").append(hasPlans));
    div.append($("<td></td>").append(jsonObject.type));
    
    if(user_namespace.userEmail === undefined){
        var emailToken = lr_namespace.loadToken();
        if(emailToken === undefined){
            lr_namespace.logout();
        }else{
            user_namespace.userEmail = emailToken.email;
        }
    }
    var loggedInEmail = user_namespace.userEmail;
    if(loggedInEmail !== jsonObject.email){
        if(hasEdit){
            var buttonEdit = $("<td></td>").append("Edit");
            buttonEdit.addClass("tableButton");
            buttonEdit.addClass("btn");
            buttonEdit.attr("elementId", jsonObject.id);
            buttonEdit.click(function(){
                alert("Redirect to "+$(this).attr("elementId"));
            });
            div.append(buttonEdit);
        }
        if(hasDelete){
            var buttonEdit = $("<td></td>").append("Delete");
            buttonEdit.addClass("tableButton");
            buttonEdit.addClass("btn");
            buttonEdit.attr("elementId", jsonObject.id);
            buttonEdit.click(function(){
                user_namespace.deleteUser($(this).attr("elementId"));
            });
            div.append(buttonEdit);
        }
    }else{
        div.append("<td></td>");
    }
    return div;
};

user_namespace.deleteUser = function(id){
    $.ajax({
        url : "admin/user",
        type : 'DELETE',
        contentType: "application/json; charset=utf-8",
        data : id,
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            user_namespace.toUserPage();
        }
    });
};