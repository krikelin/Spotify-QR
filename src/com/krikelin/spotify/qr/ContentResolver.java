package com.krikelin.spotify.qr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
public class ContentResolver  extends AsyncTask<String,String,String>{
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
		// TODO Auto-generated method stub
		DocumentBuilder X;
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
		try {
			X = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			URL D = new URL("http://ws.spotify.com/search/1/album?q=upc:"+params[0]);
			Document Ddf2 = X.parse(new InputSource(D.openStream()));
			NodeList songs = Ddf2.getElementsByTagName("album");
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
