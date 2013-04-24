$(document).ready(function() {

    var show_add_product = document.getElementById("product_overview_hidden_form:add_product_popup").value === "true";
    var show_edit_product = document.getElementById("product_overview_hidden_form:edit_product_popup").value === "true";
    var show_tab_left = document.getElementById("product_overview_hidden_form:show_left_tab").value === "true";
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

    if(show_tab_left) {
        $(".add_product_tab_single_left").css("background-color", "#333333");
        $(".add_product_tab_package_right").css("background-color", "#555555");
        $("#add_product_package").hide();
        $("#add_product_single").show();
    } else {
        $(".add_product_tab_single_left").css("background-color", "#555555");
        $(".add_product_tab_package_right").css("background-color", "#333333");
        $("#add_product_single").hide();
        $("#add_product_package").show();
    }

});