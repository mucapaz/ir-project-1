package manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Element;

import crawler.SimpleCrawler;

public class Manager {
	
	private List<Element> classifierElements;
	private List<Element> extractorElements;
	
	public static void main(String[] args){
		Manager manager = new Manager();
		
	}
	
	public Manager(){
		classifierElements = Collections.synchronizedList(new ArrayList<Element>());
		extractorElements = Collections.synchronizedList(new ArrayList<Element>());
		
		
		try {
			Thread crawlerThread = new Thread(new SimpleCrawler("http://veja.abril.com.br/", 
					"http://veja.abril.com.br/robots.txt", this));
			
			crawlerThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void addClassifierElement(Element element){
		System.out.println("added " + element.baseUri());
		classifierElements.add(element);
	}
	
	public Element removeClassifierElement(){
		
		while(classifierElements.size() == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return classifierElements.remove(0);
	}
	
	public void addExtractorElement(Element element){
		extractorElements.add(element);
	}
	
	public Element removeExtractorElement(){
		
		while(classifierElements.size() == 0){
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
