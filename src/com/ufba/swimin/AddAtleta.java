package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAtleta extends Activity {

	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	
	EditText etNome, etDataNasc, etEndereco, etPeso, etAltura;
	Button btSalvar;
	
	String id_treinador = "1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_atleta);
		
		etNome = (EditText) findViewById(R.id.etNome2);
		etDataNasc = (EditText) findViewById(R.id.etDataNasc2);
		etEndereco = (EditText) findViewById(R.id.etEndereco2);
		etPeso = (EditText) findViewById(R.id.etPeso2);
		etAltura = (EditText) findViewById(R.id.etAltura2);
		btSalvar = (Button) findViewById(R.id.btSalvar2);
		
		abreBanco();
	     
	    btSalvar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(etNome.getText().toString().equals("")){
					exibirMensagem("Atenção", "Preencha o nome do atleta.");
					return;
				}
				if(etDataNasc.getText().toString().equals("")){
					exibirMensagem("Atenção", "Preencha a data de nascimento do atleta.");
					return;
				}
				if(etEndereco.getText().toString().equals("")){
					exibirMensagem("Atenção", "Preencha o endereço do atleta.");
					return;
				}
				if(etPeso.getText().toString().equals("")){
					exibirMensagem("Atenção", "Informe o peso do atleta");
					return;
				}
				if(etAltura.getText().toString().equals("")){
					exibirMensagem("Atenção", "Informe a altura do atleta");
					return;
				}
				try{
					String sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
							+ "('"+etNome.getText().toString() + "', '"
							+ etDataNasc.getText().toString() + "', '"
							+ etEndereco.getText().toString()+"')";
					bancoDados.execSQL(sql);
					buscarDados("pessoas", new String[]{"id"});
					cursor.moveToLast();
					
					String id_atleta = Integer.toString(cursor.getInt(cursor.getColumnIndex("id")));
					
					sql = "INSERT INTO atletas (id, peso, altura) values "
							+ "(" + id_atleta
							+ ", " + etPeso.getText().toString() 
							+ ", " + etAltura.getText().toString()+ ")";
					bancoDados.execSQL(sql);
					sql = "INSERT INTO atletas_treinadores (id_atleta, id_treinador) values "
							+ "(" + id_atleta +", "+id_treinador+")";
					bancoDados.execSQL(sql);
					exibirMensagem("Salvo","Atleta cadastrado com sucesso.");
					finish();
				}catch(Exception erro){
					exibirMensagem("Erro", "Erro: "+erro.getMessage());
				}
			}
		});
	}
	
	public void abreBanco(){
		bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
	}
	
	private boolean buscarDados(String tabela, String[] select){
		try{
			cursor = bancoDados.query(tabela, 
					select, 
					null,//selection,
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
		AlertDialog.Builder mensagem = new AlertDialog.Builder(AddAtleta.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
}
