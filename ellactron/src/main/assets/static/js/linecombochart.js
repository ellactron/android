var timeFormat = 'MM/DD/YYYY';

function newDateString(days) {
    return moment().add(days, 'd').format(timeFormat);
}

var color = Chart.helpers.color;
var lineComboConfig = {
    type: 'bar',
    data: {
        labels: [
            newDateString(0),
            newDateString(1),
            newDateString(2),
            newDateString(3),
            newDateString(4),
            newDateString(5)
        ],
        datasets: [{
            type: 'line',
            label: 'Dataset 1',
            backgroundColor: color(window.chartColors.red).alpha(0.5).rgbString(),
            borderColor: window.chartColors.red,
//			fill: false,
            data: [
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100)
            ],
        }, {
            type: 'line',
            label: 'Dataset 2',
            backgroundColor: color(window.chartColors.blue).alpha(0.5).rgbString(),
            borderColor: window.chartColors.blue,
//			fill: false,
            data: [
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100)
            ],
        }, {
            type: 'line',
            label: 'Dataset 3',
            backgroundColor: color(window.chartColors.green).alpha(0.5).rgbString(),
            borderColor: window.chartColors.green,
//            fill: false,
            data: [
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100),
                randomScalingFactor(100)
            ],
        }, ]
    },
    options: {
        title: {
            text: "Chart.js Combo Time Scale"
        },
		legend: {
			labels: {
				boxWidth: 25,
				fontSize: 10,
			}
        },
		aspectRatio: 2.5,
        scales: {
            xAxes: [{
                type: "time",
                display: true,
                time: {
                    format: timeFormat,
                    round: false,
                }
            }],
        },
		layout: {
			padding: {
				top: 20,
			}
		}
    }
};

function initLineComboChart() {
    var ctx = document.getElementById("line-combo-chart-area").getContext("2d");
    window.myLineCombo = new Chart(ctx, lineComboConfig);
};

function updateLineComboChartData() {
	lineComboConfig.data.datasets.forEach(function(dataset) {
		dataset.data = dataset.data.map(function() {
			return randomScalingFactor(100);
		});
	});
}

function addLineComboChartDataset(){
	var colorNames = Object.keys(window.chartColors);
	var colorName = colorNames[lineComboConfig.data.datasets.length % colorNames.length];
	var newColor = window.chartColors[colorName];
	var newDataset = {
		label: 'Dataset ' + lineComboConfig.data.datasets.length,
		borderColor: newColor,
		backgroundColor: color(newColor).alpha(0.5).rgbString(),
		type: 'line',
		data: [],
	};

	for (var index = 0; index < lineComboConfig.data.labels.length; ++index) {
		newDataset.data.push(randomScalingFactor(100));
	}

	lineComboConfig.data.datasets.push(newDataset);
}

function removeLineComboChartDataset(index) {
	lineComboConfig.data.datasets.splice(index, 1);
}

function addLineComboChartData() {
	if (lineComboConfig.data.datasets.length > 0) {
		lineComboConfig.data.labels.push(newDateString(lineComboConfig.data.labels.length));
		for (var index = 0; index < lineComboConfig.data.datasets.length; ++index) {
			lineComboConfig.data.datasets[index].data.push(randomScalingFactor(100));
		}
	}
}

function removeLineComboChartData(index) {
	lineComboConfig.data.labels.splice(index, 1); // remove the label first
	lineComboConfig.data.datasets.forEach(function(dataset, datasetIndex) {
		lineComboConfig.data.datasets[datasetIndex].data.pop();
	});
}

function updateLinecomboChartButtons() {
	var updateLineComboChartDataButton = document.getElementById('updateLineComboChartData');
	if(null != updateLineComboChartDataButton)
		updateLineComboChartDataButton.addEventListener('click', function() {
			updateLineComboChartData();
			window.myLineCombo.update();
		});
	
	var addLineComboChartDatasetButton = document.getElementById('addLineComboChartDataset');
	if(null != addLineComboChartDatasetButton)
		addLineComboChartDatasetButton.addEventListener('click', function() {
			addLineComboChartDataset();
			window.myLineCombo.update();
		});

	var removeLineComboChartDatasetButton = document.getElementById('removeLineComboChartDataset');
	if(null != removeLineComboChartDatasetButton)
		removeLineComboChartDatasetButton.addEventListener('click', function() {
			removeLineComboChartDataset(randomScalingFactor(lineComboConfig.data.datasets.length) - 1);
			window.myLineCombo.update();
		});

	var addLineComboChartDataButton = document.getElementById('addLineComboChartData');
	if (null != addLineComboChartDataButton)
		document.getElementById('addLineComboChartData').addEventListener('click', function() {
			addLineComboChartData();
			window.myLineCombo.update();
		});

	var removeLineComboChartDataButton = document.getElementById('removeLineComboChartData');
	if(null != removeLineComboChartDataButton)
		document.getElementById('removeLineComboChartData').addEventListener('click', function() {
			removeLineComboChartData(-1);
			window.myLineCombo.update();
		});
};
updateLinecomboChartButtons();