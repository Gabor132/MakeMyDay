/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Template Element
 * @param {type} nrParinte
 * @param {type} nrCopil
 * @param {type} elementName
 * @param {type} elementType
 * @param {type} elementValueLocation
 * @param {type} elementValueType
 * @param {type} id
 * @returns {TemplateElement}
 */
function TemplateElement(nrParinte, nrCopil, elementName, elementType, elementValueLocation, elementValueType, id){
    this.id = id?id:"";
    this.nrParinte = nrParinte;
    this.nrCopil = nrCopil;
    this.orderNumber = nrParinte + "."+nrCopil;
    this.template = $("<div></div>");
    this.template.addClass("templateElement");
    this.template.attr("id",(this.orderNumber).replaceAll(".","_"));
    this.template.append("<p>"+this.orderNumber+"</p>");
    this.template.append("Element <br/>");
    var className = $("<input type=\"text\" class=\"elementClassName\"></input></br>").val(elementName?elementName:"");
    var classType = $("<select class=\"elementClassType\"></select></br>");
    classType.append("<option value=\"OBJECT\">OBJECT</option>");
    classType.append("<option value=\"IMAGE\">IMAGE</option>");
    classType.append("<option value=\"NAME\">NAME</option>");
    classType.append("<option value=\"DESCRIPTION\">DESCRIPTION</option>");
    classType.append("<option value=\"LOCATION\">LOCATION</option>");
    classType.append("<option value=\"DATE\">DATE</option>");
    classType.append("<option value=\"TYPE\">TYPE</option>");
    classType.append("<option value=\"URL\">URL</option>");
    classType.append("<option value=\"PRICE\">PRICE</option>");
    classType.change( function() {
        var parent = $(this).parent(".templateElement");
        if($(this).val() === "OBJECT"){
            parent.children(".addChildButton").show();
            parent.children(".valueType").hide();
            parent.children(".valueLocation").hide();
        }else{
            parent.children(".addChildButton").hide();
            parent.children(".valueType").show();
            parent.children(".valueLocation").show();
        }
    });
    var valueLocation = $("<input type=\"text\" class=\"elementValueLocation\"></input></br>");
    valueLocation.val(elementValueLocation?elementValueLocation:"");
    var valueType = $("<select class=\"elementValueType\"></select></br>");
    valueType.append("<option value=\"VALUE\">VALUE</option>");
    valueType.append("<option value=\"ATTRIBUTE\">ATTRIBUTE</option>");
    valueType.val(elementValueType?elementValueType:"VALUE");
    valueType.change(function(){
        if($(this).val() === "VALUE"){
            $(this).parent("div").children(".elementValueLocation").prop("disabled", true);
        }else{
            var parent = $(this).parent("div");
            var valueLocation = parent.children(".elementValueLocation");
            valueLocation.prop("disabled", false);
        }
    });
    if(valueType.val() === "VALUE"){
        valueLocation.prop("disabled", true);
    }
    this.template.append("Class/Element name: ");
    this.template.append(className);
    this.template.append("Element type: ");
    this.template.append(classType);
    this.template.append($("<div class=\"valueType\">Value type: </div>").append(valueType));
    this.template.append($("<div class=\"valueLocation\">Value location: </div>").append(valueLocation));
    var addButton = $("<div class=\"siteTemplateButton addChildButton btn\">Add Child</div>");
    addButton.attr("elementOrderNumber",this.orderNumber);
    addButton.click(function(){
        addElement($(this).attr("elementOrderNumber"));
    });
    var deleteButton = $("<div class=\"siteTemplateButton deleteButton btn\">Delete</div>");
    deleteButton.attr("elementOrderNumber",this.orderNumber);
    deleteButton.click(function(){
        deleteElement($(this).attr("elementOrderNumber"));
    });
    classType.val(elementType?elementType:"OBJECT");
    this.template.append(addButton);
    this.template.append(deleteButton);
    this.children = [];
}

//TEMPLATE ELEMENT FUNCTIONS
TemplateElement.prototype.addChild = function(elementName, elementType, elementValueLocation, elementValueType, id){
    var child = new TemplateElement(this.orderNumber, this.children.length+1, elementName, elementType, elementValueLocation, elementValueType, id);
    this.children.push(child);
    $("#"+this.orderNumber.replaceAll(".","_")).append(child.template);
};

TemplateElement.prototype.deleteChild = function(index){
    var child = this.children[index];
    this.template.children("#"+child.template.attr("id")).remove();
    this.children.splice(index,1);
    for(var i in this.children){
        this.children[i].remakeOrderNumber(this.orderNumber, parseInt(i)+1);
    }
};

TemplateElement.prototype.remakeOrderNumber = function(nrParinte, nrCopil){
    this.nrParinte = nrParinte;
    this.nrCopil = nrCopil;
    this.orderNumber = nrParinte + "."+nrCopil;
    this.template.attr("id", this.orderNumber.replaceAll(".","_"));
    this.template.children("p").replaceWith("<p>"+this.orderNumber+"</p>");
    this.template.children(".siteTemplateButton").attr("elementOrderNumber",this.orderNumber);
    for(var i in this.children){
        this.children[i].remakeOrderNumber(this.orderNumber, i);
    }
};

TemplateElement.prototype.getJSON = function(){
    var id = "#"+this.orderNumber.replaceAll(".","_");
    var className = $(id+">"+".elementClassName").val();
    var classType = $(id+">"+".elementClassType option:selected").text();
    var valueType = $(id+">"+".elementValueType option:selected").text();
    var valueLocation = "";
    if(valueType !== "VALUE"){
        valueLocation = $(id+">"+".elementValueLocation").val();
    }
    var valueType = $(id+">"+".elementValueType option:selected").text();
    var jsonArrayChildren = [];
    for(var i in this.children){
        jsonArrayChildren.push(this.children[i].getJSON());
    }
    var json = {
        'id':this.id,
        'className':className,
        'classType':classType,
        'valueLocation':valueLocation,
        'valueType':valueType,
        'children':jsonArrayChildren,
        'parent':null
    };
    return json;
};
