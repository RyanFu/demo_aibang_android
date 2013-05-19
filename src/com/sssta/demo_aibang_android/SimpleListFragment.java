package com.sssta.demo_aibang_android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;


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
	public Context context; // 存储上下文对象  
    public Activity activity; // 存储上下文对象  
	private ImageLoader imageLoader;
	private DisplayImageOptions options ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		 context = getActivity();  
	       activity = getActivity();
	   /*
	    * 加载 universe-image-loader 设置
	    */
	   	File cacheDir = StorageUtils.getOwnCacheDirectory(context, "UniversalImageLoader/Cache");
		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Create configuration for ImageLoader (all options are optional, use only those you really want to customize)
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		            .memoryCacheExtraOptions(480, 800) // max width, max height
		            .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) // Can slow ImageLoader, use it carefully (Better don't use it)
		            .threadPoolSize(3)
		            .threadPriority(Thread.NORM_PRIORITY - 1)
		            .denyCacheImageMultipleSizesInMemory()         
		            .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation
		            .discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
		            .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		            .enableLogging()
		            .build();
		
		// Initialize ImageLoader with created configuration. Do it once on Application start.
		imageLoader.init(config);
		// Creates display image options for custom display task (all options are optional)
		options = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        .displayer(new FakeBitmapDisplayer())
        .build();
		//universe-image-loader 设置结束
		
		//启动新线程
       new DownloadTask().execute();		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.simple_list_fragment, container, false);
	}
	/**
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
		  /*
		   * 解析返回的json数据，读取到list中
		   */
			 Log.i("post", "success");
			 Log.e("receive", result);
			 JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(result);
					Log.i("jsonanalysis", "success");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					places = place.JsonToPlaceList(jsonObject.getJSONArray("biz"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//json解析结束，数据已位于places中
			 /*
			  * 设置adapter
			  */
			 ItemAdapter adapter = new  ItemAdapter();
			 setListAdapter(adapter);
	        //testTextView.setText(result);
	     }
			
		}
	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text_name;
			public TextView text_addr;
			public TextView text_tel;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return places.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = activity.getLayoutInflater().inflate(R.layout.listitem_main, parent, false);
				holder = new ViewHolder();
				holder.text_name = (TextView) view.findViewById(R.id.place_name);
				holder.text_addr = (TextView)view.findViewById(R.id.place_addr);
				holder.text_tel =  (TextView)view.findViewById(R.id.place_tel);
				holder.image = (ImageView) view.findViewById(R.id.place_pic);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.text_name.setText(places.get(position).getName());
			holder.text_addr.setText(places.get(position).getAddr());
			holder.text_tel.setText(places.get(position).getTel());
			
			ImageLoader.getInstance().displayImage(places.get(position).getImg_url(), holder.image);
			return view;
			
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
