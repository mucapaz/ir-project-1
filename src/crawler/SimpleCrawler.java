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
	
	public SimpleCrawler(String base, String robotURL, Manager manager) throws MalformedURLException, IOException{
		super(base, robotURL, manager);
		nextLinks = new ArrayList<String>();
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
