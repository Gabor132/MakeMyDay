/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global templateRoot, sites_namespace */

//COMMON FUNCTIONS FOR ALL TYPES OF TEMPLATE TREES

function addElement(orderNumber){
    var parent = findElement(orderNumber);
    if(parent !== undefined){
        parent.addChild();
    }else{
        alert("TRYING TO ADD TO UNEXISTING ELEMENT!");
    }
}

function deleteElement(orderNumber){
    var parentOrderNumber = orderNumber.substring(0,orderNumber.length-2);
    var parent = findElement(parentOrderNumber);
    for(var i in parent.children){
        if(parent.children[i].orderNumber === orderNumber){
            parent.deleteChild(i);
        }
    }
    
}

function findElement(orderNumber){
    var queue = [];
    queue.push(sites_namespace.templateRoot);
    var foundElement = undefined;
    while(queue.length !== 0){
        var current = queue.pop();
        if(current.orderNumber === orderNumber){
            foundElement = current;
            while(queue.length > 0){
                queue.pop();
            }
            continue;
        }
        for(var i in current.children){
            queue.push(current.children[i]);
        }
    }
    return foundElement;
}
