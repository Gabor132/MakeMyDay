/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Template Root
 * @param {type} classElementName
 * @param {type} id
 * @returns {TemplateRoot}
 */
function TemplateRoot(classElementName, id){
    this.id = id?id:"";
    this.template = $("<div></div>");
    this.template.addClass("templateRoot");
    this.orderNumber = "1";
    this.template.append("<p>"+this.orderNumber+"</p>");
    this.template.append("Template Root <br/>");
    this.template.append("Class/Element name: <input type=\"text\" id=\"rootClassName\" value=\""+
            (classElementName ? classElementName : "") +"\"></input>");
    var addButton = $("<br/><div class=\"siteTemplateButton btn\">Add Child</div>");
    addButton.click(function(){
        addElement("1");
    });
    this.template.append(addButton);
    this.children = [];
}

//TEMPLATE ROOT FUNCTIONS
TemplateRoot.prototype.addChild = function(elementName, elementType, elementValueLocation, elementValueType, id){
    var child = new TemplateElement(this.orderNumber, this.children.length+1, elementName, elementType, elementValueLocation, elementValueType, id);
    this.children.push(child);
    $(".templateRoot").append(child.template);
};

TemplateRoot.prototype.deleteChild = function(index){
    var child = this.children[index];
    this.template.children("#"+child.template.attr("id")).remove();
    this.children.splice(index,1);
    for(var i in this.children){
        this.children[i].remakeOrderNumber(this.orderNumber, parseInt(i)+1);
    }
};

TemplateRoot.prototype.getJSON = function(){
    var className = $(".templateRoot >"+"#rootClassName").val();
    var jsonArrayChildren = [];
    for(var i in this.children){
        jsonArrayChildren.push(this.children[i].getJSON());
    }
    var json = {
        'id':parseInt(this.id),
        'name':className,
        'hostSite':null,
        'classes':jsonArrayChildren
    };
    return json;
};