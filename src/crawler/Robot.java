package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.utils.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;

public class Robot {

	private static HashMap<String, BaseRobotRules> urlRobotMap = new HashMap<String, BaseRobotRules>();
	
	private static int robotNameAux = 0;
	
	public static BaseRobotRules parseRobots(String host){
        try{
            while(host.endsWith("/")) host = host.substring(0, host.length() - 1);
 
            URI uri = new URI(host + "/robots.txt");
            
            String content = uri.toURL().toString();
           
            SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
           
            return robotParser.parseContent(uri.toString(), content.getBytes(), "text/plain", "robot" + (robotNameAux++));
        }catch(Exception e){
            return null; 
        }
       
    }
   
    public static boolean isAllowed(String url){
        try {
            URL uri = new URL(url);
           
            BaseRobotRules rules = null;
            if(urlRobotMap.containsKey(uri.getHost())){
                rules = urlRobotMap.get(uri.getHost());
            }else{
                rules = Robot.parseRobots(uri.getProtocol() + "://" + uri.getHost());
                urlRobotMap.put(uri.getHost(), rules);
            }
           
            if(rules == null) return true;
            return rules.isAllowed(url);
           
        } catch (Exception e) {
            e.printStackTrace();
            return true; 
        }
    }

}