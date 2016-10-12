package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import classifier.ClassifierWrapper;

import crawler.SimpleCrawler;

public class Manager {
	
	private List<Document> classifierDocument;
	private List<Element> extractorElements;
	
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, IOException{
		Manager manager = new Manager();
		
	}
	
	public Manager() throws ClassNotFoundException, InterruptedException, IOException{
		classifierDocument = Collections.synchronizedList(new ArrayList<Document>());
		extractorElements = Collections.synchronizedList(new ArrayList<Element>());
		
		ArrayList<String> url = new ArrayList<String>(), robot = new ArrayList<String>();
		
		BufferedReader bf = new BufferedReader(new FileReader(new File("sites")));
		String line;
		
		while((line = bf.readLine()) != null){
//			System.out.println(line);
			url.add(line.split(" ")[0]);
			robot.add(line.split(" ")[1]);
		}
		
		
		
		try {
			Thread crawlerThread = new Thread(new SimpleCrawler(url, 
					robot, this));
			
			crawlerThread.start();
			
			Thread classifierThread = new Thread(new ClassifierWrapper("classifier.model", 
					"attributes", this));
			
			classifierThread.start();
//			
//			
			crawlerThread.join();
			classifierThread.join();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public void addClassifierElement(Document document){
//		System.out.println("added classifier element" + element.baseUri());
		classifierDocument.add(document);
	}
	
	public Document removeClassifierDocument(){
		
		while(classifierDocument.size() == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("removed classfier element");
		return classifierDocument.remove(0);
	}
	
	public void addExtractorElement(Element element){
		extractorElements.add(element);
	}
	
	public Element removeExtractorElement(){
		
		while(extractorElements.size() == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return extractorElements.remove(0);
	}
}
