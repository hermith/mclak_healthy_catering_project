$(document).ready(function() {
    
    var show_popup = document.getElementById("active_orders_form:show_customer_detail").value === "true";
    var show_active = true;
    if (show_popup) {
        $("#detail_order_info").show();
        $("#site_opaque").show();
    } else {
        $("#detail_order_info").hide();
        $("#site_opaque").hide();
    }
    
    if(show_active) {
        show_left();
    } else {
        show_right();
    }

    $("#order_tab_left h3").click(function() {
        show_left();
        show_active = true;
    });

    $("#order_tab_right h3").click(function() {
        show_right();
        show_active = false;
    });



});

function show_left() {
    $("#order_tab_left").css("background-color", global_color_selected).fadeTo('fast');
    $("#order_tab_right").css("background-color", global_color_dark);
    $("#order_order_history").slideUp('fast');
    $("#order_active_orders").slideDown('fast');
}

function show_right() {
    $("#order_tab_left").css("background-color", global_color_dark);
    $("#order_tab_right").css("background-color", global_color_selected);
    $("#order_active_orders").slideUp('fast');
    $("#order_order_history").slideDown('fast');
}