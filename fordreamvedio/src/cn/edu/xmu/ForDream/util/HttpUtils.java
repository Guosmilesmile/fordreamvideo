package cn.edu.xmu.ForDream.util;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class HttpUtils {
	public static final String BASE_URL = "http://172.16.21.15:8080/ForDream/";
	public static final String VIDEO_URL = "http://172.16.21.15:8080/ForDream/Video/";
	//public static final String VIDEO_URL = "http://192.168.95.1:8080/ForDream/Video/";
	//public static final String BASE_URL = "http://192.168.95.1:8080/ForDream/";
	
	public static HttpClient httpClient = new DefaultHttpClient();
	
	public HttpUtils() {
	}
	/**
	 * 从网络中获取图片信息，以流的形式返回
	 * @return
	 */
	public static InputStream getImageViewInputStream(String path ){
		InputStream inputStream = null;
		try {
			
			URL url = new URL(path);
			if(url!=null) {
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setConnectTimeout(3000);
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setDoInput(true) ;
				int resonpseCode = httpURLConnection.getResponseCode();
				if(resonpseCode == 200){
					inputStream = httpURLConnection.getInputStream();
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}
	/**
	 * 从网络中获取图片西悉尼，以字节数组的形式放回
	 * @return
	 */
	public static byte[] getImageViewArray(String path){
		byte [] data = null;
		InputStream inputStream = null;
//	不需要关闭的输出流，直接写入内存中。
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
try {
			
			URL url = new URL(path);
			if(url!=null) {
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setConnectTimeout(3000);
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setDoInput(true) ;
				int resonpseCode = httpURLConnection.getResponseCode();
				int len = 0;
				byte[] b_data = new byte[1024];
				if(resonpseCode == 200){
					inputStream = httpURLConnection.getInputStream();
					;
					while ((len =inputStream.read(b_data)) !=-1){
						outputStream.write(b_data, 0, len);
					}
					data = outputStream.toByteArray();
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(inputStream!=null){try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
		return data;
	}
	public static Drawable getImgDraw(String image_path) throws MalformedURLException, IOException
	{
		Drawable drawable=null;
		try {
			drawable = Drawable.createFromStream(new URL(image_path).openStream(), "");
			} 				
		  catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return drawable;
	}
	
	public static Bitmap returnBitmap(String url){
		URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
	}
	
	// get方法访问服务器，返回json字符串
		public static String getRequest(String url){
			String result=null;
			HttpGet httpGet = new HttpGet(url); 			
			try {
				Log.i("GetBegin",url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.i("GetOver",url);
					result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			return result;
			
		}
		
	        // 字符串转成集合数据
		public static void resultString2List(List<Map<String ,Object>> list, String str,String title) {
			try {
				JSONObject jsonObject = new JSONObject(str);
				JSONArray jsonArray = jsonObject.getJSONArray(title);
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					Map<String ,Object> map = new HashMap<String, Object>();
					Iterator<String> iterator = jsonObject2.keys();
					while (iterator.hasNext()) {
						String key = iterator.next();
						Object value = jsonObject2.get(key);
						map.put(key, value);
					}
					
					list.add(map);
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			//post方法访问服务器，返回集合数据
		public static List<Map<String,Object>> getRequest2List(String url,String title){
			
			List<Map<String,Object>> list = new ArrayList<Map<String ,Object>>();
			
			resultString2List(list, url, title);
		
			return list;
			
		}
		
		// post方法访问服务器，返回json字符串
		public static String postRequest(String url, Map<String,String> rawParams) throws Exception{
			Log.i("post begin",url);
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			for (String key:rawParams.keySet()) {
				params.add(new BasicNameValuePair(key, rawParams.get(key)));
				
			}
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			HttpResponse httpResponse = httpClient.execute(post);
			
			Log.i("Resopnse","Resopnse:"+httpResponse.getStatusLine().getStatusCode());
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("post success",url);
	                        return result;
			}
			Log.i("post fail",url);
			return null;
		}
		
		// post方法访问服务器，返回inputStream
		public static InputStream postRequestStream(String url, Map<String,String> rawParams) throws Exception{
			Log.i("post begin",url);
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			for (String key:rawParams.keySet()) {
				params.add(new BasicNameValuePair(key, rawParams.get(key)));
				
			}
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			HttpResponse httpResponse = httpClient.execute(post);
			
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				Log.i("post success",url);
				InputStream result = httpResponse.getEntity().getContent();
				
	                        return result;
			}
			Log.i("post fail",url);
			return null;
		}

}
