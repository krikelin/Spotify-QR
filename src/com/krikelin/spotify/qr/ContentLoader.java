package com.krikelin.spotify.qr;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class ContentLoader {
	
	
	public String GetValue(Element D,String Tag,String DefaultValue)
	{
		String val = D.getElementsByTagName(Tag).item(0).getChildNodes().item(0).getNodeValue();
		return val != "" ? val : DefaultValue;
	}
 
	
	public List<Album> Albums = new ArrayList<Album>();
	public List<Album> FetchContent(String query) throws ParserConfigurationException, SAXException, IOException
	{
		query = URLEncoder.encode(query, "ISO-8859-1");
		List<Album> albums = new ArrayList<Album>();
		DocumentBuilderFactory _Factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder X = _Factory.newDocumentBuilder();
	
		URL D = new  URL(String.format("http://ws.spotify.com/search/1/album?q=%s",query));
		try {
			Albums = new ArrayList<Album>();
			Document Ddf = X.parse(new InputSource(D.openStream()));
			Ddf.getDocumentElement().normalize();
			
			Albums = new ArrayList<Album>();
			NodeList d = Ddf.getElementsByTagName("album");
		
			for(int i=0; i < d.getLength(); i++)
			{
				Element _Elm = (Element)d.item(i);
				/**
				 * 
				 * Popularity generated:
				 * Pop = plays = / COUNT_SONGS 
				 * Month = 
				 */
				Album CurrentAlbum = new Album();
				CurrentAlbum.setName(GetValue(_Elm,"name",""));
				CurrentAlbum.setArtist(GetValue((Element)((Element)_Elm.getElementsByTagName("artist").item(0)).getElementsByTagName("name").item(0),"name",""));
				CurrentAlbum.setUri(_Elm.getAttribute("href"));
				
				albums.add(CurrentAlbum);
			}
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.Albums = albums;
		return albums;
		
	}
}
