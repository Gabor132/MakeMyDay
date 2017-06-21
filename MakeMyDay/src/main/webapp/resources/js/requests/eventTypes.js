/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global bubble_namespace */

var event_types_namespace = {};

event_types_namespace.types = [];

event_types_namespace.getEventTypes = function(){
    $.ajax({
        async:false,
        url : "references",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            for(var index in result.data){
                if(result.data.hasOwnProperty(index)){
                    event_types_namespace.types[index] = result.data[index].type;
                }
            }
        },
        dataType: "json"
    });
};