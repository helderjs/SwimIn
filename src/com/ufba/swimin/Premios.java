package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.ufba.swimin.R;

public class Premios extends Activity {
	
	TableLayout tbMedalhas;
	Button btAdicionar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premios);
		
		tbMedalhas = (TableLayout) findViewById(R.id.tbMedalhas);
		btAdicionar = (Button) findViewById(R.id.btAdicionar);
		
		btAdicionar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				EditText etNome = (EditText) findViewById(R.id.etNome);
				
				if(etNome.getText().toString().equals("")){
					exibirMensagem("Atenção","Você deve dar um nome a premiação.");
					return;
				}
				
				TableRow tbRow = (TableRow) findViewById(R.id.tbRow1);
				ImageView ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
				
				final ImageView medalha = new ImageView(Premios.this);
				
				RadioGroup rgMedalha = (RadioGroup) findViewById(R.id.rgMedalha);
				int rbId = rgMedalha.getCheckedRadioButtonId();
				int rbSelecionado = rgMedalha.indexOfChild(rgMedalha.findViewById(rbId));
				
				switch(rbSelecionado){
				case 0: medalha.setImageResource(R.drawable.silver_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					break;
				case 1: medalha.setImageResource(R.drawable.bronze_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					break;
				case 2: medalha.setImageResource(R.drawable.gold_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					break;
				default: exibirMensagem("Atenção", "É preciso selecionar o tipo de medalha");
				}
			}
		});
		
		/*TableRow tbRow = (TableRow) findViewById(R.id.tbRow1);
		
		ImageView ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
		
		final ImageView medalha = new ImageView(this);
		medalha.setImageResource(R.drawable.silver_medal);
		
		medalha.setLayoutParams(ivMedalha.getLayoutParams());
		
		tbRow.addView(medalha);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(Premios.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
	
}
