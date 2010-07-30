package com.cellcity.citiguide.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.cellcity.citiguide.screen.TwitterScreen;

public class BitmapLoader implements Runnable {
	private Activity act;
	private String id;
	private String url;
	
	public BitmapLoader(Activity act, String id, String url){
		this.act = act;
		this.id = id;
		this.url = url;
	}
	
	public void start(){
		if(url != null && url.startsWith("http://")){
			Thread t = new Thread(this, id);
			t.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		final String myId = this.id;
		String myurl = url;
		Bitmap bitmap = null;
		int timeOutMS = 6000;
		try {
			HttpParams my_httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(my_httpParams, timeOutMS);
			HttpConnectionParams.setSoTimeout(my_httpParams, timeOutMS);
			// HttpClient httpClient = new DefaultHttpClient(my_httpParams); //

			DefaultHttpClient client = new DefaultHttpClient(my_httpParams);
			URI uri = new URI(myurl);
			HttpGet method = new HttpGet(uri);
			HttpResponse res = client.execute(method);
			InputStream data = res.getEntity().getContent();
			// websiteData = generateString(data);

			bitmap = BitmapFactory.decodeStream(data);
			
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return ;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
//		notifyContext();
		
		if(bitmap != null){
			if(act instanceof TwitterScreen){
//				bitmap = Util.resizeImage(bitmap, 125, 92);
				final Bitmap myBitmap = bitmap;
				act.runOnUiThread(new Runnable() {
				
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HashMap hm = TwitterScreen.imageHash;
						ImageView adapter = (ImageView) hm.get(myId);
						if(myBitmap != null){
							adapter.setImageBitmap(myBitmap);
							adapter.invalidate();
						}
					}
				});
			}
			
		}
	}

}

