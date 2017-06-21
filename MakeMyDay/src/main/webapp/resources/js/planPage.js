
/* global bubble_namespace */

var plan_namespace = {};

plan_namespace.headerPlanArray = {0:"ID", 1:"NAME", 2:"TYPE", 3:"LINK", 4:"EDIT BUTTONS"};

plan_namespace.toPlanPage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", true);
    $("#logout").toggleClass("col-xs-3", false);
    $(".container").hide();
    $("#plans").show();
    plan_namespace.getPlans();
};

plan_namespace.getPlans = function(){
    $.ajax({
        url : "user/plan",
        type : 'GET',
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            plan_namespace.generatePlanContent(result.data);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        },
        dataType: "json"
    });
};

plan_namespace.generatePlanContent = function(plans){
    var planContent = $("#planContent").empty();
    if(plans.length > 0){
        for(var index in plans){
            if(plans.hasOwnProperty(index)){
                var plan = plans[index];
                var table = plan_namespace.createPlan(plan);
                planContent.append(table);
            }
        }
    }else{
        planContent.append("<h3>No plan found</h3>");
    }
};

plan_namespace.createPlan = function(plan){
    var eventList = plan.events;
    var div = $("<div class=\"planTable\"></div>");
    for(var index = 0; index<eventList.length; index++){
        if(eventList.hasOwnProperty(index)){
            var event = eventList[index];
            var table = $("<table class=\"table-" + index + "\"></table>").prop("itemprop", index);
            var leftB = $("<td></td>").append(" < ").addClass("btn").addClass("tableButton");
            if(index > 0){
                leftB.click(function(){
                    var divParent = $(this).parents(".planTable");
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
                    var divParent = $(this).parents(".planTable");
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
            var deleteFromPlan = $("<h2></h2>").append("-");
            deleteFromPlan.prop("event_id", event.id);
            deleteFromPlan.click(function(){
                plan_namespace.deleteEventFromPlan($(this).prop("event_id"));
            });
            var tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>").append(deleteFromPlan).toggleClass("specialButton",true));
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

plan_namespace.addEventToPlan = function(id){
    $.ajax({
        url : "user/plan/"+parseInt(id),
        type : 'PUT',
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        },
        dataType: "json"
    });
};

plan_namespace.deleteEventFromPlan = function(id){
    $.ajax({
        url : "user/plan/"+parseInt(id),
        type : 'DELETE',
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            plan_namespace.toPlanPage();
        },
        fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        },
        dataType: "json"
    });
};