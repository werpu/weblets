function setDemoText() {
    var area = dojo.byId("demotext");
    if(area)
        area.innerHTML = "This text was set by a weblet include javascript from the non dojo weblet";
}

setTimeout("setDemoText()", 3000);