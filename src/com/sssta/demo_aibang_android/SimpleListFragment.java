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
	private String jsonString;
//	private ListView listView;
	private List<Map<String, Object>> mData=null;
	public Context context; // 存储上下文对象  
    public Activity activity; // 存储上下文对象  
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		 context = getActivity();  
	       activity = getActivity();
	   
       new DownloadTask().execute();	
       
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
			 JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(jsonString);
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
			mData = new ArrayList<Map<String, Object>>();
			 Log.i("receive1", places.get(0).getAddr());
			 for(place tempPlace : places)
				{
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", tempPlace.getName());
					Log.i("name", tempPlace.getName());
					map.put("addr", tempPlace.getAddr());
					Log.i("addr", tempPlace.getAddr());
					map.put("tel", tempPlace.getTel());
					Log.i("tel",tempPlace.getTel());
					map.put("image_url", tempPlace.getImg_url());
					Log.i("image_url",tempPlace.getImg_url());
					Log.i("map is empty", String.valueOf(map.isEmpty()));
					mData.add(map);
				}
			 SimpleAdapter mSchedule = new SimpleAdapter(activity, 
						mData,
						R.layout.listitem_main,// ListItem的XML实现
						new String[] { "name", "addr","tel" },
						//TextView ID
						new int[] { R.id.place_name, R.id.place_addr,R.id.place_tel });
			setListAdapter(mSchedule);
	        //testTextView.setText(result);
	     }
			
		}
	class ImageSimpleAdapter extends SimpleAdapter {
		private int[] mTo;
		private String[] mFrom;
		private ViewBinder mViewBinder;
		private List<? extends Map<String, ?>> mData;
		private int mResource;
		private int mDropDownResource;
		private LayoutInflater mInflater;

		public ImageSimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource, String[] from,
				int[] to) {
			super(context, data, resource, from, to);
			mTo = to;
			mFrom = from;
			mData = data;
			mResource = mDropDownResource = resource;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent, mResource);
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent,
					mDropDownResource);
		}

		private View createViewFromResource(int position, View convertView,
				ViewGroup parent, int resource) {
			View v;
			if (convertView == null) {
				v = mInflater.inflate(resource, parent, false);
			} else {
				v = convertView;
			}

			bindView(position, v);

			return v;
		}

		private void bindView(int position, View view) {
			final Map dataSet = mData.get(position);
			if (dataSet == null) {
				return;
			}

			final ViewBinder binder = mViewBinder;
			final String[] from = mFrom;
			final int[] to = mTo;
			final int count = to.length;

			for (int i = 0; i < count; i++) {
				final View v = view.findViewById(to[i]);
				if (v != null) {
					final Object data = dataSet.get(from[i]);
					String text = data == null ? "" : data.toString();
					if (text == null) {
						text = "";
					}

					boolean bound = false;
					if (binder != null) {
						bound = binder.setViewValue(v, data, text);
					}

					if (!bound) {
						if (v instanceof Checkable) {
							if (data instanceof Boolean) {
								((Checkable) v).setChecked((Boolean) data);
							} else if (v instanceof TextView) {
								// Note: keep the instanceof TextView check at the
								// bottom of these
								// ifs since a lot of views are TextViews (e.g.
								// CheckBoxes).
								setViewText((TextView) v, text);
							} else {
								throw new IllegalStateException(v.getClass()
										.getName()
										+ " should be bound to a Boolean, not a "
										+ (data == null ? "<unknown type>"
												: data.getClass()));
							}
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at the
							// bottom of these
							// ifs since a lot of views are TextViews (e.g.
							// CheckBoxes).
							setViewText((TextView) v, text);
						} else if (v instanceof ImageView) {
							if (data instanceof Integer) {
								setViewImage((ImageView) v, (Integer) data);
							} else if (data instanceof Bitmap) {// ���������һ��
								setViewImage((ImageView) v, (Bitmap) data);
							} else {
								setViewImage((ImageView) v, text);
							}
						} else {
							throw new IllegalStateException(
									v.getClass().getName()
											+ " is not a view that can be bounds by this SimpleAdapter");
						}
					}
				}
			}
		}

		/**
		 * ����������������Bitmap���Ͳ���
		 * 
		 * @param v
		 * @param bitmap
		 */
		public void setViewImage(ImageView v, Bitmap bitmap) {
			v.setImageBitmap(bitmap);
		}
	}
}
