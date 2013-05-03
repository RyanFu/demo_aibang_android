package com.sssta.demo_aibang_android;

import java.io.IOException;

import com.sssta.demo_aibang_android.R.string;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private String resultString = null;
	private TextView testTextView;
	public static final String ipAddress = "192.168.1.135"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	SimpleListFragment simpleListFragment = new SimpleListFragment();
	//	getSupportFragmentManager().beginTransaction().replace(R.id.container, simpleListFragment).commit();
		testTextView =(TextView) findViewById(R.id.test_testview);
		/*
		 *使用post的方式把对ui tookit的控制加入到主线程
		 */
		 /*new Thread(new Runnable() {
		        public void run() {
		        try {
					Client client = new Client();
		        	resultString = client.ReceiveFromServer();
		        
		        	if(resultString!=null)
		        	Log.i("resultString", resultString);
		        	else {
		        		Log.i("resultString", "null");
					}
		        	testTextView.post( new  Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							testTextView.setText(resultString);
						}
					});
		        	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  
		        }
		    }).start();*/
		new DownloadTask().execute();
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*
	 * 使用asnyctask方式进行线程处理
	 */
	private class DownloadTask extends AsyncTask<Void, integer, String>
	{
		
		
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
	        testTextView.setText(result);
	     }
			
		}
		
	}

