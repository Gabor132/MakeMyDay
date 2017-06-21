/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global bubble_namespace, event_types_namespace */

var preferences_namespace = {};

preferences_namespace.userPreferences = [];

preferences_namespace.toPreferencePage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", false);
    $("#logout").toggleClass("col-xs-3", true);
    $(".container").hide();
    $("#preferences").show();
    preferences_namespace.getPreferences();
    preferences_namespace.createPreference();
    $("#preferencesButton").show();
};

preferences_namespace.getPreferences = function(){
    $.ajax({
        async:false,
        url : "user/preferences",
        type:'GET',
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            preferences_namespace.userPreferences = result.data;
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        }
    });
};

preferences_namespace.savePreferences = function(){
    var event_types = event_types_namespace.types;
    var preferences = [];
    var i = 0;
    for(var index in event_types){
        var id = "#preferences"+event_types[index].replaceAll(" ","_");
        var input = $(id);
        if(input[0].checked === true){
            preferences[i] = { type: event_types[index] };
            i++;
        }
    }
    preferences_namespace.setPreferences(preferences);
};

preferences_namespace.setPreferences = function(preferences){
    $.ajax({
        async:false,
        url : "user/preferences",
        type:'PUT',
        contentType: "application/json; charset=utf-8",
        data:JSON.stringify(preferences),
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        }
    });
};

preferences_namespace.createPreference = function(){
    var event_types = event_types_namespace.types;
    var form = $("#preferences_form");
    form.empty();
    for(var index in event_types){
        var type = event_types[index];
        var div = $("<div></div>").toggleClass("checkbox");
        var input = $("<input></input>").prop("type", "checkbox").prop("id","preferences"+(type).replaceAll(" ","_"));
        var label = $("<label></label>").toggleClass("control-label", true).prop("for","preferences"+(type).replaceAll(" ","_")).append(input).append(type+":");
        div.append(label);
        form.append(div).append("<br/>");
    }
    
    for(var index in preferences_namespace.userPreferences){
        var id = "#preferences"+preferences_namespace.userPreferences[index].type;
        id = id.replaceAll(" ","_");
        var input = $(id);
        input[0].checked = true;
    }
};