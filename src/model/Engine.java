package model;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;


public class Engine
{
	private static Engine instance = null;
	private final String geoURL = "https://maps.googleapis.com/maps/api/geocode/xml";
	private final String privateKey = "AIzaSyAW7Q93Kv2b1_eSuKeDvwc-0msWIHF24P0";
	private final String distanceMatrixURL = "https://maps.googleapis.com/maps/api/distancematrix/xml";
	private static String dbURL = "jdbc:derby://red.eecs.yorku.ca:64413/EECS;user=student;password=secret";;

	private Engine()
	{
	}

	public synchronized static Engine getInstance()
	{
		if (instance == null) instance = new Engine();
		return instance;
	}
	
	
	public String nextPrime(String min, String max) throws Exception {
		int minInt = Integer.parseInt(min);
		int maxInt = Integer.parseInt(max);
		if (minInt >= maxInt - 1)
			throw new Exception("No more prime in between these");
		
		BigInteger integer = new BigInteger(min);
		String ans = integer.nextProbablePrime().toString();
		
		if (Integer.parseInt(ans) >= maxInt) 
			throw new Exception("No more prime between these two numbers");
		
		return ans;
	}	
	
	public String getDroneDistance(String firstAddress, String desAddress) throws Exception {
		String firstXML = getResponse(String.format(geoURL + "?address=%s&key=%s",
				URLEncoder.encode(firstAddress, "UTF-8"), privateKey));
		String desXML = getResponse(String.format(geoURL + "?address='%s'&key=%s", 
				URLEncoder.encode(desAddress, "UTF-8"), privateKey));
		
		ArrayList<String> first = getGeoLocationFromXML(firstXML);
		ArrayList<String> des = getGeoLocationFromXML(desXML);
		
		
		return getDistance(first.get(0), first.get(1), des.get(0), des.get(1));
		
	}
	
	
	public String getTravelCost(String from, String to) throws Exception {
		String response = getResponse(String.format(distanceMatrixURL + "?origins=%s&destinations=%s&departure_time=now&key=%s",
						URLEncoder.encode(from, "UTF-8"), URLEncoder.encode(to, "UTF-8"), privateKey));
		
		String travelDuration = getTravelDuration(response);
		String[] dur = travelDuration.split(" ");
		return String.format("%.2f", Integer.parseInt(dur[0]) * .5);
		
	}
	
	public List<StudentBean> getSIS(String prefix,  String sortBy, String minGpa) throws Exception
	{
		StudentDAO studentDao = new StudentDAO(prefix, sortBy, minGpa);
		return studentDao.retreive();
	}
	
	
	public String getTravelDuration(String xmlString) throws Exception {
		
		Document document = getDocument(xmlString);
		
		NodeList rootElement = document.getElementsByTagName("duration");
		NodeList childNodes = rootElement.item(0).getChildNodes();
		
		for (int j = 0; j < childNodes.getLength(); j++) {
			//Extract log and lat
			boolean isMins = childNodes.item(j).getTextContent().contains("mins");
			if (isMins)
				return childNodes.item(j).getTextContent().trim();
		}
		
		
		throw new Exception("Did not find the duration in the xml string");
	
	}
	
	
		
		public ArrayList<String> getGeoLocationFromXML(String xmlString) throws Exception {
			
			Document document = getDocument(xmlString);
			
			NodeList rootElement = document.getElementsByTagName("location");
			NodeList childNodes = rootElement.item(0).getChildNodes();
			
			String lat = "";
			String lng = "";
			for (int j = 0; j < childNodes.getLength(); j++) {
				//Extract log and lat
				boolean isLat = childNodes.item(j).toString().contains("lat");
				boolean isLng = childNodes.item(j).toString().contains("lng");
				if (isLat) {
					 lat = childNodes.item(j).getTextContent().trim();
					 System.out.println(childNodes.item(j).getTextContent().trim());
				}
				else if (isLng) {
					lng =childNodes.item(j).getTextContent().trim();
					 System.out.println(childNodes.item(j).getTextContent().trim());
				}
			
			}
			
	
			
			if (lat.equals("") || lng.equals("") )
				throw new Exception("Did not find lng or lat in the xml string");
			
			ArrayList<String> ans = new ArrayList<>();
			ans.add(lng);
			ans.add(lat);
			return ans;
		
		
	}
		
	public Document getDocument(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlString)));
	}
	
	public String getResponse(String fullUrl) throws Exception {
		URL url = new URL(fullUrl);
		HttpURLConnection  connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
	    
	    Scanner in = new Scanner(connection.getInputStream());
	    
	     StringBuffer response = new StringBuffer();
	     int i = 0;
	     while ( in.hasNext())  {
	    	 String ans = in.nextLine();
	     	if (i != 0)
	     		response.append(ans);
	     	i++;
	     }
	     
	     in.close();
	
		
		return response.toString();
	}
	
	
	public String getDistance(String lon1Str, String lat1Str, String lon2Str, String lat2Str) {
		 
				float lon1 = Float.parseFloat(lon1Str);
				float lon2 = Float.parseFloat(lon2Str);
				float lat1 = Float.parseFloat(lat1Str);
				float lat2 = Float.parseFloat(lat2Str);
				
				final int R = 6371; // Radius of the earth

			    double latDistance = Math.toRadians(lat2 - lat1);
			    double lonDistance = Math.toRadians(lon2 - lon1);
			    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
			    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			    double distance = R * c; // convert to meters

			    

			    return  String.format("%.2f", distance).toString() + " KMS";
	}

	

}
