package com.ufba.swimin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
		init();		//Carrega as informações na tela
		
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
	
	/* ---------------------------------------------------------------------
	 * Carrega os dados da tela - texto de boas-vindas e últimos treinos
	 */
	
	public void init(){
		TextView tvWelcome, tvName1, tvName2, tvDateAndTime1, tvDateAndTime2,
			tvNado1, tvNado2, tvNado3, tvNado4;
		
		tvWelcome = (TextView) findViewById(R.id.tvWelcome);
		tvName1 = (TextView) findViewById(R.id.tvName1);
		tvName2 = (TextView) findViewById(R.id.tvName2);
		tvDateAndTime1 = (TextView) findViewById(R.id.tvDateAndTime1);
		tvDateAndTime2 = (TextView) findViewById(R.id.tvDateAndTime2);
		tvNado1 = (TextView) findViewById(R.id.tvNado1);
		tvNado2 = (TextView) findViewById(R.id.tvNado2);
		tvNado3 = (TextView) findViewById(R.id.tvNado3);
		tvNado4 = (TextView) findViewById(R.id.tvNado4);
		
		// id_treinador será utilizado provisoriamente, enquanto nenhum login é implementado
		String id_treinador = "2";
		
		// Busca o nome do treinador para exibir na tela de boas-vindas
		buscarDados("pessoas", new String[]{"nome"},
				"id = "+id_treinador);
		tvWelcome.setText("Bem-vindo "
				+ cursor.getString(cursor.getColumnIndex("nome")));
		
		// Recupera os dados do último treino
		if(buscarDados("treinos", new String[]{"id_atleta","tipo_nado", "metros",
				"tempo","data","hora"},
				"id_treinador = "+id_treinador)){
		
			cursor.moveToLast();
			int id_atleta = cursor.getInt(cursor.getColumnIndex("id_atleta"));
			
			String aux = cursor.getString(cursor.getColumnIndex("data")) +
					" - " + cursor.getString(cursor.getColumnIndex("hora"));
			tvDateAndTime1.setText(aux);
			
			aux = cursor.getString(cursor.getColumnIndex("tipo_nado"))
					+ ": " + cursor.getString(cursor.getColumnIndex("tempo"));
			tvNado1.setText(aux);
			
			// Recupera os dados do penúltimo treino
	
			if(cursor.moveToPrevious()){
			
				aux = cursor.getString(cursor.getColumnIndex("data")) +
						" - " + cursor.getString(cursor.getColumnIndex("hora"));
				tvDateAndTime2.setText(aux);
				
				aux = cursor.getString(cursor.getColumnIndex("tipo_nado"))
						+ ": " + cursor.getString(cursor.getColumnIndex("tempo"));
				tvNado3.setText(aux);
				
				try{
					buscarDados("pessoas", new String[]{"nome"},"id = "
							+ Integer.toString(cursor.getInt(
									cursor.getColumnIndex("id_atleta"))));
					tvName2.setText(cursor.getString(cursor.getColumnIndex("nome")));
				}catch(Exception erro){
					exibirMensagem("Erro", "Erro: " + erro.getMessage());
				}
			}
			
			try{
				buscarDados("pessoas", new String[]{"nome"},"id = "
						+ Integer.toString(id_atleta));
				tvName1.setText(cursor.getString(cursor.getColumnIndex("nome")));
			}catch(Exception erro){
				exibirMensagem("Erro", "Erro: " + erro.getMessage());
			}
			
			/*Alguns textos ficarão provisoriamente invisíveis, 
			 * para melhoramento posterior
			 * */
			tvNado2.setVisibility(View.GONE);
			tvNado4.setVisibility(View.GONE);
		}
	}
	
	//*******************	BANCO	*******************		
	
	public void abreOuCriaBanco() {
		try{
			//Cria ou abre o banco
			bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
			
			//bancoDados.execSQL("DROP TABLE IF EXISTS pessoas");
			//bancoDados.execSQL("DROP TABLE IF EXISTS atletas");
			//bancoDados.execSQL("DROP TABLE IF EXISTS treinadores");
			//bancoDados.execSQL("DROP TABLE IF EXISTS atletas_treinadores");
			//bancoDados.execSQL("DROP TABLE IF EXISTS treinos");
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
					+ "id_atleta INTEGER, tipo_nado TEXT, metros REAL, tempo TEXT, "
					+ "data TEXT, hora TEXT, "
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
	
	private boolean buscarDados(String tabela, String[] select){
		try{
			cursor = bancoDados.query(tabela, 
					select, 
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
	
	private boolean buscarDados(String tabela, String[] select, String where){
		try{
			cursor = bancoDados.query(tabela, 
					select, 
					where,//selection,
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
	
	public void gravarRegistro(){
		try{
			String sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
					+ "('Manoel Neto', '14/07/1977', 'Rua Rochael Tantan n 38 - Salvador BA')";
					//+ "('Thalita Andrade', '09/11/1988', 'Rua Itambém Ticotico - Salvador BA')";
					//+ "('Helder Carvalho', '11/12/1995', 'Rua Circo Pegafogo - Salvador BA')";
					//+ "('Lucas Augusto', '30/03/1956', 'Rua Vandré Datena - São Paulo SP')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
					//+ "('Manoel Neto', '14/07/1977', 'Rua Rochael Tantan n 38 - Salvador BA')";
					+ "('Thalita Andrade', '09/11/1988', 'Rua Itambém Ticotico - Salvador BA')";
					//+ "('Helder Carvalho', '11/12/1995', 'Rua Circo Pegafogo - Salvador BA')";
					//+ "('Lucas Augusto', '30/03/1956', 'Rua Vandré Datena - São Paulo SP')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
					//+ "('Manoel Neto', '14/07/1977', 'Rua Rochael Tantan n 38 - Salvador BA')";
					//+ "('Thalita Andrade', '09/11/1988', 'Rua Itambém Ticotico - Salvador BA')";
					+ "('Helder Carvalho', '11/12/1995', 'Rua Circo Pegafogo - Salvador BA')";
					//+ "('Lucas Augusto', '30/03/1956', 'Rua Vandré Datena - São Paulo SP')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
					//+ "('Manoel Neto', '14/07/1977', 'Rua Rochael Tantan n 38 - Salvador BA')";
					//+ "('Thalita Andrade', '09/11/1988', 'Rua Itambém Ticotico - Salvador BA')";
					//+ "('Helder Carvalho', '11/12/1995', 'Rua Circo Pegafogo - Salvador BA')";
					+ "('Lucas Augusto', '30/03/1956', 'Rua Vandré Datena - São Paulo SP')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO premios(id_atleta, nome, tipo) values "
					+ "(1, 'Olimpiadas 2006', 0)";
					//+ "(1, 'Olimpiadas 2007', 2)";
					//+ "(1, 'Olimpiadas 2008', 2)";
					//+ "(1, 'Rio 2011', 1)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO premios(id_atleta, nome, tipo) values "
					//+ "(1, 'Olimpiadas 2006', 0)";
					+ "(1, 'Olimpiadas 2007', 2)";
					//+ "(1, 'Olimpiadas 2008', 2)";
					//+ "(1, 'Rio 2011', 1)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO premios(id_atleta, nome, tipo) values "
					//+ "(1, 'Olimpiadas 2006', 0)";
					//+ "(1, 'Olimpiadas 2007', 2)";
					+ "(1, 'Olimpiadas 2008', 2)";
					//+ "(1, 'Rio 2011', 1)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO premios(id_atleta, nome, tipo) values "
					//+ "(1, 'Olimpiadas 2006', 0)";
					//+ "(1, 'Olimpiadas 2007', 2)";
					//+ "(1, 'Olimpiadas 2008', 2)";
					+ "(1, 'Rio 2011', 1)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO atletas (id, peso, altura) values "
					+ "(1, 77.6, 1.82)";
					//+ "(4, 67.8, 1.75)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO atletas (id, peso, altura) values "
					//+ "(1, 77.6, 1.82)";
					+ "(4, 67.8, 1.75)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO treinadores (id, numero_atletas) values "
					+ "(2, 0)";
					//+ "(3, 0)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO treinadores (id, numero_atletas) values "
					//+ "(2, 0)";
					+ "(3, 0)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO treinos (id_treinador, id_atleta, tipo_nado, "
					+ "metros, tempo, data, hora) values "
					+ "(2, 1, 'crawl', 55.4, '01:36', '12/12/2013', '14:35:55')";
					//+ "(2, 4, 'borboleta', 30.5, '0:51', '12/12/2013', '14:50:33')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO treinos (id_treinador, id_atleta, tipo_nado, "
					+ "metros, tempo, data, hora) values "
					//+ "(2, 1, 'crawl', 55.4, '01:36', '12/12/2013', '14:35:55')";
					+ "(2, 4, 'borboleta', 30.5, '0:51', '12/12/2013', '14:50:33')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO atletas_treinadores (id_atleta, id_treinador) values "
					+ "(1, 2)";
					//+ "(4, 2)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO atletas_treinadores (id_atleta, id_treinador) values "
					//+ "(1, 2)";
					+ "(4, 2)";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO pessoas (nome, data_nasc, endereco) values "
					+ "('Caio José', '30/01/1980', 'Rua Manoel Tavares - Salvador BA')";
			bancoDados.execSQL(sql);
			sql = "INSERT INTO atletas (id, peso, altura) values "
					+ "(5, 60.6, 1.68)";
			bancoDados.execSQL(sql);
			
			//exibirMensagem("Banco", "Dados gravados com sucesso!");
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