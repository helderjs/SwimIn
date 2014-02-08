package com.ufba.swimin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Athlete;
import com.ufba.swimin.model.Coach;

import java.text.SimpleDateFormat;

public class ProfileActivity extends Activity {
    DatabaseHelper db;
    Bundle extras;
    Button btStatistics, btPremios, btEditarPerfil;
    TextView tvNome, tvIdade, tvPeso, tvAltura;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

        extras = getIntent().getExtras();

        btStatistics = (Button) findViewById(R.id.button);
        btPremios = (Button) findViewById(R.id.button2);
        btEditarPerfil = (Button) findViewById(R.id.btEditar);
        tvNome = (TextView) findViewById(R.id.tvNome);
        tvIdade = (TextView) findViewById(R.id.tvIdade);
        tvPeso = (TextView) findViewById(R.id.tvPeso);
        tvAltura = (TextView) findViewById(R.id.tvAltura);

        carregaDadosNaTela();

        btStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), StatisticsActivity.class);
                i.putExtra("person_id", extras.getString("person_id"));
                startActivity(i);
            }
        });

        btPremios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Premios.class);
                i.putExtra("person_id", extras.getString("person_id"));
                startActivity(i);
            }
        });
        btEditarPerfil.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), EditProfile.class);
                i.putExtra("person_id", extras.getString("person_id"));
                i.putExtra("person_type", extras.getString("person_type"));
                startActivity(i);
			}
		});
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		carregaDadosNaTela();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void carregaDadosNaTela(){
        db = new DatabaseHelper(this);

        if (extras.getString("person_type").equals("COACH")) {
            Coach ps = db.getCoach();
            tvNome.setText("Nome: " + ps.getName());
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            tvIdade.setText("Nascimento: " + dt.format(ps.getBirthday()));
            tvPeso.setVisibility(View.GONE);
            tvAltura.setVisibility(View.GONE);
            btStatistics.setVisibility(View.GONE);
            btPremios.setVisibility(View.GONE);
        } else {
            Athlete ps = db.getAthlete(Long.valueOf(extras.getString("person_id")));
            tvNome.setText("Nome:" + ps.getName());
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            tvIdade.setText("Nascimento: " + dt.format(ps.getBirthday()));
            tvPeso.setText("Peso: " + String.valueOf(ps.getWeight()));
            tvAltura.setText("Altura: "+ String.valueOf(ps.getHeight()));
        }
        //tvIdade.setText(extras.getString("person_id"));
        /*if (person_type == 1) {
            Coach ps = db.getCoach();
            tvNome.setText(ps.getName());
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            tvIdade.setText(dt.format(ps.getBirthday()));
            tvPeso.setVisibility(View.GONE);
            tvAltura.setVisibility(View.GONE);
        } else {
            Athlete ps = db.getAthlete(person_id);
            tvNome.setText(String.valueOf(person_id));
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            tvIdade.setText(dt.format(ps.getBirthday()));
            tvPeso.setText(String.valueOf(ps.getWeight()));
            tvAltura.setText(String.valueOf(String.valueOf(person_type)));
        }*/
	}
}
