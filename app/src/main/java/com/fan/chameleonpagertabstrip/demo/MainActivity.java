package com.fan.chameleonpagertabstrip.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fan.chameleonpagertabstrip.ChameleonPagerTabStrip;

public class MainActivity extends AppCompatActivity {

	private ChameleonPagerTabStrip tabScript;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabScript = (ChameleonPagerTabStrip) findViewById(R.id.viewPagerTabStrip);
		viewPager = (ViewPager) findViewById(R.id.viewPager_demo);

		MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		tabScript.setViewPager(viewPager);
	}

}
