package com.cellcity.citiguide.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cellcity.citiguide.info.MerchantInfo1;
import com.cellcity.citiguide.parser.ParsedDataSet;
import com.cellcity.citiguide.parser.XMLParserHandler;
import com.cellcity.citiguide.screen.ListingMerchantScreen;
import com.cellcity.citiguide.screen.R;

public class Util {
	public static void showAlert(Activity act,
			final String title, final String msg, final String buttontext,
			final boolean finishScreen) {
		final Activity myact = act;
		try {
			act.runOnUiThread(new Runnable() {
				public void run() {
					AlertDialog alertDialog = new AlertDialog.Builder(myact)
							.create();
					alertDialog.setTitle(title);
					alertDialog.setMessage(msg);
					alertDialog.setButton(buttontext,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (finishScreen) {
										myact.finish();
									}
									return;
								}
							});
					alertDialog.show();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}	
	
	public static String getGPSAddress(Activity context, double lat, double lng){
		String addressName = "";
		Geocoder geoCoder = new Geocoder(context.getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
			String add = "";
			if (addresses.size() > 0) {
				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
					add += addresses.get(0).getAddressLine(i) + " ";
			}
			addressName = add;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(addressName);
		return addressName;
	}
	
	public static void makeCall(Context context, String number) {
		if(!number.startsWith("tel:"))
			number = "tel:" + number;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(number));
		context.startActivity(intent);
//		context.startActivity(Intent.createChooser(intent, "Send mail..."));
	}
	
	public static Bitmap loadImage(Context context, String name, boolean buffer) {
		Bitmap toReturn;
		try {
			int id = R.drawable.class.getField(name).getInt(new Integer(0));
			toReturn = BitmapFactory.decodeResource(context.getResources(), id);
		} catch (Exception ee) {
			System.out.println("Could not load:" + name);
			toReturn = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon);
		}

		return toReturn;
	}
	
	public static String toJSONString(String result){
		if(result.startsWith("("))
			return result.substring(1);
		if(!result.startsWith("{")){
			int index = result.indexOf("{");
			return result.substring(index);
		}
		
		else
			return result;
	}
	public static int getScreenWidth(Context c){
		Display display = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		return display.getWidth(); 
	}
	
	public static int getScreenHeight(Context c){
		Display display = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		return display.getHeight(); 
	}
	public static void showKeyboard(Context context){
		// force show keyboard
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.SHOW_IMPLICIT);
	}
	
