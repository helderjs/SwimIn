package com.ufba.swimin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class HomeCoachActivity extends Activity {

	ImageButton btPerfil, btUsuarios, btTreinos, btRanking;
	
	//Home para Treinador
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_coach);
		
		btPerfil = (ImageButton) findViewById(R.id.btPerfil);
		btUsuarios = (ImageButton) findViewById(R.id.btUsuarios);
		btTreinos = (ImageButton) findViewById(R.id.btTreinos);
		btRanking = (ImageButton) findViewById(R.id.btRanking);
		
		btPerfil.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setContentView(R.layout.profile);				
			}
		});
		
		btUsuarios.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setContentView(R.layout.user_list);
			}
		});
		
		btTreinos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setContentView(R.layout.training);
			}
		});
		
		btRanking.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setContentView(R.layout.ranking);
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
