/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){

    $("#rb_private").click(function() {
        $("#registercorporate").fadeOut('fast', function() {
            $("#registerprivate").fadeIn('fast');
        });
        
        
    });

    $("#rb_corporate").click(function() {
        $("#registerprivate").fadeOut('fast', function() {
            $("#registercorporate").fadeIn('fast');
        });
        
    });
});