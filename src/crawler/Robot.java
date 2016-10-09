package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Robot {

	private Set<String> disallowSet;

	public Robot(String base, String robotURL) throws MalformedURLException, IOException{
		disallowSet = new HashSet<String>();
		
				
		InputStreamReader sr = new InputStreamReader(new URL(robotURL).openStream());		
		BufferedReader bf = new BufferedReader(sr);
		
		String line;
		String[] split;
		while((line = bf.readLine()) != null){
			split = line.split(" ");
			if(split != null && split.length>=2){
				if(split[0].equals("Disallow:")){
					disallowSet.add(base + split[1]);
				}
			}
			
			line = null;
			split = null;
		}
		
	}
	
	public boolean disallow(String url){
		return disallowSet.contains(url);
	}
	
}