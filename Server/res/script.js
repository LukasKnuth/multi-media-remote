/**
* Assign methods to send the AJAX-requests for every button
*  with the corresponding functionality.
*/
$(document).ready(function(){
    // Pause/Play
    $("#pause_play").click(function(){
        // Send POST-request per AJAX.
        sendAjaxPost("PAUSE_PLAY");
    });
    // Stop
    $("#stop").click(function(){
        sendAjaxPost("STOP");
    });
    // Next
    $("#next").click(function(){
        sendAjaxPost("NEXT");
    });
    // Previous
    $("#previous").click(function(){
        sendAjaxPost("PREVIOUS");
    });
    // Louder
    $("#vol_up").click(function(){
        sendAjaxPost("VOL_UP");
    });
    // Volume lower
    $("#vol_down").click(function(){
        sendAjaxPost("VOL_DOWN");
    });
    // Mute
    $("#mute").click(function(){
        sendAjaxPost("MUTE");
    });
});

/**
* Send an AJAX-request with the given action to the main
*  servlet of the MMR-Server.
* @param action_str the action to send to the MMR-Server
*  as a String (same as for the MMR-protocol)
*/
function sendAjaxPost(action_str){
    $.post("/", {action : action_str}, "json")
        .success(function(){ajaxPostSuccess();})
        .error(function(){ajaxPostError();});
}

/**
* Called if the request was successfully send. </p>
* This method updates the "status"-span with a success-message.
*/
function ajaxPostSuccess(){
    $("#status").text("Success!");
}

/**
* Called if the request was not successfully send. </p>
* Will update the "status"-span with a error-message.
*/
function ajaxPostError(){
    $("#status").text("Error...");
}