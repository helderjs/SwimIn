package com.ufba.swimin;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

public class UserListActivity extends Activity {

	ListView users;
	ArrayList<String> lvArray;
	ArrayList <Integer>lvArrayId;
	ArrayAdapter<String> adapter;
	
	ImageButton btSearch;
	Spinner spnCategory;
	EditText etNome;
	
	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	
	String id_treinador = "2";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        
        bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);        
       
        lvArray = new ArrayList<String>();
        lvArrayId = new ArrayList<Integer>();
        
        users = (ListView) findViewById(R.id.listView);
        btSearch = (ImageButton) findViewById(R.id.btSearch);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
		etNome = (EditText) findViewById(R.id.etNome);
       
        btSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				
				lvArray.clear();
				lvArrayId.clear();
				
				/*int	pos = spnCategory.getSelectedItemPosition();	
							
				switch(pos){
				//Categoria: todos (índice 0)
				case 0: if(etNome.getText().toString().equals("")){
						buscarDados("pessoas",new String[]{"id","nome"});
					}else{
						buscarDados("pessoas",new String[]{"id","nome"},"nome LIKE '%"
								+ etNome.getText().toString() +"%'");
					}
					break;
			    //Categoria: atletas (índice 1)
				case 1: */if(etNome.getText().toString().equals("")){
						buscarDados("SELECT p.id, nome FROM pessoas AS p, atletas AS a " 
								+ "WHERE p.id = a.id");		
					}else{
						buscarDados("SELECT p.id, nome FROM pessoas AS p, atletas AS a "
								+ "WHERE p.id = a.id "
								+ "AND nome LIKE '%"+etNome.getText().toString()+"%'");
					}/*
					break;
			    //Categoria: equipe (índice 2)
				case 2: if(etNome.getText().toString().equals("")){
						buscarDados("SELECT p.id, nome FROM pessoas AS p, atletas_treinadores AS a "
								+ "WHERE p.id = a.id_atleta "
								+ "AND a.id_treinador = "+id_treinador);
					}else{
						buscarDados("SELECT p.id, nome FROM pessoas AS p, atletas_treinadores As a "
								+ "WHERE p.id = a.id_atleta "
								+ "AND a.id_treinador = "+id_treinador
								+ " AND nome LIKE '%"+etNome.getText().toString()+"%'");
					}
					break;
				//Categoria: treinadores (índice 3)
				case 3: if(etNome.getText().toString().equals("")){
						buscarDados("SELECT p.id, nome FROM pessoas AS p, treinadores AS t "
								+ "WHERE p.id = t.id");
					}else{
						buscarDados("SELECT p.id, nome FROM pessoas AS p, treinadores AS t "
								+ "WHERE p.id = t.id "
								+ "AND nome LIKE '%"+etNome.getText().toString()+"%'");
					}
					break;
				}*/
				
				if(cursor.getCount()>0){
					do{
							lvArray.add(cursor.getString(cursor.getColumnIndex("nome")));
							lvArrayId.add(cursor.getInt(cursor.getColumnIndex("id")));
					}while(cursor.moveToNext());
				}
				
				adapter = new ArrayAdapter<String>(UserListActivity.this, 
						android.R.layout.simple_list_item_1, lvArray);
				users.setAdapter(adapter);
			}
		});

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private boolean buscarDados(String sql){
    	try{
    		cursor = bancoDados.rawQuery(sql, null);
    		
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

    public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(UserListActivity.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
    
}