	public static void sendEmail(Context context, String sender, String receiver, 
			String subject, String message){
//		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", receiver, null));
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject); 
//        intent.putExtra(Intent.EXTRA_TEXT, message); 
//        context.startActivity(intent); 
	
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
		emailIntent.setType("plain/text"); 
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{receiver}); 
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject); 
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message); 
		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
	public static void sendMessage(Context context, String address, String message) {
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms://"));
		sendIntent.putExtra("address", address);
		sendIntent.putExtra("sms_body", message);
		context.startActivity(sendIntent);
	}
	
	// http request
	public static String getHttpData(String url) {
		System.out.println("getUrlData ======================>>> " + url);
		String result = null;
		int timeOutMS = 1000*30;
		try {
			HttpParams my_httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(my_httpParams, timeOutMS);
			HttpConnectionParams.setSoTimeout(my_httpParams, timeOutMS);
			// HttpClient httpClient = new DefaultHttpClient(my_httpParams); //

			DefaultHttpClient client = new DefaultHttpClient(my_httpParams);
			URI uri = new URI(url);
			HttpGet httpGetRequest = new HttpGet(uri);
			HttpResponse response = client.execute(httpGetRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				response.getEntity().writeTo(os);
				result = os.toString();
			}

		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return ERROR_CODE_TIME_OUT;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return ERROR_CODE_UNKNOW_HOST;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	public static final String ERROR_CODE_TIME_OUT = "408";
	public static final String ERROR_CODE_UNKNOW_HOST = "404";
	public static String errorCode = "000";
	
	public static ArrayList<MerchantInfo1> getMerchants(String query) {
		ArrayList<MerchantInfo1> merchants = null;
		
		System.out.println(query);
		
		try {
			URL url = new URL(query);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XMLParserHandler myExampleHandler = new XMLParserHandler();
			xr.setFeature("http://xml.org/sax/features/namespaces", true);
			xr.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
			xr.setContentHandler(myExampleHandler);
			xr.parse(new InputSource(url.openStream()));
			ParsedDataSet parsedExampleDataSet = myExampleHandler.getParsedData();
			merchants = parsedExampleDataSet.getMerchants();
			
			ListingMerchantScreen.totalPage = merchants.size() / Constants.ITEMS_PER_PAGE;
			
			if(merchants.size() % Constants.ITEMS_PER_PAGE != 0) {
				ListingMerchantScreen.totalPage++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return merchants;
	}

	public static ArrayList<MerchantInfo1> getMerchantList(ArrayList<MerchantInfo1> merchantList, int page, int size) {
		ArrayList<MerchantInfo1> merchants = new ArrayList<MerchantInfo1>();
		int pageStop = page * 10;
		
		int i = 0;
		
		if(page != 1) {
			i = (page - 1) * 10;
		}
		
		for( ; i < pageStop && i < size; i++) {
			merchants.add(merchantList.get(i));
		}
		
		return merchants;
	}
	
	public static Bitmap getBitmap(String fileUrl) {
		Bitmap bmImg = null;
		URL myFileUrl = null;
		try {
			myFileUrl = new URL(fileUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			int length = conn.getContentLength();
			// int[] bitmapData = new int[length];
			// byte[] bitmapData2 = new byte[length];
			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmImg;
	}
	
	public static Bitmap resizeImage(Bitmap bitmapOrg, int width_, int height_) {
		Bitmap resizedBitmap = bitmapOrg;
		try {
			int width = bitmapOrg.getWidth();
			int height = bitmapOrg.getHeight();
			int newWidth = width_;
			int newHeight = height_;

			// calculate the scale - in this case = 0.4f
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			// create a matrix for the manipulation
			Matrix matrix = new Matrix();
			// resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);
			// rotate the Bitmap
			matrix.postRotate(0);

			// recreate the new Bitmap
			resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
					height, matrix, true);

			// make a Drawable from Bitmap to allow to set the BitMap
			// to the ImageView, ImageButton or what ever
			// BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
			//
			// ImageView imageView = new ImageView(context);
			//
			// // set the Drawable on the ImageView
			// imageView.setImageDrawable(bmd);
			//
			// // center the Image
			// imageView.setScaleType(ImageView.ScaleType.CENTER);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return resizedBitmap;
	}
	
	public static double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
		double distance = 0.0;
		
		//ACOS(SIN(lat1)*SIN(lat2)+COS(lat1)*COS(lat2)*COS(lon2-lon1))*6371
		distance = (Math.acos((Math.sin(lat1)) * (Math.sin(lat2)) + (Math.cos(lat1)) * (Math.cos(lat2)) * (Math.cos(lon2 - lon1)) ) * 6371) / 1000;
		
		return distance;
	}
	
	public static double convertLatLongToDist(Double lat1, Double lon1, Double lat2, Double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(degreesToRadian(lat1)) * Math.sin(degreesToRadian(lat2)) +
					  Math.cos(degreesToRadian(lat1)) * Math.cos(degreesToRadian(lat2)) *
					  Math.cos(degreesToRadian(theta));
		dist = Math.acos(dist);
		dist = radianToDegrees(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		
		return dist;
	}
	
	public static double degreesToRadian(double degree) {
		return (degree * Math.PI / 180.0);
	}
	
	public static double radianToDegrees(double radian) {
		return (radian / Math.PI * 180.0);
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		
		Bitmap output = null;
		
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(),
			bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
					 
			final int color = Color.WHITE;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 15;
			
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
					 
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return output;
	}
	
	public static List<Address> getLatLongOfAddress(Context context, String locationName) {
		List<Address> address = null;
		Geocoder geocoder = new Geocoder(context);
		try {
			address = geocoder.getFromLocationName(locationName, 1);
			System.out.println(locationName + " " + address.get(0).getLatitude() + "," + address.get(0).getLongitude());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return address;
	}
}
