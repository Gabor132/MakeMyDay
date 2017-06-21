
/**
 * Get the parent where the id will be, the elements to be inserted in the table
 * the array of header elements and the function that creates the javascript element
 * from the json
 * @param {String} parentId
 * @param {Json} arrayElements
 * @param {Array} headerArray
 * @param {Function} functionElement
 * @param {Boolean} hasEdit
 * @param {Boolean} hasDelete
 * @returns void
 */
function generateTable(parentId, arrayElements, headerArray, functionElement, hasEdit, hasDelete){
    if(hasEdit === undefined){
        hasEdit = true;
    }
    if(hasDelete === undefined){
        hasDelete = false;
    }
    var parent = $(parentId);
    var table = $("<table></table>");
    var headerRow = $("<thead></thead>");
    for(var index in headerArray){
        if(!hasEdit && !hasDelete && headerArray[index] === "EDIT BUTTONS"){
            continue;
        }
        headerRow.append($("<th>"+headerArray[index]+"</th>"));
    }
    table.append(headerRow);
    for(var i = 0; i< arrayElements.length; i++){
        var line = functionElement(arrayElements[i], hasEdit, hasDelete);
        table.append(line);
    }
    parent.append(table);
}