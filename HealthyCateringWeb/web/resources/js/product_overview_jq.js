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
    
    if (!show_Add_product && !show_edit_product) {
        $("#site_opaque").hide();
    }

});