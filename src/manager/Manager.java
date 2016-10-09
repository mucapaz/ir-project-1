package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Element;

public class Manager {
	
	private List<Element> classifierElements;
	private List<Element> extractorElements;
	
	public Manager(){
		classifierElements = Collections.synchronizedList(new ArrayList<Element>());
		extractorElements = Collections.synchronizedList(new ArrayList<Element>());
		
		
		
		
	}
	
	public void addClassifierElement(Element element){
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
