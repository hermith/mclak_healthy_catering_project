$(document).ready(function() {
    
    $("#place_order_form\\:place_order_radio\\:0").click(function() {
        $("#place_order_delivery_date_form").slideUp('slow')
    });
    
    $("#place_order_form\\:place_order_radio\\:1").click(function() {
        $("#place_order_delivery_date_form").slideDown('slow');
    });
});