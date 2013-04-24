/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function drawGraph() {
    var myData = new Array([10, 20], [15, 10], [20, 30], [25, 10], [30, 5]);
    var myChart = new JSChart('graph', 'line');
    myChart.setDataArray(myData);
    myChart.draw();
}


function getData() {

    window.onload = init;

    function init() {
        var numbers = document.getElementById("stat_form:orderstring").value;
        alert(numbers);
        var data = new Array();
    }
}