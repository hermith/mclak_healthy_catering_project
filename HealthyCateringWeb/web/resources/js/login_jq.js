var loggedIn = false;
var loggedInAsPrivate = false;
var loggedInAsCorporate = false;

var global_color_dark = "#375D81";
var global_color_light = "#ABC8E2";
var global_color_selected = "#183152";

$(document).ready(function() {
    var isUp = true;
    
    loggedIn = document.getElementById("hidden_login_field:is_user_logged_in").value === "true";
    loggedInAsPrivate = document.getElementById("hidden_login_field:is_user_logged_in_as_private").value === "true";
    loggedInAsCorporate = document.getElementById("hidden_login_field:is_user_logged_in_as_corporate").value === "true";
    wrongPassword = document.getElementById("hidden_login_field:last_login_failed").value === "true";
    
    if(wrongPassword) {
        $("#top_bar_span_login").show();
        $("#site_opaque").show();
    } else {
        $("#top_bar_span_login").hide();
        $("#site_opaque").hide();
    }

    $("#top_bar_span_login_always_show").click(function() {
        
        if (!loggedIn) {
            // Starting animation of drop down and white overlay.
            $("#site_opaque").fadeToggle('slow');
            $("#top_bar_span_login").slideToggle('slow');

            // Enabling/disabling overflow (scroll bars)
            if (isUp) {
                //$("body").css("overflow", "hidden");
                isUp = false;
            } else {
                $("#top_bar_span_login").promise().done(function() {
                    //$("body").css("overflow", "auto").fadeIn('slow');
                    isUp = true;
                });
            }
        }

    }).children().click(function(e) {
        return false;
    });
    
    $("#forgot_password_link").click(function() {
        $(".login_form").fadeOut('fast', function() {
            $(".forgot_password").fadeIn('slow');
        });
    });
    
    $("#forgot_password_back").click(function() {
        $(".forgot_password").fadeOut('fast', function() {
            $(".login_form").fadeIn('slow');
        });
    });
    
    if(loggedInAsPrivate || loggedInAsCorporate) {
        $("article#primary_content").css("width", "685px");
        $("aside#sidebar").show();
    } else {
        $("article#primary_content").css("width", "973px");
        $("aside#sidebar").hide();
    }
});