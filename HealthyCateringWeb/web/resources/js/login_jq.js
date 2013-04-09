$(document).ready(function() {
    var isUp = true;

    $login_bar = $("#top_bar_span_login");

    $("#top_bar_span_login_always_show").click(function() {
        $("#site_opaque").fadeToggle('slow');
        $login_bar.slideToggle('slow');
        if (isUp) {
            $("body").css("overflow", "hidden");
            isUp = false;
        } else {
            $login_bar.promise().done(function() {
                $("body").css("overflow", "auto");
                isUp = true;
            });
        }
    });
});