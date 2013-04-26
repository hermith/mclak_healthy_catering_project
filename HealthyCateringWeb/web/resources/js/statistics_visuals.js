/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Method collects data from a hidden input element on the page, which in turn recieves
 * data from the StatisticsBean.
 * 
 * Data is sorted into an array of tuples [x, y] where x is order ID, and y is
 * price of corresponding order.
 * 
 * Parses the information and draws a line graph with the corresponding axes.
 * 
 * @returns N/A
 */
function drawGraph() {
    var numbers = document.getElementById("stat_form:orderstring").value;
    var split = numbers.split(" ");
    var myData = new Array();
    for (var i = 0; i < split.length - 1; i += 2) {
        myData.push([parseFloat(split[i]), parseFloat(split[i + 1])]);
    }

    var myChart = new JSChart('graph_price_per_id', 'line');
    myChart.setDataArray(myData);
    myChart.setTitleColor('#000000');
    myChart.setAxisNameX('Order IDs');
    myChart.setAxisNameY('Order total price');
    myChart.setPieUnitsColor('#000000');
    myChart.setTitle('Price of orders registered');
    myChart.setSize(500, 300);
    myChart.draw();
}


/**
 * Method collects data from a hidden input element on the page, which in turn recieves
 * data from the StatisticsBean.
 * 
 * Data is sorted into an array of tuples [x, y] where x is product ID, and y is
 * frequency of purchase.
 * 
 * Parses the information and draws a line graph with the corresponding axes.
 * 
 * Colors for each segment is randomly generated because the number of colors needs
 * to correspond to the number of segments. This results in changing colors upon refresh.
 * 
 * @returns N/A
 */
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

    var myChart = new JSChart('chart_pie_popularity', 'pie');
    myChart.setDataArray(myData);
    myChart.colorizePie(colors);
    myChart.setTitleColor('#000000');
    myChart.setPieUnitsColor('#9B9B9B');
    myChart.setPieValuesColor('#6A0000');
    myChart.setTitle('Product order frequency');
    myChart.setSize(500, 300);
    myChart.draw();
}

/**
 * Helper function to create a random color in the #000 format.
 * 
 * @returns {String} Random color.
 */
function getRandomColor() {
    var color = "#";
    var newChar;
    for (var i = 0; i < 3; i++) {
        newChar = Math.floor(Math.random() * 16);
        if (newChar === 10) {
            newChar = 'a';
        }
        if (newChar === 11) {
            newChar = 'b';
        }
        if (newChar === 12) {
            newChar = 'c';
        }
        if (newChar === 13) {
            newChar = 'd';
        }
        if (newChar === 14) {
            newChar = 'e';
        }
        if (newChar === 15) {
            newChar = 'f';
        }
        color += newChar;

    }
    return color;
}