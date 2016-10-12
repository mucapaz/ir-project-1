package crawler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import manager.Manager;

public class HeuristicCrawler extends Crawler{

	private ArrayList<String> bestLinks;
	private ArrayList<String> otherLinks;
	
	private Random rand;
	
	private Set<String> words;
	
	public HeuristicCrawler(ArrayList<String> urls, ArrayList<String> robotUrls, Manager manager)
			throws MalformedURLException, IOException {
		super(urls, robotUrls, manager);
		
		rand = new Random();
		
		bestLinks = new ArrayList<String>();
		otherLinks = new ArrayList<String>();
		words = new HashSet<String>();
		
		File file = new File("heuristic_words");
		Scanner in = new Scanner(file);
		
		while(in.hasNextLine()){
			words.add(in.nextLine());
		}
		
		
	}

	@Override
	public void addLink(String link) {
		boolean ok = false;
		
		String[] split = link.split("/");
		for(int x=0;x<split.length;x++){
			if(words.contains(split[x])){
				ok = true;
				break;
			}
		}
		
		if(ok){
			bestLinks.add(link);
		}else{
			otherLinks.add(link);
		}
		
	}

	@Override
	public int linksNumber() {
		return bestLinks.size() + otherLinks.size(); 
	}

	@Override
	public String removeNextLink() {
		int randNumber = rand.nextInt(100);
		
		if(bestLinks.size() > 0 && otherLinks.size() > 0){
			if(randNumber <= 80){
				return bestLinks.remove(0);
			}else{
				return otherLinks.remove(0);
			}
		}else if(bestLinks.size() > 0){
			return bestLinks.remove(0);
		}else{
			return otherLinks.remove(0);
		}
	}

}
