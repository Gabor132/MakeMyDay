
/* global bubble_namespace, event_types_namespace, plan_namespace */

var filter_namespace = {};

filter_namespace.toFilterPage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", false);
    $("#logout").toggleClass("col-xs-3", true);
    $(".container").hide();
    $("#filter").show();
    $("#filterButton").show();
};

filter_namespace.init = function(){
    $("#inputHourS, #inputHourF").each(function(){
        for(var i = 0; i < 24; i++){
            var option = $("<option></option>").toggleClass("hourOption").val(i).text(" " + i + " ");
            $(this).append(option);
        }
    });
    $("#inputHourS, #inputHourF").change(function(){
        filter_namespace.hourCheck();
    });
    
    for(var index in event_types_namespace.types){
        $("#inputType").append($("<option></option>").text(event_types_namespace.types[index]).val(event_types_namespace.types[index]));
    }
};

filter_namespace.hourCheck = function(){
    var from = $("#inputHourS").val();
    var to = $("#inputHourF").val();
    if(from > to){
        $("#inputHourS").toggleClass("fail", true);
    }else{
        $("#inputHourS").toggleClass("fail", false);
    }
};

filter_namespace.findEvents = function(){
    var day = $("#inputDay").val();
    var year = day.split("/")[2];
    var month = day.split("/")[1];
    day = day.split("/")[0];
    var start = $("#inputHourS").val();
    var finish = $("#inputHourF").val();
    var type = $("#inputType").val();
    var data = {
        day:day,
        month:month,
        year:year,
        start:start,
        finish:finish,
        type:type
    };
    $.ajax({
        url : "user/events/filter",
        type : "PUT",
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            if(result.succes){
                filter_namespace.generateFilterContent(result.data);
            }
        },fail: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        },
        data: JSON.stringify(data)
    });
};

filter_namespace.generateFilterContent = function(filteredEvents){
    var filterContent = $("#filterContent").empty();
    if(filteredEvents.length > 0){
        for(var index in filteredEvents){
            if(filteredEvents.hasOwnProperty(index)){
                var filteredEvent = filteredEvents[index];
                var table = filter_namespace.createEvent(filteredEvent);
                filterContent.append(table);
            }
        }
    }else{
        filterContent.append("<h3>No events found</h3>");
    }
};

filter_namespace.createEvent = function(event){
    var div = $("<div class=\"planTable\"></div>");
    var table = $("<table></table>");
    var name = event.name;
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
    tr = $("<tr></tr>").append($("<td></td>")).append(td).append($("<td></td>"));
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
    div.children("table").hide();
    div.children("table").first().show();
    return div;
};
