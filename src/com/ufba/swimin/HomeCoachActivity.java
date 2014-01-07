package com.ufba.swimin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

public class HomeCoachActivity extends Activity {

	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	
	ImageButton btPerfil, btUsuarios, btTreinos, btRanking;
	Context context = this;
	
	//Home para Treinador
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_coach);
		
		abreOuCriaBanco();
		//gravarRegistro();
		
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//*******************	BANCO	*******************		
	
	public void abreOuCriaBanco() {
		try{
			//Cria ou abre o banco
			bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
			
			//bancoDados.execSQL("DROP TABLE IF EXISTS premios");
			
			String sql = "CREATE TABLE IF NOT EXISTS pessoas "
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, data_nasc TEXT, endereco TEXT)";
			bancoDados.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS atletas "
					+ "(id INTEGER PRIMARY KEY REFERENCES pessoas (id), "
					+ "peso REAL, altura REAL)";
			bancoDados.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS treinadores "
					+ "(id INTEGER PRIMARY KEY REFERENCES pessoas(id), "
					+ "numero_atletas INTEGER)";
			bancoDados.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS atletas_treinadores "
					+ "(id_atleta INTEGER PRIMARY KEY REFERENCES atletas(id), "
					+ "id_treinador INTEGER, "
					+ "FOREIGN KEY (id_treinador) REFERENCES treinadores(id))";
			bancoDados.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS treinos "
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_treinador INTEGER, "
					+ "id_atleta INTEGER, tipo_nado TEXT, metros REAL, tempo INTEGER, "
					+ "FOREIGN KEY (id_treinador) REFERENCES treinadores(id), "
					+ "FOREIGN KEY (id_atleta) REFERENCES atletas(id))";
			bancoDados.execSQL(sql);
			
			sql = "CREATE TABLE IF NOT EXISTS premios "
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "id_atleta INTEGER, nome TEXT, tipo INTEGER, "
					+ "FOREIGN KEY (id_atleta) REFERENCES atletas(id))";
			bancoDados.execSQL(sql);
		}catch(Exception erro){
			exibirMensagem("Erro banco", "Erro ao abrir ou criar banco: " + erro.getMessage());
		}
	}
	
	public void fechaBanco(){
		try{
			bancoDados.close();
		}catch(Exception erro){
			exibirMensagem("Erro Banco", "Erro ao fechar o banco " + erro.getMessage());
		}
	}
	
	private boolean buscarDados(){
		try{
			cursor = bancoDados.query("pessoas", 
					new String [] {"nome","dataNasc","endereco"}, 
					null,//selection,
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
	
	public void mostraProximoRegistro(){
		try{
			cursor.moveToNext();
			mostrarDados();
		}catch(Exception erro){
			exibirMensagem("Erro","Erro ao acessar próximo registro: "+erro.getMessage());
		}
	}
	
	public void mostraRegistroAnterior(){
		try{
			cursor.moveToPrevious();
			mostrarDados();
		}catch(Exception erro){
			exibirMensagem("Erro","Erro ao acessar registro anterior: "+erro.getMessage());
		}
	}
	
	public void gravarRegistro(){
		try{
			/*String sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
					+ "('Manoel Neto', '14/07/1977', 'Rua Rochael Tantan n 38 - Salvador BA'), "
					+ "('Thalita Andrade', '09/11/1988', 'Rua Itambém Ticotico - Salvador BA'), "
					+ "('Helder Carvalho', '11/12/1995', 'Rua Circo Pegafogo - Salvador BA'),"
					+ "('Lucas Augusto', '30/03/1956', 'Rua Vandré Datena - São Paulo SP')";*/
			String sql = "INSERT INTO premios (id_atleta, nome, tipo) values "
					+ "(1, 'Olimpiadas 2006', 0), "
					+ "(1, 'Olimpiadas 2007', 2), "
					+ "(1, 'Olimpiadas 2008', 2), "
					+ "(1, 'Rio 2011', 1)";
			bancoDados.execSQL(sql);
			
			exibirMensagem("Banco", "Dados gravados com sucesso!");
		}catch(Exception erro){
			exibirMensagem("Erro","Erro ao gravar dados: " + erro.getMessage());
		}
	}
	
	//*******************	FIM BANCO	*******************
	
	//*******************	INTERFACE	*******************
	
	public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(HomeCoachActivity.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
	
	//*******************	FIM INTERFACE	*******************
	
	public void mostrarDados(){
		
	}
}