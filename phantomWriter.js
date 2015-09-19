function returnFrameContent(pageName){ 
var html = ''; 
var framescount = pageName.framesCount+1; 
for (var i = 0; i < framescount; i++) { 
	 html += " <!--- frame " + i + "--->" + pageName.frameContent ;	 pageName.switchToFrame(i); 
} 
return html; 
}

var page = require('webpage').create(); 
var fs = require('fs'); 
var system = require('system'); 
var args = system.args; 
var output = 'pageOutput.html'; 
page.open('https://www.studyblue.com/notes/note/n/final-exam/deck/14520416', function(status) { 
	 if (status == 'fail') { 
		 console.log('Failed to open page.'); 
		 phantom.exit(1); 
	 } else { 
		 var results = returnFrameContent(page); 
		 fs.write(output,results,'w'); 
		 phantom.exit(0); 
	 } 
}); 
