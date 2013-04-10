$(document).ready(function() {
    var isUp = true;

    $("#top_bar_span_login_always_show").click(function() {

        // Starting animation of drop down and white overlay.
        $("#site_opaque").fadeToggle('slow');
        $("#top_bar_span_login").slideToggle('slow');

        // Enabling/disabling overflow (scroll bars)
        if (isUp) {
            $("body").css("overflow", "hidden");
            isUp = false;
        } else {
            $("#top_bar_span_login").promise().done(function() {
                $("body").css("overflow", "auto").fadeIn('slow');
                isUp = true;
            });
        }

    });

    /*$(".control_panel_header").click(function() {
        $(".control_panel_button_section").slideToggle('slow');
    });*/
});