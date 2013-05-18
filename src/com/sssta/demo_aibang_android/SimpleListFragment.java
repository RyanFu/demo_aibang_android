package com.sssta.demo_aibang_android;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FakeBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class SimpleListFragment extends android.support.v4.app.ListFragment{
	
	private List<place> places;
	private String jsonString;
//	private ListView listView;
	private List<Map<String, Object>> mData;
	public Context context; // 存储上下文对象  
    public Activity activity; // 存储上下文对象  
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		 context = getActivity();  
	       activity = getActivity();
        //  new DownloadTask().execute();	
       
		/**
		 * 解析返回的json数据，读取到list中
	*/
         
	
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.simple_list_fragment, container, false);
	}
	public List<place> AnalysisJson (String jsonString)
	{
		List<place> places_temp = null; 
		JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(jsonString);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				places_temp = place.JsonToPlaceList(jsonObject.getJSONArray("biz"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return places_temp;
		
	}
	/*
	 * 使用asnyctask方式进行线程处理
	 */
	private class DownloadTask extends AsyncTask<Void, integer, String>
	{
		
		String resultString;
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Client client = new Client();
        	try {
				resultString = client.ReceiveFromServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultString;
			
		}
		 protected void onPostExecute(String result) {
			 jsonString= result;
			 Log.i("post", "success");
			 Log.e("receive", jsonString);
			 places = AnalysisJson(jsonString);
			 for(place tempPlace : places)
				{
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", tempPlace.getName());
					map.put("addr", tempPlace.getAddr());
					map.put("tel", tempPlace.getTel());
					map.put("image_url", tempPlace.getImg_url());
					 mData.add(map);
				}
			 
	        //testTextView.setText(result);
	     }
			
		}
}
