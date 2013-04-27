/**
 * Used on register.xhtml to check that two passwords match real time.
 * 
 * @author Aleks
 * 
 */

/**
 * Checks password fields in registration form for private
 * user.
 */
function checkPassPrivate() {
    var pass1 = document.getElementById('registerprivate:pass1');
    var pass2 = document.getElementById('registerprivate:pass2');
    var goodColor = "#66cc66";
    var badColor = "#ff6666";

    if (pass1.value !== "" && pass2.value !== "") {

        if (pass1.value === pass2.value) {
            pass2.style.backgroundColor = goodColor;
        } else {
            pass2.style.backgroundColor = badColor;
        }
    }
}

/**
 * Checks password fields in registration form for corporate
 * user.
 */
function checkPassCorporate()
{
    var pass3 = document.getElementById('registercorporate:pass3');
    var pass4 = document.getElementById('registercorporate:pass4');
    var goodColor = "#66cc66";
    var badColor = "#ff6666";

    if (pass3.value !== "" && pass4.value !== "") {

        if (pass3.value === pass4.value) {
            pass4.style.backgroundColor = goodColor;
        } else {
            pass4.style.backgroundColor = badColor;
        }
    }
}