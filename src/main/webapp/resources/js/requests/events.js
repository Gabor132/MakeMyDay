/* global bubble_namespace */

var event_namespace = {};

event_namespace.toEventPage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", true);
    $("#logout").toggleClass("col-xs-4", false);
    $(".container").empty().hide();
    $("#events").show();
    event_namespace.getEvents();
};

event_namespace.headerEventsArray = {0:"ID", 1:"NAME", 2:"IMAGE", 3:"ADDRESS", 4:"TYPE", 5:"DATE", 6:"LINK", 7:"DESCRIPTION",
    8:"HOST SITE", 9:"EDIT BUTTONS"};

event_namespace.getEvents = function(){
    $.ajax({
        url : "admin/event",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            generateTable("#events", result.data, event_namespace.headerEventsArray, event_namespace.Event, false, true);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        },
        dataType: "json"
    });
};

event_namespace.Event = function(jsonObject, hasEdit, hasDelete){
    var div = $("<tr></tr>");
    div.append($("<td></td>").append(jsonObject.id));
    div.append($("<td></td>").append(jsonObject.name));
    var img = $("<img></img>");
    img.attr("src", jsonObject.imageUrl);
    div.append($("<td></td>").append(img));
    div.append($("<td></td>").append(jsonObject.address));
    div.append($("<td></td>").append(jsonObject.type));
    var date = new Date(jsonObject.date);
    div.append($("<td></td>").append(date.toLocaleString()));
    var a = $("<a>LINK</a>");
    a.prop("href", jsonObject.link);
    a.prop("target","_blank");
    div.append($("<td></td>").append(a));
    div.append($("<td></td>").append(jsonObject.description));
    div.append($("<td></td>").append(jsonObject.hostSite));
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
            event_namespace.deleteEvent($(this).attr("elementId"));
        });
        div.append(buttonEdit);
    }
    return div;
};

event_namespace.deleteEvent = function(id){
    $.ajax({
        url : "admin/event",
        type : 'DELETE',
        contentType: "application/json; charset=utf-8",
        data : id,
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            event_namespace.toEventPage();
        }
    });
};