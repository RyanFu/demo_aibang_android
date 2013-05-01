package com.sssta.demo_aibang_android;

import java.io.IOException;

import com.sssta.demo_aibang_android.R.string;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private String resultString = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	SimpleListFragment simpleListFragment = new SimpleListFragment();
	//	getSupportFragmentManager().beginTransaction().replace(R.id.container, simpleListFragment).commit();
		TextView testTextView =(TextView) findViewById(R.id.test_testview);
		
		 new Thread(new Runnable() {
		        public void run() {
		        try {
					Client client = new Client();
		        	resultString = client.ReceiveFromServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		    }).start();
		 testTextView.setText(resultString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
