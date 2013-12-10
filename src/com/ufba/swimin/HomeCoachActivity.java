package com.ufba.swimin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class HomeCoachActivity extends Activity {

	ImageButton btPerfil, btUsuarios, btTreinos, btRanking;
	Context context = this;
	
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
				//setContentView(R.layout.profile);
				Intent intent = new Intent(context, ProfileActivity.class);
				startActivity(intent);
			}
		});
		
		btUsuarios.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//setContentView(R.layout.user_list);
				Intent intent = new Intent(context, UserListActivity.class);
				startActivity(intent);
			}
		});
		
		btTreinos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//setContentView(R.layout.training);
				Intent intent = new Intent(context, TrainingActivity.class);
				startActivity(intent);
			}
		});
		
		btRanking.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//setContentView(R.layout.ranking);
				Intent intent = new Intent(context, Ranking.class);
				startActivity(intent);
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
