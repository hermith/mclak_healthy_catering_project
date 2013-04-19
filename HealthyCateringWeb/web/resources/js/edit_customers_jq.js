

$(document).ready(function() {

    var show_popup_edit_customer = document.getElementById("edit_customers_form:edit_customer_detail").value === "true";
    if (show_popup_edit_customer) {
        $("#popoup_edit_customer").show();
        $("#site_opaque").show();
    } else {
        $("#popoup_edit_customer").hide();
        $("#site_opaque").hide();
    }

});