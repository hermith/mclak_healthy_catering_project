/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function checkPassPrivate()
{
    // Stores password fields as objects
    var pass1 = document.getElementById('registerprivate:pass1');
    var pass2 = document.getElementById('registerprivate:pass2');
    // Setting colors (green and red) for visuals
    var goodColor = "#66cc66";
    var badColor = "#ff6666";

    // Check if both password fields are filled
    if (pass1.value !== "" && pass2.value !== "") {

        // Compares the values in the password fields
        if (pass1.value === pass2.value) {
            // The passwords match, and the green color is set as feedback
            pass2.style.backgroundColor = goodColor;
        } else {
            // The passwords don't match, and the red color is used as feedback
            pass2.style.backgroundColor = badColor;
        }
    }
}

function checkPassCorporate()
{
    // Stores password fields as objects
    var pass3 = document.getElementById('registercorporate:pass3');
    var pass4 = document.getElementById('registercorporate:pass4');
    // Setting colors (green and red) for visuals
    var goodColor = "#66cc66";
    var badColor = "#ff6666";

    // Check if both password fields are filled
    if (pass3.value !== "" && pass4.value !== "") {

        // Compares the values in the password fields
        if (pass3.value === pass4.value) {
            // The passwords match, and the green color is set as feedback
            pass4.style.backgroundColor = goodColor;
        } else {
            // The passwords don't match, and the red color is used as feedback
            pass4.style.backgroundColor = badColor;
        }
    }
}