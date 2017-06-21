/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global bubble_namespace, plan_namespace */

var home_namespace = {};

home_namespace.toHomePage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", true);
    $("#logout").toggleClass("col-xs-3", false);
    $(".container").hide();
    $("#home").show();
    home_namespace.getHomeEvents();
};

home_namespace.getHomeEvents = function(){
  $.ajax({
        async:false,
        url : "user/event",
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            home_namespace.generateHomeContent(result.complexData);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        }
    });    
};

home_namespace.generateHomeContent = function(events){
    var homeContent = $("#homeContent").empty();
    if(events.length > 0){
        for(var index in events){
            if(events.hasOwnProperty(index)){
                var event = events[index];
                var table = home_namespace.createHome(event);
                homeContent.append(table);
            }
        }
    }else{
        homeContent.append("<h3>No events found</h3>");
    }
};

home_namespace.createHome = function(eventList){
    var div = $("<div class=\"homeTable\"></div>");
    for(var index = 0; index<eventList.length; index++){
        if(eventList.hasOwnProperty(index)){
            var event = eventList[index];
            var table = $("<table class=\"table-" + index + "\"></table>").prop("itemprop", index);
            var leftB = $("<td></td>").append(" < ").addClass("btn").addClass("tableButton");
            if(index > 0){
                leftB.click(function(){
                    var divParent = $(this).parents(".homeTable");
                    var tableParent = $(this).parents("table");
                    var index = parseInt(tableParent.prop("itemprop"));
                    tableParent.hide();
                    index = index-1;
                    divParent.children(".table-"+index).show();
                });
            }
            var rightB = $("<td></td>").append(" > ").addClass("btn").addClass("tableButton");
            rightB.index = index;
            if(index < eventList.length-1){
                rightB.click(function(){
                    var divParent = $(this).parents(".homeTable");
                    var tableParent = $(this).parents("table");
                    var index = parseInt(tableParent.prop("itemprop"));
                    tableParent.hide();
                    index = index+1;
                    divParent.children(".table-"+index).show();
                });
            }
            var nrOrdine = index+1;
            var name = event.name + " (" + nrOrdine + "/" + eventList.length + ")";
            var td = $("<td></td>").append($("<h5></h5>").append(name));
            var addToPlan = $("<h2></h2>").append("+");
            addToPlan.prop("event_id", event.id);
            addToPlan.click(function(){
                plan_namespace.addEventToPlan($(this).prop("event_id"));
            });
            var tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>").append(addToPlan).toggleClass("specialButton",true));
            table.append(tr);
            var img = $("<img></img>");
            img.attr("src", event.imageUrl);
            td = $("<td></td>").append(img);
            tr = $("<tr></tr>").append(leftB).append(td).append(rightB);
            table.append(tr);
            td = $("<td></td>").append($("<p></p>").append(event.address));
            tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
            table.append(tr);
            td = $("<td></td>").append($("<p></p>").append(event.type));
            tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
            table.append(tr);
            var date = new Date(event.date);
            td = $("<td></td>").append($("<time></time>").append(date.toLocaleString()));
            tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
            table.append(tr);
            var a = $("<a>LINK</a>");
            a.prop("href", event.link);
            a.prop("target","_blank");
            td = $("<td></td>").append(a);
            tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
            table.append(tr);
            td = $("<td></td>").append($("<p></p>").append(event.description));
            tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
            table.append(tr);
            td = $("<td></td>").append($("<h5></h5>").append(event.hostSite));
            tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
            table.append(tr);
            div.append(table);
        }
    }
    div.children("table").hide();
    div.children("table").first().show();
    return div;
};