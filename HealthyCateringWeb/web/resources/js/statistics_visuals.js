/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function drawGraph() {
    var data = getData();
    var myData = new Array();

    for (var i = 0; i < data.length - 1; i += 2) {
        myData.push([parseFloat(data[i]), parseFloat(data[i + 1])]);
    }

    var myChart = new JSChart('graph', 'line');
    myChart.setDataArray(myData);
    myChart.draw();
}

function getData() {
    var numbers = document.getElementById("stat_form:orderstring").value;
    var split = numbers.split(" ");

    var data = new Array();
    for (var i = 0; i < split.length; i += 2) {
        data.push([split[i], split[i + 1]]);
    }
    return split;
}

function drawPieChart() {
    var numbers = document.getElementById("stat_form:productstring").value;
    var split = numbers.split(" ");
    //var myData = new Array(['Sector 1', 2], ['Sector 2', 1], ['Sector 3', 3], ['Sector 4', 6], ['Sector 5', 8.5], ['Sector 6', 10]);
    var myData = new Array();

    for (var i = 0; i < split.length-1; i += 2) {
        myData.push(['Product #' + parseFloat(split[i]), parseFloat(split[i + 1])]);
    }

    //var colors = ['#101', '#505', '#909'];
    
    var colors = [];
    
    for(var i = 0; i < myData.length; i++){
        colors.push(getRandomColor());
    }
    
    var myChart = new JSChart('chart', 'pie');
    myChart.setDataArray(myData);
    myChart.colorizePie(colors);
    myChart.setTitleColor('#857D7D');
    myChart.setPieUnitsColor('#9B9B9B');
    myChart.setPieValuesColor('#6A0000');
    myChart.draw();
}

function getRandomColor(){
    var color = "#";
    
    for(var i = 0; i < 3; i++){
        color += Math.floor(Math.random()*10);
    }
    
    alert(color);
    
    return color;
}