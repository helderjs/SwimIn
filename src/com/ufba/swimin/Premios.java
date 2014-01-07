package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	TableRow tbRow = null;
	
	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premios);
		
		bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
		
		tbMedalhas = (TableLayout) findViewById(R.id.tbMedalhas);
		btAdicionar = (Button) findViewById(R.id.btAdicionar);
		tbRow = (TableRow) findViewById(R.id.tbRow1);
		
		ImageView ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
		ivMedalha.setVisibility(View.GONE);
		
		if(buscarDados()){
			cursor.moveToFirst();
			count=cursor.getCount();
			for(int i = 0; i<cursor.getCount(); i++){
				
				if(i==7){
					tbRow = (TableRow) findViewById(R.id.tbRow2);
				}
				if(i==15){
					tbRow = (TableRow) findViewById(R.id.tbRow3);
				}
				if(i==22){
					tbRow = (TableRow) findViewById(R.id.tbRow4);
				}
				if(i==29){
					tbRow = (TableRow) findViewById(R.id.tbRow5);
				}
				
				final ImageView medalha = new ImageView(Premios.this);
				
				switch(cursor.getInt(cursor.getColumnIndex("tipo"))){
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
				}
				cursor.moveToNext();				
			}
		}
		
		btAdicionar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				EditText etNome = (EditText) findViewById(R.id.etNome);
				
				if(etNome.getText().toString().equals("")){
					exibirMensagem("Atenção","Você deve dar um nome a premiação.");
					return;
				}
				
				if(count>34){
					exibirMensagem("Atenção","Você atingiu o número máximo de premiações.");
					return;
				}
				
				if(count==7){
					tbRow = (TableRow) findViewById(R.id.tbRow2);
				}
				if(count==14){
					tbRow = (TableRow) findViewById(R.id.tbRow3);
				}
				if(count==21){
					tbRow = (TableRow) findViewById(R.id.tbRow4);
				}
				if(count==28){
					tbRow = (TableRow) findViewById(R.id.tbRow5);
				}
				
				ImageView ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
				
				final ImageView medalha = new ImageView(Premios.this);
				
				RadioGroup rgMedalha = (RadioGroup) findViewById(R.id.rgMedalha);
				int rbId = rgMedalha.getCheckedRadioButtonId();
				int rbSelecionado = rgMedalha.indexOfChild(rgMedalha.findViewById(rbId));
				
				switch(rbSelecionado){
				case 0: medalha.setImageResource(R.drawable.silver_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					count++;
					gravarMedalhaBanco(1,etNome.getText().toString(),rbSelecionado);
					break;
				case 1: medalha.setImageResource(R.drawable.bronze_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					count++;
					gravarMedalhaBanco(1,etNome.getText().toString(),rbSelecionado);
					break;
				case 2: medalha.setImageResource(R.drawable.gold_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					count++;
					gravarMedalhaBanco(1,etNome.getText().toString(),rbSelecionado);
					break;
				default: exibirMensagem("Atenção", "É preciso selecionar o tipo de medalha");
				}
			}
		});
	}

	private boolean buscarDados(){
		try{
			cursor = bancoDados.query("premios", 
					new String [] {"id_atleta","nome","tipo"}, 
					"id_atleta = 1",//selection,
					null,//selectionArgs, 
					null,//groupBy, 
					null,//having,
					null,//orderBy,
					null);//Limite de registros retornados
			
			int numeroRegistros = cursor.getCount();
			if(numeroRegistros!=0){
				cursor.moveToFirst();
				return true;
			}else
				return false;	
		}catch(Exception erro){
			exibirMensagem("Erro banco.", "Erro ao buscar dados no banco: " + erro.getMessage());
			return false;
		}
	}
	
	public void gravarMedalhaBanco(int id_atleta, String nome, int tipo){
		try{
			String sql = "INSERT INTO premios (id_atleta, nome, tipo) values "
					+ "("+id_atleta+",'"+nome+"',"+tipo+")";
			bancoDados.execSQL(sql);
			
			//exibirMensagem("Premiação", "Medalha adicionada com sucesso!");
		}catch(Exception erro){
			exibirMensagem("Erro","Erro ao gravar dados: " + erro.getMessage());
		}
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
