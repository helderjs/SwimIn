package com.ufba.swimin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TrainingActivity extends Activity {

	EditText distancia;
    Button btStart;
	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training);

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        distancia = (EditText) findViewById(R.id.distancia);
		btStart = (Button) findViewById(R.id.btStart);
		
		btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distancia.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "Defina os metros", Toast.LENGTH_LONG).show();
                    return;
                } else{
                	//setContentView(R.layout.cronometer);                	
                	Intent intent = new Intent(context, CronometerActivity.class);
                    startActivity(intent);
                }
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
