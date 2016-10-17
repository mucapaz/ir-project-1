package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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

	private String savePageTo = "crawler_saved_pages";
	
	protected static final int CRAW_LIMIT = 10000;

	protected String base;

	protected Manager manager;

	protected Set<String> usedLinks;

	public Crawler(ArrayList<String> urls, ArrayList<String> robotUrls, Manager manager) throws MalformedURLException, IOException{
		this.manager = manager;
		usedLinks = new HashSet<String>();
		
		File file = new File("crawler_saved_pages");
		file.mkdir();
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
				
				savePage(atLinkdocument.toString(), savePageTo +"/"+ it+".html");
				if(atLinkdocument != null){
					manager.addClassifierElement(atLinkdocument);

					nextLinkDocuments = atLinkdocument.select("a[href]");
					if(nextLinkDocuments != null){
						for(Element element : nextLinkDocuments){
							link = element.attr("abs:href");
							if(!usedLinks.contains(link) && Robot.isAllowed(link)){
								usedLinks.add(link);
								addLink(link);
							}
						}
					}					
				}
				it++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("CRAWS TO GO " + (CRAW_LIMIT - it));
				e.printStackTrace();
			} 

		}

	}

	public void savePage(String page, String pageName){
		BufferedWriter htmlWriter;
		try {
			htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pageName), "UTF-8"));
			htmlWriter.write(page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public abstract void addLink(String link);

	public abstract int linksNumber();

	public abstract String removeNextLink();
	

}
