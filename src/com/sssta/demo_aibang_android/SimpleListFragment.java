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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
		/**
		 * uniserve-image-loader 初始化
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
		/**
		 *控件绑定 
		 */
	
		/**
		 * 数据初始化，从server获取数据
		 */
		try {
			jsonString = Client.ReceiveFromServer();
			Log.e("receive", jsonString);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		/**
		 * 解析返回的json数据，读取到list中
		 */
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
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
		Log.i("info", places.get(0).toString());
		/**
		 * ListAdapter will get info from dataArray and put it to the list
		 */
//		ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataArray);
//		setListAdapter(listAdapter);
		for(place tempPlace : places)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", tempPlace.getName());
			map.put("addr", tempPlace.getAddr());
			map.put("tel", tempPlace.getTel());
			map.put("image_url", tempPlace.getImg_url());
			 mData.add(map);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.simple_list_fragment, container, false);
	}
	
	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		/**
		 * Toast message will be shown when you click any list element
		 */
		Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
	}
	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
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
				holder.text = (TextView) view.findViewById(R.id.place_name);
				holder.image = (ImageView) view.findViewById(R.id.place_pic);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.text.setText("Item " + (position + 1));
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
