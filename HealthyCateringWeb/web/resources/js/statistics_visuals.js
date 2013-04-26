/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function drawGraph() {
    var numbers = document.getElementById("stat_form:orderstring").value;
    var split = numbers.split(" ");
    var myData = new Array();
    for (var i = 0; i < split.length - 1; i += 2) {
        myData.push([parseFloat(split[i]), parseFloat(split[i + 1])]);
    }

    var myChart = new JSChart('graph', 'line');
    myChart.setDataArray(myData);
    myChart.setTitleColor('#000000');
    myChart.setAxisNameX('Order IDs');
    myChart.setAxisNameY('Order total price');
    myChart.setPieUnitsColor('#000000');
    myChart.draw();
}

function drawPieChart() {
    var numbers = document.getElementById("stat_form:productstring").value;
    var split = numbers.split(" ");
    var myData = new Array();
    for (var i = 0; i < split.length - 1; i += 2) {
        myData.push(['Product ID: ' + parseFloat(split[i]), parseFloat(split[i + 1])]);
    }

    var colors = [];
    for (var i = 0; i < myData.length; i++) {
        colors.push(getRandomColor());
    }

    var myChart = new JSChart('chart', 'pie');
    myChart.setDataArray(myData);
    myChart.colorizePie(colors);
    myChart.setTitleColor('#000000');
    myChart.setPieUnitsColor('#9B9B9B');
    myChart.setPieValuesColor('#6A0000');
    myChart.draw();
}

function getRandomColor() {
    var color = "#";
    var newChar;
    for (var i = 0; i < 3; i++) {
        newChar = Math.floor(Math.random() * 16);
        if(newChar === 10){
            newChar = 'a';
        }
        if(newChar === 11){
            newChar = 'b';
        }
        if(newChar === 12){
            newChar = 'c';
        }
        if(newChar === 13){
            newChar = 'd';
        }
        if(newChar === 14){
            newChar = 'e';
        }
        if(newChar === 15){
            newChar = 'f';
        }
        color += newChar;
        
    }

    return color;
}