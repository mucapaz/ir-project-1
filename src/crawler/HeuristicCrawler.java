package crawler;

import java.io.IOException;
import java.net.MalformedURLException;

public class HeuristicCrawler extends Crawler{

	
	
	public HeuristicCrawler(String base, String robotURL)
			throws MalformedURLException, IOException {
		super(base, robotURL);
	}

	@Override
	public void addLink(String link) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int linksNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String removeNextLink() {
		// TODO Auto-generated method stub
		return null;
	}

}
