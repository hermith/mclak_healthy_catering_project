

$(document).ready(function() {

    var show_popup = document.getElementById("active_orders_form:show_customer_detail").value === "true";
    if (show_popup) {
        $("#detail_info").show();
        $("#site_opaque").show();
    } else {
        $("#detail_info").hide();
        $("#site_opaque").hide();
    }

    $("#order_tab_left h3").click(function() {
        $("#order_tab_left").css("background-color", "#333333").fadeTo('fast');
        $("#order_tab_right").css("background-color", "#555555");
        $("#order_order_history").slideUp('fast');
        $("#order_active_orders").slideDown('fast');
    });

    $("#order_tab_right h3").click(function() {
        $("#order_tab_left").css("background-color", "#555555");
        $("#order_tab_right").css("background-color", "#333333");
        $("#order_active_orders").slideUp('fast');
        $("#order_order_history").slideDown('fast');
    });



});