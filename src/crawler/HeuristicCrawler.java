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

	private ArrayList<String> baseLinks;	
	private ArrayList<ArrayList<String>> bestLinks;
	
	
	private ArrayList<ArrayList<String>> otherLinks;
	
	
	private Random rand;
	
	private Set<String> words;
	
	public HeuristicCrawler(ArrayList<String> urls, ArrayList<String> robotUrls, Manager manager)
			throws MalformedURLException, IOException {
		super(urls, robotUrls, manager);
		
		rand = new Random();
		
		bestLinks = new ArrayList<ArrayList<String>>();
		otherLinks = new ArrayList<ArrayList<String>>();
		
		for(int x=0;x<urls.size();x++){
			bestLinks.add(new ArrayList<String>());
			otherLinks.add(new ArrayList<String>());
		}
		
		/*
		 * heuristic_word and heuristic_base_links must have the same number of line, they also need to have an "one to one relation"
		 */
		
		words = new HashSet<String>();
		File file = new File("heuristic_words");
		Scanner in = new Scanner(file);
		while(in.hasNextLine()){
			words.add(in.nextLine());
		}
		
		baseLinks = new ArrayList<String>();
		file = new File("heuristic_base_links");
		in = new Scanner(file);
		while(in.hasNextLine()){
			baseLinks.add(in.nextLine());
		}
		
		
		for(String url : urls){
			addLink(url);
			usedLinks.add(url);
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
			for(int x=0;x < bestLinks.size();x++){
				if(link.contains(baseLinks.get(x))){
					bestLinks.get(x).add(link);
				}
			}
		}else{
			for(int x=0;x < otherLinks.size();x++){
				if(link.contains(baseLinks.get(x))){
					otherLinks.get(x).add(link);
				}
			}
		}
		
	}

	@Override
	public int linksNumber() {
		return bestLinks.size() + otherLinks.size(); 
	}

	
	@Override
	public String removeNextLink() {
		ArrayList<Integer> options = new ArrayList<Integer>();
		
		for(int x=0;x<baseLinks.size();x++){
			if(bestLinks.get(x).size() + otherLinks.get(x).size() > 0) options.add(x);
		}
		
		if(options.size() != 0){
			int op = rand.nextInt(options.size()); 
			
			return removeNextLink(bestLinks.get(options.get(op)), otherLinks.get(options.get(op)));
		}else{
			return "";
		}

	}

	public String removeNextLink(ArrayList<String> best, ArrayList<String> others){
		
		if(best.size() > 0 && others.size() > 0){
			if(rand.nextInt(100) <= 80){
				return best.remove(0);
			}else{
				return others.remove(0);
			}
		}else if(best.size() > 0){
			return best.remove(0);
		}else{
			return others.remove(0);
		}
	}
	
}
