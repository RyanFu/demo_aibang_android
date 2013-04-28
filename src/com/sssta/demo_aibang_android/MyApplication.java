package com.sssta.demo_aibang_android;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.view.Menu;

public class MyApplication extends Application {

	@Override
    public void onCreate() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .build();
        super.onCreate();
        ImageLoader.getInstance().init(config);
        
	}

	
}
