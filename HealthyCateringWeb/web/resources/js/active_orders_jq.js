

$(document).ready(function() {

    var show_popup = document.getElementById("active_orders_form:show_customer_detail").value === "true";
    if (show_popup) {
        $("#detail_info").show();
    } else {
        $("#detail_info").hide();
    }

});