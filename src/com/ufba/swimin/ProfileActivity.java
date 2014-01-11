package com.ufba.swimin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity {
    Button btStatistics, btPremios, btEditarPerfil;
    TextView tvNome, tvIdade, tvEndereco, tvPeso, tvAltura, tvTreinador;
    
    SQLiteDatabase bancoDados = null;
    Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

        btStatistics = (Button) findViewById(R.id.button);
        btPremios = (Button) findViewById(R.id.button2);
        btEditarPerfil = (Button) findViewById(R.id.btEditar);
        tvNome = (TextView) findViewById(R.id.tvNome);
        tvIdade = (TextView) findViewById(R.id.tvIdade);
        tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        tvPeso = (TextView) findViewById(R.id.tvPeso);
        tvAltura = (TextView) findViewById(R.id.tvAltura);
        tvTreinador = (TextView) findViewById(R.id.tvTreinador);
        
        abreBanco();
        carregaDadosNaTela();

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
        btEditarPerfil.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), EditProfile.class);
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
	
	public void abreBanco(){
		bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
	}
	
	public void carregaDadosNaTela(){
		if(buscarDados("pessoas",new String[]{"id","nome","data_nasc","endereco"},"id = 1")){
			tvNome.setText(cursor.getString(cursor.getColumnIndex("nome")));
			tvIdade.setText(cursor.getString(cursor.getColumnIndex("data_nasc")));
			tvEndereco.setText(cursor.getString(cursor.getColumnIndex("endereco")));
			//Nessa versão do programa, não teremos atletas utilizando o aplicativo
			tvPeso.setVisibility(View.GONE);
			tvAltura.setVisibility(View.GONE);
			tvTreinador.setVisibility(View.GONE);
		}
	}
	
	private boolean buscarDados(String tabela, String[] select, String where){
		try{
			cursor = bancoDados.query(tabela, 
					select, 
					where,//selection,
					null,//selectionArgs, 
					null,//groupBy, 
					null,//having,
					null,//orderBy,nome
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
	
	public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(ProfileActivity.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
}
