/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    /*
     $("#rb_private").click(function() {
     $("#registercorporate").fadeOut('fast', function() {
     $("#registerprivate").fadeIn('fast');
     });
     
     
     });
     
     $("#rb_corporate").click(function() {
     $("#registerprivate").fadeOut('fast', function() {
     $("#registercorporate").fadeIn('fast');
     });
     
     });*/

    $("#register_tab_left h3").click(function() {
        $("#register_tab_left").css("background-color", "#333333").fadeTo('fast');
        $("#register_tab_right").css("background-color", "#555555");
        $("#registercorporate").fadeOut('fast', function() {
            $("#registerprivate").fadeIn('fast');
        });

    });

    $("#register_tab_right h3").click(function() {
        $("#register_tab_left").css("background-color", "#555555");
        $("#register_tab_right").css("background-color", "#333333");
        $("#registerprivate").fadeOut('fast', function() {
            $("#registercorporate").fadeIn('fast');
        });

    });
});