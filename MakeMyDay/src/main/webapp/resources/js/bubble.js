
var bubble_namespace = {};

bubble_namespace.bubble = $("<div></div>").addClass("messageBubble").css("display", "none");

bubble_namespace.lastMessageCode = "-1";
bubble_namespace.lastMessageStatus = true;
bubble_namespace.lastMessageContent = "Nothing";
bubble_namespace.timeout;

bubble_namespace.init = function(){
    bubble_namespace.bubble = $(".messageBubble");
};

bubble_namespace.showRequestMessage = function(code, status, message){
    bubble_namespace.lastMessageCode = code;
    bubble_namespace.lastMessageStatus = status;
    bubble_namespace.lastMessageContent = message;
    if(bubble_namespace.timeout !== undefined){
        clearTimeout(bubble_namespace.timeout);
    }
    setTimeout(function(){
        var text = $("<h3>"+bubble_namespace.lastMessageContent+"</h3>");
        $(".messageBubble").empty();
        if(!bubble_namespace.lastMessageStatus){
            $(".messageBubble").toggleClass("alert-danger", true);
            $(".messageBubble").toggleClass("alert-success", false);
        }else{
            $(".messageBubble").toggleClass("alert-success", true);
            $(".messageBubble").toggleClass("alert-danger", false);
        }
        $(".messageBubble").append(text).css("display", "block");
        bubble_namespace.timeout = setTimeout(function(){
            $(".messageBubble").append(text).css("display", "none");
        },3000);
    },0);
};

bubble_namespace.showValidationMessage = function(message){
    bubble_namespace.lastMessageContent = message;
    setTimeout(function(){
        var text = $("<p>"+bubble_namespace.lastMessageContent+"</p>");
        $(".messageBubble").empty();
        $(".messageBubble").append(text).css("display", "block");
        setTimeout(function(){
            $(".messageBubble").append(text).css("display", "none");
        },3000);
    },0);
};
