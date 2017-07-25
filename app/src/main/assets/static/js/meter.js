function addMeter(dataValue) {
	var value = dataValue;
	var speed = 61-60*value/250;
	
	var meterDiv = document.createElement('div');
	meterDiv.className = 'meter';
	
	var circleDiv = document.createElement('div');
	circleDiv.className = 'circle';
	circleDiv.style.WebkitAnimation = "loading "+speed+"s infinite linear";
	circleDiv.style.animation="loading "+speed+"s infinite linear";
	
	var digitsDiv = document.createElement('div');
	digitsDiv.className = 'digits';
	digitsDiv.innerHTML = value;
	
	meterDiv.appendChild(circleDiv);
	meterDiv.appendChild(digitsDiv);
	
	var addMeterDiv = document.getElementsByClassName("addMeter");
	var parentNode = addMeterDiv[0].parentNode;
	parentNode.insertBefore(meterDiv, addMeterDiv[0]);
}

function removeMeter(index) {
	var meterDivs = document.getElementsByClassName("meter");
	if(meterDivs.length > 0) {
		//meterDivs[index].remove();
		meterDivs[index].parentNode.removeChild(meterDivs[index])
	}
}