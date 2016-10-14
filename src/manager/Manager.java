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
import crawler.HeuristicCrawler;
import crawler.SimpleCrawler;
import extractor.Extractor;

public class Manager {
	
	private List<Document> classifierDocuments;
	private List<Document> extractorDocuments;
	
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, IOException{
		Manager manager = new Manager();
		
	}
	
	public Manager() throws ClassNotFoundException, InterruptedException, IOException{
		classifierDocuments = Collections.synchronizedList(new ArrayList<Document>());
		extractorDocuments = Collections.synchronizedList(new ArrayList<Document>());
		
		ArrayList<String> url = new ArrayList<String>(), robot = new ArrayList<String>();
		
		BufferedReader bf = new BufferedReader(new FileReader(new File("sites")));
		String line;
		
		while((line = bf.readLine()) != null){
			url.add(line.split(" ")[0]);
			robot.add(line.split(" ")[1]);
		}
		
		
		
		try {
			Thread crawlerThread = new Thread(new HeuristicCrawler(url, 
					robot, this));
			crawlerThread.start();
			
			Thread classifierThread = new Thread(new ClassifierWrapper("classifier.model", 
					"attributes", this));
			classifierThread.start();
		
			
			Thread extractorThread = new Thread( new Extractor(true, this));
			extractorThread.start();
			
			
			
			crawlerThread.join();
			System.out.println("Crawler terminated");
			classifierThread.join();
			System.out.println("Classifier terminated");
			extractorThread.join();
			System.out.println("Extractor terminated");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addClassifierElement(Document document){
		classifierDocuments.add(document);
		System.out.println("Added document to classifierList: " + document.baseUri());
	}
	
	public Document removeClassifierDocument(){
		
		while(classifierDocuments.size() == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return classifierDocuments.remove(0);
	}
	
	public void addExtractorDocument(Document document){
		extractorDocuments.add(document);
		System.out.println("Added document to extractorList: " + document.baseUri());
	}
	
	public Document removeExtractorDocument(){
		
		while(extractorDocuments.size() == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return extractorDocuments.remove(0);
	}
}
