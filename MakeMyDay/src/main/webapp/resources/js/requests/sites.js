
/* global headerEventsArray, Event, bubble_namespace, event_namespace, event_types_namespace, cursor_namespace */


var sites_namespace = {};

sites_namespace.last_result;
sites_namespace.selected_for_edit = -1;

sites_namespace.toSitePage = function(){
    $(".navbar-fixed-bottom > .buttonNav").hide();
    $("#logout").show();
    $("#logout").toggleClass("col-xs-12", false);
    $("#logout").toggleClass("col-xs-4", true);
    $(".container").empty().hide();
    $("#sites").show();
    sites_namespace.getSites();
    $("#createSite").show();
    $("#collectSites").show();
};

sites_namespace.toSiteCreate = function(){
    $("#sites").empty();
    $("#createSite").hide();
    $("#collectSites").hide();
    $("#saveSite").show();
    $("#cancelSite").show();
    sites_namespace.selected_for_edit = -1;
    sites_namespace.createSite();
};

sites_namespace.toSiteEdit = function(siteId){
    $("#sites").empty();
    $("#createSite").hide();
    $("#collectSites").hide();
    $("#saveSite").show();
    $("#cancelSite").show();
    sites_namespace.selected_for_edit = siteId;
    sites_namespace.editSite(siteId);
};

sites_namespace.cancelSite = function(){
    sites_namespace.toSitePage();
};

sites_namespace.headerSiteArray = {0:"ID", 1:"NAME", 2:"CONTENT TYPE", 3:"LINK",  4:"EDIT BUTTONS"};

sites_namespace.templateRoot;

sites_namespace.getSites = function(){
    $.ajax({
        url : "admin/site",
        type:'GET',
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            sites_namespace.last_result = result.data;
            generateTable("#sites", result.data, sites_namespace.headerSiteArray, sites_namespace.Site);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        }
    });
};

sites_namespace.addSite = function(name, eventUrlType, link, contentType, dateFormat, itemTemplate){
    var data = {
        "id":sites_namespace.selected_for_edit,
        "name":name,
        "eventUrlType":eventUrlType,
        "link":link,
        "contentType":contentType,
        "dateFormat":dateFormat,
        "itemTemplate":itemTemplate,
        "events":null
    };
    
    $.ajax({
        async: "false",
        url : "admin/site",
        type:'POST',
        contentType: "application/json; charset=utf-8",
        data:JSON.stringify(data),
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            sites_namespace.toSitePage();
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        }
    });
};

sites_namespace.deleteSite = function(idSite){
    var data = {
        "id":idSite,
        "name":null,
        "link":null,
        "itemTemplate":null,
        "events":null
    };
    
    $.ajax({
        url : "admin/site",
        type:'DELETE',
        contentType: "application/json; charset=utf-8",
        data:JSON.stringify(data),
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            location.reload();
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
        }
    });
};

sites_namespace.testSite = function(idSite){
    var tabele = $("table");
    if(tabele.length > 1)
        tabele[1].remove();
    cursor_namespace.changeToLoad();
    bubble_namespace.showRequestMessage(100, true, 'Please wait till the parsing is over');
    $("#sites > table").hide();
    $.ajax({
        url : "admin/site/test/"+idSite,
        type:'GET',
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            cursor_namespace.changeToNormal();
            generateTable("#sites", result.data, event_namespace.headerEventsArray, event_namespace.Event, false);
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
            cursor_namespace.changeToNormal();
        }
    });
};

sites_namespace.Site = function(jsonObject, hasEdit){
    var div = $("<tr></tr>");
    div.append($("<td></td>").append(jsonObject.id));
    div.append($("<td></td>").append(jsonObject.name));
    div.append($("<td></td>").append(jsonObject.contentType));
    div.append($("<td></td>").append(jsonObject.link));
    
    if(hasEdit){
        var buttonEdit = $("<td></td>").append("Edit");
        var buttonDelete = $("<td></td>").append("Delete");
        var buttonTest = $("<td></td>").append("Test");
        buttonEdit.addClass("tableButton");
        buttonEdit.addClass("btn");
        buttonEdit.attr("elementId", jsonObject.id);
        buttonEdit.click(function(){
            sites_namespace.toSiteEdit($(this).attr("elementId"));
        });
        buttonDelete.addClass("tableButton");
        buttonDelete.addClass("btn");
        buttonDelete.attr("elementId", jsonObject.id);
        buttonDelete.click(function(){
            sites_namespace.deleteSite($(this).attr("elementId"));
        });
        buttonTest.addClass("tableButton");
        buttonTest.addClass("btn");
        buttonTest.attr("elementId", jsonObject.id);
        buttonTest.click(function(){
            sites_namespace.testSite($(this).attr("elementId"));
        });
        div.append(buttonEdit);
        div.append(buttonDelete);
        div.append(buttonTest);
    }
    return div;
};

