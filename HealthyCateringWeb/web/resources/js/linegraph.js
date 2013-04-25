/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function drawGraph() {
    var data = getData();
    var myData = new Array();
    
    var tuple;
    for(var i = 0; i < data.length-1; i+=2){
        myData.push([data[i]],data[i+1]);
    }
    
    for(var i = 0; i < myData.length; i++){
        alert(myData[i]);
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