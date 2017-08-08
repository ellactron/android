var MONTHS = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var lineChartConfig = {
    type: 'line',
    data: {
        labels: ["January", "February", "March", "April", "May", "June"],
        datasets: [{
            label: "My First dataset",
            backgroundColor: window.chartColors.red,
            borderColor: window.chartColors.red,
            data: [
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100)
            ],
            fill: false,
        }, {
            label: "My Second dataset",
            fill: false,
            backgroundColor: window.chartColors.blue,
            borderColor: window.chartColors.blue,
            data: [
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100)
            ],
        }]
    },
    options: {
        responsive: true,
        title: {
            display: true,
            text: 'Power consumption detail for last 6 months'
        },
        tooltips: {
            mode: 'index',
            intersect: false,
        },
        hover: {
            mode: 'nearest',
            intersect: true
        },
        scales: {
            xAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Month'
                }
            }],
            yAxes: [{
                display: true,
                scaleLabel: {
                    display: true,
                    labelString: 'Value'
                }
            }]
        }
    }
};

/*window.onload = function() {
    var ctx = document.getElementById("line-chart-area").getContext("2d");
    window.myLine = new Chart(ctx, lineChartConfig);
};*/

function initLineChart() {
	var ctx = document.getElementById("line-chart-area").getContext("2d");
	myLine = new Chart(ctx, lineChartConfig);
	return myLine;
}
	
document.getElementById('updateLineChartData').addEventListener('click', function() {
    lineChartConfig.data.datasets.forEach(function(dataset) {
        dataset.data = dataset.data.map(function() {
            return randomScalingFactor(100);
        });

    });

    window.myLine.update();
});

var colorNames = Object.keys(window.chartColors);
document.getElementById('addLineChartDataset').addEventListener('click', function() {
    var colorName = colorNames[lineChartConfig.data.datasets.length % colorNames.length];
    var newColor = window.chartColors[colorName];
    var newDataset = {
        label: 'Dataset ' + lineChartConfig.data.datasets.length,
        backgroundColor: newColor,
        borderColor: newColor,
        data: [],
        fill: false
    };

    for (var index = 0; index < lineChartConfig.data.labels.length; ++index) {
        newDataset.data.push(randomScalingFactor(100));
    }

    lineChartConfig.data.datasets.push(newDataset);
    window.myLine.update();
});

document.getElementById('removeLineChartDataset').addEventListener('click', function() {
    lineChartConfig.data.datasets.splice(0, 1);
    window.myLine.update();
});

document.getElementById('addLineChartDatasetData').addEventListener('click', function() {
    if (lineChartConfig.data.datasets.length > 0) {
        var month = MONTHS[lineChartConfig.data.labels.length % MONTHS.length];
        lineChartConfig.data.labels.push(month);

        lineChartConfig.data.datasets.forEach(function(dataset) {
            dataset.data.push(randomScalingFactor(100));
        });

        window.myLine.update();
    }
});
document.getElementById('removeLineChartDatasetData').addEventListener('click', function() {
    lineChartConfig.data.labels.splice(-1, 1); // remove the label first

    lineChartConfig.data.datasets.forEach(function(dataset, datasetIndex) {
        dataset.data.pop();
    });

    window.myLine.update();
});