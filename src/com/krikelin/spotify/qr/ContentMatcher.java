 package com.krikelin.spotify.qr;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.os.AsyncTask;

/**
 * Matches an content by their ISRC/UPC number
 * @author Alexander
 *
 */
public class ContentMatcher  extends AsyncTask<String,String,String>{
	public interface ContentEventHandler {
		public void onFinish(String uri);
	}
	private ContentEventHandler onFinish;
	public ContentEventHandler getOnFinish() {
		return onFinish;
	}
	public void setOnFinish(ContentEventHandler onFinish) {
		this.onFinish = onFinish;
	}
	@Override
	protected String doInBackground(String... params) {
		if(params[0].startsWith("spotify:")){
			return params[0];
		}
		DocumentBuilder X;
		try {
			
				String fx = "00" + params[0];
			
			X = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			URL D = new URL("http://ws.spotify.com/search/1/album?q=upc:"+params[0]);
			Document Ddf2 = X.parse(new InputSource(D.openStream()));
			NodeList songs = Ddf2.getElementsByTagName("album");
			if(songs.getLength() > 0){
				String URI = ((Element)songs.item(0)).getAttribute("href");
				return URI;
			}
			D = new URL("http://ws.spotify.com/search/1/album?q=upc:"+fx);
			Ddf2 = X.parse(new InputSource(D.openStream()));
			songs = Ddf2.getElementsByTagName("album");
			if(songs.getLength() > 0){
				String URI = ((Element)songs.item(0)).getAttribute("href");
				return URI;
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
		try {
			
			X = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			URL D = new URL("http://api.discogs.com/search?q="+params[0]+"&f=xml");
			Document Ddf2 = X.parse(new InputSource(D.openStream()));
			NodeList nl = Ddf2.getElementsByTagName("title");
			Element c =(Element)nl.item(0); 
			String title = (c).getFirstChild().getNodeValue();
			
			// Split track name
			String title_ = title.split(" - ",2)[1].trim();
			String artist_ = title.split(" - ",2)[0].trim();
			 D = new URL("http://ws.spotify.com/search/1/album?q=album:\""+URLEncoder.encode(title_).replace("+","%20")+"\" artist:\""+URLEncoder.encode(artist_).replace("+","%20")+"\"	");
			 X = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Ddf2 = X.parse(new InputSource(D.openStream()));
			NodeList songs = Ddf2.getElementsByTagName("album");
			for(int i=0; i < songs.getLength(); i++){
				Element song_ = (Element)songs.item(i);
				String _title = ((Element)(song_)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				String _artist = ((Element)((Element)(song_)).getElementsByTagName("artist").item(0)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				_title = _title.replace(":", "");
				title_ = title_.replace(":", "");
				_title = _title.replace("-", "");
				title_ = title_.replace("-", "");
				
				if(_title.equals(title_) && _artist.equals(artist_)){
					String URI = ((Element)song_).getAttribute("href");
					return URI;
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		try {
			X = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			URL D = new URL("http://ws.spotify.com/search/1/track?q=isrc:"+params[0]);
			Document Ddf2 = X.parse(new InputSource(D.openStream()));
			NodeList songs = Ddf2.getElementsByTagName("track");
			if(songs.getLength() > 0){
				String URI = ((Element)songs.item(0)).getAttribute("href");
				return URI;
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(getOnFinish()!= null){
			getOnFinish().onFinish((result));
			
		}
	}
	
}
