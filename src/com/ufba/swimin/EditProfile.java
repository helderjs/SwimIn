package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfile extends Activity {

	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	
	EditText etNome, etDataNasc, etEndereco;
	Button btSalvar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		
		etNome = (EditText) findViewById(R.id.etNome);
		etDataNasc = (EditText) findViewById(R.id.etDataNasc);
		etEndereco = (EditText) findViewById(R.id.etEndereco);
		btSalvar = (Button) findViewById(R.id.btSalvar);
		
		abreBanco();
	    carregaDadosNaTela();
	     
	    btSalvar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					String sql = "UPDATE pessoas SET nome='"+etNome.getText().toString()+"' "
							+ "WHERE id=1";
					bancoDados.execSQL(sql);
					sql = "UPDATE pessoas SET data_nasc='"+etDataNasc.getText().toString()+"' "
							+ "WHERE id=1";
					bancoDados.execSQL(sql);
					sql = "UPDATE pessoas SET endereco='"+etEndereco.getText().toString()+"' "
							+ "WHERE id=1";
					bancoDados.execSQL(sql);
				}catch(Exception erro){
					exibirMensagem("Erro", "Erro: "+erro.getMessage());
				}
				finish();
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}*/

	public void abreBanco(){
		bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
	}
	
	public void carregaDadosNaTela(){
		if(buscarDados("pessoas",new String[]{"id","nome","data_nasc","endereco"},"id = 1")){
			etNome.setText(cursor.getString(cursor.getColumnIndex("nome")));
			etDataNasc.setText(cursor.getString(cursor.getColumnIndex("data_nasc")));
			etEndereco.setText(cursor.getString(cursor.getColumnIndex("endereco")));
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
		AlertDialog.Builder mensagem = new AlertDialog.Builder(EditProfile.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
	
}
