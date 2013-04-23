$(document).ready(function() {

    var show_add_product = document.getElementById("product_overview_hidden_form:add_product_popup").value === "true";
    var show_edit_product = document.getElementById("product_overview_hidden_form:edit_product_popup").value === "true";
    if (show_edit_product) {
        $("#popoup_edit_product").show();
        $("#site_opaque").show();
    } else {
        $("#popoup_edit_product").hide();
    }
    
    if (show_add_product) {
        $("#popoup_add_product").show();
        $("#site_opaque").show();
    } else {
        $("#popoup_add_product").hide();
    }
    
    if (!show_add_product && !show_edit_product) {
        $("#site_opaque").hide();
    }

    $("#product_tab_left h3").click(function() {
        $("#product_tab_left").css("background-color", "#333333");
        $("#product_tab_right").css("background-color", "#555555");
        $("#add_product_package").fadeOut('fast', function(){
            $("#add_product_single").fadeIn('fast');
        });
    });

    $("#product_tab_right h3").click(function() {
        $("#product_tab_left").css("background-color", "#555555");
        $("#product_tab_right").css("background-color", "#333333");
        $("#add_product_single").fadeOut('fast', function(){
            $("#add_product_package").fadeIn('fast');
        });
    });

});