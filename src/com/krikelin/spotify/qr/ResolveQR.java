package com.krikelin.spotify.qr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.ListActivity;
import android.app.SearchManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ResolveQR extends ListActivity {
	public static final String WEB_SERVICE_BASE_URL = "http://cobresia.webfactional.com/spotiqr/";
	public class SearchTask extends AsyncTask<String,String,List<Album>>{

		@Override
		protected List<Album> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ContentLoader c= new ContentLoader();
			
			try {
				return c.FetchContent(params[0]);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ArrayList<Album>();
		}

		@Override
		protected void onPostExecute(List<Album> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setContentView(R.layout.list_activity);
			if(result.size() > 0)
			{
				setListAdapter(new AlbumAdapter(result,ResolveQR.this, 0));
			
				getListView().setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Album album = (Album)arg0.getItemAtPosition(arg2);
						Long ean = -1l;
						if(getIntent().hasExtra("ean")){
							ean = getIntent().getLongExtra("ean", 0);
						}else if (ResolveQR.ean != -1){
							ean = ResolveQR.ean;
						}
						if(ean == 0){
							throw new IllegalArgumentException();
						}
						// Now add them to the database
						String putString = ResolveQR.WEB_SERVICE_BASE_URL+"add/?ean=%s&href=%s";
						String GETData = "";
						try {
							GETData = String.format(putString,String.valueOf(ean).replace(".",""),URLEncoder.encode(album.getUri(),"ISO-8859-1"));
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							Log.e("A",GETData);
							new URL(GETData).openStream();
							
						} catch (Exception e){
							throw new IllegalArgumentException("Error sending data");
						}
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(album.getUri()));
						startActivity(i);
						finish();
						
					}
				}); 
			}
			else
			{
				setContentView(R.layout.no_match);
				Button l = (Button)findViewById(R.id.btnFind);
				l.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ResolveQR.ean = getIntent().getLongExtra("ean", -1);
						onSearchRequested();
					}
				});
			}
		}
		
	}
	public static Long ean = -1l;
	public class AlbumAdapter extends ArrayAdapter<Album>{
		private List<Album> albums;
		public AlbumAdapter(List<Album> albums, Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.albums = albums;
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return albums.size();
		}

		@Override
		public Album getItem(int position) {
			// TODO Auto-generated method stub
			return albums.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			// Get album
			Album album = albums.get(position);
			// Create view if not recycled
			if(convertView == null){
				LayoutInflater li = (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.list_entry, null);
				
			}
			TextView tvName = (TextView)convertView.findViewById(R.id.tvTitle);
			TextView tvText = (TextView)convertView.findViewById(R.id.tvText);
			tvName.setText(album.getName());
			tvText.setText(album.getArtist());
			
			return convertView;
			
		}
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		if(getIntent().getAction().equals(Intent.ACTION_SEARCH)){
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			
			SearchTask st = new SearchTask();
			st.execute(query);
			
		}
	}
	
}
