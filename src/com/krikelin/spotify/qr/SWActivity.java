package com.krikelin.spotify.qr;


import com.google.zxing.integration.android.IntentIntegrator;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;

public class SWActivity extends ListActivity implements com.krikelin.spotify.qr.ContentMatcher.ContentEventHandler {
	ProgressDialog pd;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == IntentIntegrator.REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	      // String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            // Handle successful scan
	            pd = ProgressDialog.show(this, "", "Matching barcode with Spotify");
	            ContentMatcher cm = new ContentMatcher();
	            cm.setOnFinish(this);
	            cm.execute(contents);
	            
	           
	        } else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        	finish();
	        }
	    }
	}
	@Override
	public void onFinish(String inputData, String uri,String predictArtist,String predictAlbum) {
		// TODO Auto-generated method stub
		try{
			pd.cancel();
		}catch(Exception e){
			
		}
		if(uri != null){
			Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
			startActivity(i);
		}else{
			
			//Toast t = Toast.makeText(this, "Inget innehåll matchades", 3000);
			//t.show();
			// Redirect the user to the Spotify QR page
			Intent i = new Intent(SWActivity.this,ResolveQR.class);
			i.putExtra("ean",Long.valueOf(inputData));
			i.putExtra(SearchManager.QUERY, predictArtist + " " + predictAlbum);
			i.setAction(Intent.ACTION_SEARCH);
			startActivity(i);
		}
		finish();
		
	}

}
