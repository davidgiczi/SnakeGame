/**
 * 
 */

var msg = document.getElementById("end").value;
var tempo =  parseInt(document.getElementById("tempo").value);
document.addEventListener("keydown", pressButton);


if(msg !== ""){

alert(msg)
document.getElementById("start").submit();

}

var run = setInterval(sendGoDirectRequest, tempo);

function pressButton(e) {

    e = e || window.event;
	
    if (e.keyCode === 37) {
	
        document.getElementById("left").submit();
    } else if (e.keyCode === 39) {
	
       document.getElementById("right").submit();

    }  else if (e.keyCode === 13) {
	
      alert("Szünet");
    } 
}

function sendGoDirectRequest(){
	document.getElementById("direct").submit();
}