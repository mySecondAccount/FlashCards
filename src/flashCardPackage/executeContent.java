package flashCardPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class executeContent {

	public static void main(String[] args) throws IOException, InterruptedException {
		String givenURL = "https://www.studyblue.com/#flashcard/view/14609350";
		
		StringBuilder myBuilder = new StringBuilder(); 
		myBuilder.append("var page = require('webpage').create(); \n");
		myBuilder.append("var fs = require('fs'); \n"); // File System Module
		myBuilder.append("var args = system.args; \n");
		myBuilder.append("var output = 'pageOutput.html'; \n"); // path for saving the local file
		
		myBuilder.append("page.open('");
		myBuilder.append(givenURL);
		myBuilder.append("', function() { \n"); // open the file 
		myBuilder.append("\t fs.write(output,page.content,'w'); \n");// Write the page to the local file using page.content
		myBuilder.append("\t phantom.exit(); \n"); // exit PhantomJs
		myBuilder.append("}); \n");
		
		String phantomFile = myBuilder.toString();
		File file = new File("phantomWriter.js");
		FileWriter writer = new FileWriter(file);
		writer.write(phantomFile);
		writer.close();
		
		Process process = Runtime.getRuntime().exec("U:\\GitHub\\FlashCards\\phantomjs-2.0.0-windows\\bin\\phantomjs phantomWriter.js");
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
	}

}
