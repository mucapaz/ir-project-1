package crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import manager.Manager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleCrawler extends Crawler {

	private ArrayList<String> nextLinks;
	
	public SimpleCrawler(ArrayList<String> urls, ArrayList<String> robotUrls, Manager manager) throws MalformedURLException, IOException{
		super(urls, robotUrls, manager);
		nextLinks = new ArrayList<String>();
		
		for(String url : urls){
			addLink(url);
			usedLinks.add(url);
		}
	}
	
	@Override
	public void addLink(String link) {
		nextLinks.add(link);
	}
	
	@Override
	public int linksNumber() {
		return nextLinks.size();
	}

	@Override
	public String removeNextLink() {
		return nextLinks.remove(0);
	}
	
	
}
