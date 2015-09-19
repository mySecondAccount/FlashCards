package flashCardPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.apache.commons.lang3.StringEscapeUtils;

public class executeContent {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, InterruptedException {
		String givenURL = "https://www.studyblue.com/notes/note/n/final-exam/deck/14520416";
		
		StringBuilder myBuilder = new StringBuilder(); 
		myBuilder.append("function returnFrameContent(pageName){ \n"); 
		myBuilder.append("var html = ''; \n");
		myBuilder.append("var framescount = pageName.framesCount+1; \n");
		myBuilder.append("for (var i = 0; i < framescount; i++) { \n");
		myBuilder.append("\t html += \" <!--- frame \" + i + \"--->\" + pageName.frameContent ;");
		myBuilder.append("\t pageName.switchToFrame(i); \n");
		myBuilder.append("} \n");
		myBuilder.append("return html; \n");
		myBuilder.append("}\n\n");
		
		myBuilder.append("var page = require('webpage').create(); \n");
		myBuilder.append("var fs = require('fs'); \n"); // File System Module
		myBuilder.append("var system = require('system'); \n");
		myBuilder.append("var args = system.args; \n");
		myBuilder.append("var output = 'pageOutput.html'; \n"); // path for saving the local file
		myBuilder.append("page.open('");
		myBuilder.append(givenURL);
		myBuilder.append("', function(status) { \n"); // open the file 
		myBuilder.append("\t if (status == 'fail') { \n");
		myBuilder.append("\t\t console.log('Failed to open page.'); \n");
		myBuilder.append("\t\t phantom.exit(1); \n"); // exit PhantomJs with failure
		myBuilder.append("\t } else { \n");
		myBuilder.append("\t\t var results = returnFrameContent(page); \n");
		
		myBuilder.append("\t\t fs.write(output,results,'w'); \n");
		//myBuilder.append("\t\t fs.write(output,page.content,'w'); \n");// Write the page to the local file using page.content
		myBuilder.append("\t\t phantom.exit(0); \n"); // exit PhantomJs with success
		myBuilder.append("\t } \n");
		myBuilder.append("}); \n");
		
		String phantomFile = myBuilder.toString();
		File file = new File("phantomWriter.js");
		FileWriter writer = new FileWriter(file);
		writer.write(phantomFile);
		writer.close();
		
		Process process = Runtime.getRuntime().exec("U:\\GitHub\\FlashCards\\phantomjs-2.0.0-windows\\bin\\phantomjs --ignore-ssl-errors=true phantomWriter.js");
		int exitStatus = process.waitFor();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String currentLine = null;
		StringBuilder stringBuilder = new StringBuilder(exitStatus == 0 ? "SUCCESS:" : "ERROR:");
		currentLine = bufferedReader.readLine();
		while (currentLine != null) {
			stringBuilder.append(currentLine);
			currentLine = bufferedReader.readLine();
		}
		System.out.println(stringBuilder.toString());
		
		// WebClient webClient = new WebClient();
		// HtmlPage myPage = ((HtmlPage)
		// webClient.getPage("https://www.studyblue.com/#flashcard/view/14609350"));

		// @SuppressWarnings("resource")
		// WebClient webClient = new WebClient();
		// String url = "https://www.studyblue.com/#flashcard/view/14609350";
		// HtmlPage page = webClient.getPage(url);
		// webClient.waitForBackgroundJavaScript(30 * 1000);

		// String html = page.asText();

		// org.jsoup.nodes.Document doc = Jsoup.parse(myPage.asText());
		//
		// System.out.println(doc);
		
		File myFile = new File("U:\\GitHub\\FlashCards\\pageOutput.html");
		org.jsoup.nodes.Document doc = Jsoup.parse(myFile, "UTF-8");
		
		Elements front = doc.select("div.front div.side div.text");
		Elements back = doc.select("div.back div.side div.text");
		
		File uploadReadyNotes = new File("uploadReadyNotes.csv");
		FileWriter uploadReadyWriter = new FileWriter(uploadReadyNotes);
		String cardFront;
		String cardBack;
		for(int i = 0; i < front.size(); i++) {
			cardFront = appendDQ(front.get(i).text());
			cardBack = appendDQ(back.get(i).text().replaceAll("\"", ""));
			uploadReadyWriter.write(cardFront + "," + cardBack + " \n");
		}
		uploadReadyWriter.close();
	}
	private static String appendDQ(String str) {
	    return "\"" + str + "\"";
	}
}