sites_namespace.createSite = function(){
    $("#createSiteForm").remove();
    var contentType = $("<select id=\"contentType\" name=\"contentType\"></select>");
    for (var index in event_types_namespace.types){
        contentType.append($("<option></option>").text(event_types_namespace.types[index]).val(event_types_namespace.types[index]));
    }
    var form = $("<form id=\"createSiteForm\"></form>")
            .append("Site name: <input id=\"siteName\" type=\"text\" name=\"name\"></input><br/>")
            .append("Link: <input id=\"siteLink\" type=\"text\" name=\"link\"></input><br/>")
            .append("Date format: <input id=\"siteDateFormat\" type=\"text\" name=\"dateFormat\"></input><br/>")
            .append("Event url type: ")
            .append("<select id=\"eventUrlType\" name=\"eventUrlType\"><option value=\"SIMPLE\">SIMPLE</option><option value=\"CONCATENATION\">CONCATENATION</option></select><br/>")
            .append("Content Type: ")
            .append(contentType)
            .append("<div class=\"templateTree\"></div>");
    $("#sites").append(form);
    sites_namespace.initializeTemplateTree();
};

sites_namespace.initializeTemplateTree = function(existingTemplate){
    if(existingTemplate === undefined){
        var tree = $(".templateTree");
        sites_namespace.templateRoot = new TemplateRoot();
        tree.append(sites_namespace.templateRoot.template);
    }else{
        var tree = $(".templateTree");
        sites_namespace.templateRoot = new TemplateRoot(existingTemplate.name, existingTemplate.id);
        tree.append(sites_namespace.templateRoot.template);
        for(var index in existingTemplate.classes){
            sites_namespace._initializaTemplateTree(sites_namespace.templateRoot, existingTemplate.classes[index]);
        }
    }
};

sites_namespace._initializaTemplateTree = function(root, classTemplate){
    if(root !== undefined && classTemplate !== undefined){
        root.addChild(classTemplate.className, classTemplate.classType,
            classTemplate.valueLocation, classTemplate.valueType, classTemplate.id);
        for(var index in classTemplate.children){
            sites_namespace._initializaTemplateTree(classTemplate, classTemplate.children[index]);
        }
    }
};

sites_namespace.saveSite = function(){
    var name = $("#siteName");
    var link = $("#siteLink");
    var eventUrlType = $("#eventUrlType");
    var contentType = $("#contentType option:selected");
    var dateFormat = $("#siteDateFormat");
    var template = sites_namespace.templateRoot.getJSON();
    sites_namespace.addSite(name.val(), eventUrlType.val(), link.val(), contentType.val(), dateFormat.val(), template);
    $("#createSiteForm").remove();
};

sites_namespace.editSite = function(siteId){
    var templateForEdit;
    for(var index in sites_namespace.last_result){
        var possibleTemplate = sites_namespace.last_result[index];
        if(possibleTemplate.id === parseInt(siteId)){
            templateForEdit = possibleTemplate;
        }
    }
    if(templateForEdit === undefined || templateForEdit === null)
        return;
    
    $("#createSiteForm").remove();
    var contentType = $("<select id=\"contentType\" name=\"contentType\"></select>");
    for (var index in event_types_namespace.types){
        contentType.append($("<option></option>").text(event_types_namespace.types[index]).val(event_types_namespace.types[index]));
    }
    var form = $("<form id=\"createSiteForm\"></form>")
            .append("Site name: <input id=\"siteName\" type=\"text\" name=\"name\"></input><br/>")
            .append("Link: <input id=\"siteLink\" type=\"text\" name=\"link\"></input><br/>")
            .append("Date format: <input id=\"siteDateFormat\" type=\"text\" name=\"dateFormat\"></input><br/>")
            .append("Event url type: ")
            .append("<select id=\"eventUrlType\" name=\"eventUrlType\"><option value=\"SIMPLE\">SIMPLE</option><option value=\"CONCATENATION\">CONCATENATION</option></select><br/>")
            .append("Content Type: ")
            .append(contentType)
            .append("<div class=\"templateTree\"></div>");
    $("#sites").append(form);
    $("#siteName").val(templateForEdit.name);
    $("#siteType").val(templateForEdit.type);
    $("#siteLink").val(templateForEdit.link);
    $("#eventUrlType").val(templateForEdit.eventUrlType);
    $("#contentType").val(templateForEdit.contentType);
    $("#siteDateFormat").val(templateForEdit.dateFormat);
    sites_namespace.initializeTemplateTree(templateForEdit.itemTemplate);
};

sites_namespace.collectSites = function(){
    cursor_namespace.changeToLoad();
    bubble_namespace.showRequestMessage(100, true, 'Please wait till the parsing is over');
    $.ajax({
        url : "admin/site/banana",
        type:'GET',
        contentType: "application/json; charset=utf-8",
        success: function(result){
            bubble_namespace.showRequestMessage(result.code, result.succes, result.message);
            cursor_namespace.changeToNormal();
        },
        fail: function(result){
            bubble_namespace.showRequestMessage("FAIL", result, result);
            cursor_namespace.changeToNormal();
        }
    });
};