var page = require('webpage').create(); 
var fs = require('fs'); 
var args = system.args; 
var output = 'pageOutput.html'; 
page.open('https://www.studyblue.com/#flashcard/view/14609350', function() { 
	 fs.write(output,page.content,'w'); 
	 phantom.exit(); 
}); 
