package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Robot {

	private Set<String> disallowSet;

	public Robot(ArrayList<String> urls, ArrayList<String> robotUrls) {
		disallowSet = new HashSet<String>();

		String url;
		int index = 0;
		for(String robotUrl : robotUrls){
			url = urls.get(index++);
			if(!robotUrl.equals("NULL")){
				try {
					InputStreamReader sr = new InputStreamReader(new URL(robotUrl).openStream());
					BufferedReader bf = new BufferedReader(sr);

					String line;
					String[] split;
					while((line = bf.readLine()) != null){
						split = line.split(" ");
						if(split != null && split.length>=2){
							if(split[0].equals("Disallow:")){
								disallowSet.add(url + split[1].substring(1));
								//System.out.println(url + split[1].substring(1));
							}
						}

						line = null;
						split = null;
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}

	}

	public boolean disallow(String url){
		return disallowSet.contains(url);
	}

}