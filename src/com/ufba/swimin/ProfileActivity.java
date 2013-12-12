package com.ufba.swimin;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity {
    Button btStatistics, btPremios;
    TextView txtNome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btStatistics = (Button) findViewById(R.id.button);
        btPremios = (Button) findViewById(R.id.button2);
        txtNome = (TextView) findViewById(R.id.textView2);

        btStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
                //i.putExtra("meters", meters.getText().toString());
                startActivity(i);
            }
        });

        btPremios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtNome.setText("Hi there, Thalita!");
                Intent i = new Intent(getApplicationContext(), Premios.class);
                //i.putExtra("meters", meters.getText().toString());
                startActivity(i);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
