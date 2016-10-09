package crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import manager.Manager;


public abstract class Crawler implements Runnable{

	protected static final int CRAW_LIMIT = 100;
	
	protected String base;
	
	protected Robot robot;
	protected Manager manager;
	
	protected Set<String> usedLinks;
	
	public Crawler(String base, String robotURL) throws MalformedURLException, IOException{
		this.base = base;
		robot = new Robot(base, robotURL);
		usedLinks = new HashSet<String>();
	}
	
	@Override
	public void run() {
		craw();
	
	}
	
	public void craw() {
		addLink(base);
		usedLinks.add(base);
		
		Element atLinkElement;
		Elements nextLinkElements;
		
		String link;
		
		int it = 0;	
		while(linksNumber() > 0 && it < CRAW_LIMIT){
			
			try {
								
				atLinkElement = Jsoup.connect(removeNextLink()).get();
				manager.addClassifierElement(atLinkElement);
				
				nextLinkElements = atLinkElement.select("a[href]");
			
				for(Element element : nextLinkElements){
					link = element.attr("abs:href");
					if(!usedLinks.contains(link) && !robot.disallow(link)){
						usedLinks.add(link);
						addLink(link);
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			it++;
		}
		
	}
	
	public abstract void addLink(String link);
	
	public abstract int linksNumber();
	
	public abstract String removeNextLink();
	
}
