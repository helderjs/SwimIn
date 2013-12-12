package com.ufba.swimin;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TabHost;

public class Ranking  extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TabHost tabhost = (TabHost)findViewById(R.id.tab_ranking);
        tabhost.setup();

        TabHost.TabSpec spec1 = tabhost.newTabSpec("Craw");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Craw");

        TabHost.TabSpec spec2 = tabhost.newTabSpec("Peito");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Peito");

        TabHost.TabSpec spec3 = tabhost.newTabSpec("Costa");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Costa");

        TabHost.TabSpec spec4 = tabhost.newTabSpec("Borboleta");
        spec4.setContent(R.id.tab4);
        spec4.setIndicator("Borboleta");

        tabhost.addTab(spec1);
        tabhost.addTab(spec2);
        tabhost.addTab(spec3);
        tabhost.addTab(spec4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}