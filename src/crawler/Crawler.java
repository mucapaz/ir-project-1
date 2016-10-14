package crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import manager.Manager;


public abstract class Crawler implements Runnable{

	protected static final int CRAW_LIMIT = 10000;

	protected String base;

	protected Robot robot;
	protected Manager manager;

	protected Set<String> usedLinks;

	public Crawler(ArrayList<String> urls, ArrayList<String> robotUrls, Manager manager) throws MalformedURLException, IOException{
		this.manager = manager;
		
		robot = new Robot(urls, robotUrls);		
		usedLinks = new HashSet<String>();
		
	}

	@Override
	public void run() {
		craw();

	}

	public void craw() {
		
		Document atLinkdocument;
		Elements nextLinkDocuments;

		String link;
		int it = 0;	
		while(linksNumber() > 0 && it < CRAW_LIMIT){
			
			try {

				atLinkdocument = Jsoup.connect(removeNextLink()).get();
				if(atLinkdocument != null){
					manager.addClassifierElement(atLinkdocument);

					nextLinkDocuments = atLinkdocument.select("a[href]");
					if(nextLinkDocuments != null){
						for(Element element : nextLinkDocuments){
							link = element.attr("abs:href");
							if(!usedLinks.contains(link) && !robot.disallow(link)){
								usedLinks.add(link);
								addLink(link);
							}
						}
					}					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("CRAWS TO GO " + (CRAW_LIMIT - it));
				e.printStackTrace();
			} 

			it++;
		}

	}

	public abstract void addLink(String link);

	public abstract int linksNumber();

	public abstract String removeNextLink();
	

}
