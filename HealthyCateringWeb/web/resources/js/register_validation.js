/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function checkPass()
{
    // Stores password fields as objects
    var pass1 = document.getElementById('pass1');
    var pass2 = document.getElementById('pass2');
    // Stores message field as object
    var message = document.getElementById('confirmMessage');
    // Setting colors (green and red) for visuals
    var goodColor = "#66cc66";
    var badColor = "#ff6666";

    // Check if both password fields are filled
    if (pass1.value !== "" && pass2.value !== "") {

        // Compares the values in the password fields
        if (pass1.value === pass2.value) {
            // The passwords match, and the green color is set as feedback
            pass2.style.backgroundColor = goodColor;
            message.style.color = goodColor;
            message.innerHTML = "Passwords Match!";
        } else {
            // The passwords don't match, and the red color is used as feedback
            pass2.style.backgroundColor = badColor;
            message.style.color = badColor;
            message.innerHTML = "Passwords Do Not Match!";
        }
    }
}