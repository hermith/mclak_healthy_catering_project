/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    $("#register_tab_left h3").click(function() {
        $("#register_tab_left").css("background-color", global_color_selected);
        $("#register_tab_right").css("background-color", global_color_dark);
        $("#registercorporate").fadeOut('fast', function() {
            $("#registerprivate").fadeIn('fast');
        });

    });

    $("#register_tab_right h3").click(function() {
        $("#register_tab_left").css("background-color", global_color_dark);
        $("#register_tab_right").css("background-color", global_color_selected);
        $("#registerprivate").fadeOut('fast', function() {
            $("#registercorporate").fadeIn('fast');
        });

    });
});