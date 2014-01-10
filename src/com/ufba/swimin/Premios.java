package com.ufba.swimin;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.ufba.swimin.R;

public class Premios extends Activity {
	
	TableLayout tbMedalhas;
	Button btAdicionar;
	TableRow tbRow = null;
	Spinner spnAtletas;
	ImageView ivMedalha;
	
	ArrayList <ImageView> ivArray;
	
	ArrayList <String> spnArray;
	ArrayList <Integer> spnArrayId;
	SpinnerAdapter adapter;
	
	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	int count = 0;
	
	int index = 0; //indice do atleta selecionado
	
	String id_treinador = "2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premios);
		
		//Abre banco
		bancoDados = openOrCreateDatabase("bancoSwing", MODE_WORLD_READABLE, null);
		
		tbMedalhas = (TableLayout) findViewById(R.id.tbMedalhas);
		btAdicionar = (Button) findViewById(R.id.btAdicionar);
		tbRow = (TableRow) findViewById(R.id.tbRow1);
		spnAtletas = (Spinner) findViewById(R.id.spnAtleta);
	 
		spnArray = new ArrayList<String>();
	    spnArrayId = new ArrayList<Integer>();
	    
	    ivArray = new ArrayList<ImageView>();
		
		//O ImageView ivMedalha1 serve para passamarmos seus atributos,
		//como largura e altura, as outras medalhas - isso é feito com
		//ivMedalha.getLayoutParams()
		
		ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
		ivMedalha.setVisibility(View.GONE);
		
		buscarDados("SELECT p.id, nome FROM pessoas AS p, atletas_treinadores AS a "
				+ "WHERE p.id = a.id_atleta "
				+ "AND a.id_treinador = "+id_treinador);
		
		//Carrega atletas no spinner
		
		if(cursor.getCount()>0){
			do{
				spnArray.add(cursor.getString(cursor.getColumnIndex("nome")));
				spnArrayId.add(cursor.getInt(cursor.getColumnIndex("id")));
			}while(cursor.moveToNext());
			
			adapter = new ArrayAdapter<String>(Premios.this, 
					android.R.layout.simple_spinner_item, spnArray);
			spnAtletas.setAdapter(adapter);
			
			//Carrega as medalhas do primeiro atleta
			index = 0;			
			//carregaMedalhasDoAtleta(spnArrayId.get(index));
			
			spnAtletas.setOnItemSelectedListener(new OnItemSelectedListener() {

	            @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
	                // TODO Auto-generated method stub
	                Object item = arg0.getItemAtPosition(arg2);
	                if (item!=null) {
						int pos = arg2;
		
						index = pos;
						while(ivArray.size()>0){
							ivArray.get(0).setVisibility(View.GONE);
							ivArray.remove(0);
						}
						
						tbRow = (TableRow) findViewById(R.id.tbRow1);
						
						carregaMedalhasDoAtleta(spnArrayId.get(index));
	                }

	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub

	            }
	        });
						
			btAdicionar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					EditText etNome = (EditText) findViewById(R.id.etNome);
					
					//Compara se o conteúdo do campo nome é igual a vazio
					if(etNome.getText().toString().equals("")){
						exibirMensagem("Atenção","Você deve dar um nome a premiação.");
						return;
					}
					
					//Retorna se o número de medalhas for maior que 34
					
					if(count>34){
						exibirMensagem("Atenção","Você atingiu o número máximo de premiações.");
						return;
					}
					
					/*Caso existam 7 medalhas em uma mesma linha, as 
					 * próximas serão adicionadas na linha logo abaixo
					 * */
					
					//exibirMensagem("Count",Integer.toString(count));
					
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
					
					final ImageView medalha = new ImageView(Premios.this);
					
					//A posição da nova medalha será a última posição + 1
					cursor.moveToLast();
					medalha.setTag(cursor.getPosition()+1);
					
					//Captura o índice do radioButton selecionado
					
					RadioGroup rgMedalha = (RadioGroup) findViewById(R.id.rgMedalha);
					int rbId = rgMedalha.getCheckedRadioButtonId();
					int rbSelecionado = rgMedalha.indexOfChild(rgMedalha.findViewById(rbId));
					
					switch(rbSelecionado){
					case 0: medalha.setImageResource(R.drawable.silver_medal);
						medalha.setLayoutParams(ivMedalha.getLayoutParams());
						tbRow.addView(medalha);
						count++;
						gravarMedalhaBanco(spnArrayId.get(index),etNome.getText().toString(),rbSelecionado);
						ivArray.add(medalha);
						buscarDados(spnArrayId.get(index));
						
						eventoClicaMedalha(medalha, "Medalha de prata");
						
						break;
					case 1: medalha.setImageResource(R.drawable.bronze_medal);
						medalha.setLayoutParams(ivMedalha.getLayoutParams());
						tbRow.addView(medalha);
						count++;
						gravarMedalhaBanco(spnArrayId.get(index),etNome.getText().toString(),rbSelecionado);
						ivArray.add(medalha);
						buscarDados(spnArrayId.get(index));
						
						eventoClicaMedalha(medalha, "Medalha de bronze");
						
						break;
					case 2: medalha.setImageResource(R.drawable.gold_medal);
						medalha.setLayoutParams(ivMedalha.getLayoutParams());
						tbRow.addView(medalha);
						count++;
						gravarMedalhaBanco(spnArrayId.get(index),etNome.getText().toString(),rbSelecionado);
						ivArray.add(medalha);
						buscarDados(spnArrayId.get(index));
						
						eventoClicaMedalha(medalha, "Medalha de ouro");
						
						break;
					default: exibirMensagem("Atenção", "É preciso selecionar o tipo de medalha");
					}
				}
			});
		}
	}

	private void carregaMedalhasDoAtleta(int id_atleta){
		if(buscarDados(id_atleta)){
			cursor.moveToFirst();	//Aponta para a primeira linha da tabela
			count=cursor.getCount();	//Guarda o número atual de medalhas
			
			//Loop criado para ler o banco e apresentar as medalhas na tela
			
			for(int i = 0; i<cursor.getCount(); i++){
				
				/*Caso existam 7 medalhas em uma mesma linha, as 
				 * próximas serão adicionadas na linha logo abaixo
				 * */
				
				if(i==7){
					tbRow = (TableRow) findViewById(R.id.tbRow2);
				}
				if(i==14){
					tbRow = (TableRow) findViewById(R.id.tbRow3);
				}
				if(i==21){
					tbRow = (TableRow) findViewById(R.id.tbRow4);
				}
				if(i==28){
					tbRow = (TableRow) findViewById(R.id.tbRow5);
				}
				
				//Cria um novo elemento ImageView para a nova medalha adicionada
				
				final ImageView medalha = new ImageView(Premios.this);
				
				/*Se o valor da coluna tipo for 0, a medalha é de prata;
				 * se for 1, é bronze; se for 2, ouro.
				 * */
				
				medalha.setTag(cursor.getPosition());
				
				switch(cursor.getInt(cursor.getColumnIndex("tipo"))){
				case 0: medalha.setImageResource(R.drawable.silver_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					ivArray.add(medalha);
					
					eventoClicaMedalha(medalha,"Medalha de prata");
					
					break;
				case 1: medalha.setImageResource(R.drawable.bronze_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					ivArray.add(medalha);
					
					eventoClicaMedalha(medalha,"Medalha de bronze");
					
					break;
				case 2: medalha.setImageResource(R.drawable.gold_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					ivArray.add(medalha);
					
					eventoClicaMedalha(medalha,"Medalha de ouro");
					
					break;
				}
				cursor.moveToNext();				
			}
		}
	}
	
	
	//Busca a tablea com todas as medalhas de determinado atleta
	
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
	
	private boolean buscarDados(int id_atleta){
		try{
			cursor = bancoDados.query("premios", 
					new String [] {"id","id_atleta","nome","tipo"}, 
					"id_atleta = "+Integer.toString(id_atleta),//selection,
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
	
	//Exibe caixa de diálogo com um único botão
	
	public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(Premios.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
	
	// Exibe caixa de diálogo com botão extra; é necessário passar o evento listener
	
	public void exibirMensagem(String titulo, String texto, String extraButton, 
			DialogInterface.OnClickListener listener){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(Premios.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.setNegativeButton(extraButton, listener);
		mensagem.show();
	}
	
	//Método utilizado para lidar com o evento de click nas medalhas
	
	public void eventoClicaMedalha(ImageView medalha, String tipoMedalha){
		final String tipo = tipoMedalha;
		
		medalha.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					final ImageView mdAux = (ImageView) v;
					cursor.moveToPosition((Integer)v.getTag());
					exibirMensagem(tipo, "Evento: "
							+cursor.getString(cursor.getColumnIndex("nome")),
							"Remover", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									mdAux.setVisibility(View.GONE);
									
									int pos = 0;
									ImageView ivAux = null;
									
									//Corrige o índice (tag) das medalhas
									for(int i = 0;i<ivArray.size();i++){
										ivAux = (ImageView) ivArray.get(i);
										if(ivAux.getTag()==mdAux.getTag()){
											pos=i;
											//exibirMensagem("Tag",Integer.toString((Integer)ivAux.getTag()));
											for(int j = pos+1;j<ivArray.size();j++){
												ivAux = (ImageView) ivArray.get(j);
												ivAux.setTag((
														(Integer)ivAux.getTag())-1);
											}
											break;
										}
									}
									
									bancoDados.delete("premios", 
											"id = "+Integer.toString(cursor.getInt(
													cursor.getColumnIndex("id"))), 
											null);
									buscarDados(spnArrayId.get(index));
									if(count<=7){
										tbRow = (TableRow) findViewById(R.id.tbRow1);
									}
									if(count<=14&&count>7){
										if(pos<7){
											ajustaTabela("2 > 1");
										}
										tbRow = (TableRow) findViewById(R.id.tbRow2);
									}
									if(count<=21&&count>14){
										if(pos<7){
											ajustaTabela("2 > 1");
											ajustaTabela("3 > 2");
										}else{
											if(pos>=7&&pos<14){
												ajustaTabela("3 > 2");
											}
										}
										tbRow = (TableRow) findViewById(R.id.tbRow3);
									}
									if(count<=28&&count>21){
										if(pos<7){
											ajustaTabela("2 > 1");
											ajustaTabela("3 > 2");
											ajustaTabela("4 > 3");
										}else{
											if(pos>=7&&pos<14){
												ajustaTabela("3 > 2");
												ajustaTabela("4 > 3");
											}else{
												if(pos>=14&&pos<21){
													ajustaTabela("4 > 3");
												}
											}
										}
										tbRow = (TableRow) findViewById(R.id.tbRow4);
									}
									if(count>28){
										if(pos<7){
											ajustaTabela("2 > 1");
											ajustaTabela("3 > 2");
											ajustaTabela("4 > 3");
											ajustaTabela("5 > 4");
										}else{
											if(pos>=7&&pos<14){
												ajustaTabela("3 > 2");
												ajustaTabela("4 > 3");
												ajustaTabela("5 > 4");
											}else{
												if(pos>=14&&pos<21){
													ajustaTabela("4 > 3");
													ajustaTabela("5 > 4");
												}else{
													if(pos>=21&&pos<28){
														ajustaTabela("5 > 4");
													}
												}
											}
										}
										tbRow = (TableRow) findViewById(R.id.tbRow5);
									}
									ivArray.remove(pos);
									count--;
								}
							});
				}catch(Exception erro){
					exibirMensagem("Erro","Erro: "+erro.getMessage());
				}
			}
		});
	}
	
	public void ajustaTabela(String troca){
		//Retira medalha da linha 2 e coloca na 1
		if(troca.toString().equals("2 > 1")){
			try{
				tbRow = (TableRow) findViewById(R.id.tbRow2);
				tbRow.removeView((View)ivArray.get(7));
				tbRow = (TableRow) findViewById(R.id.tbRow1);
				tbRow.addView(ivArray.get(7));
			}catch(Exception erro){
				exibirMensagem("Erro", "Erro: "+erro.getMessage());
			}
		}
		//Retira medalha da linha 3 e coloca na 2
		if(troca.toString().equals("3 > 2")){
			try{
				tbRow = (TableRow) findViewById(R.id.tbRow3);
				tbRow.removeView((View)ivArray.get(14));
				tbRow = (TableRow) findViewById(R.id.tbRow2);
				tbRow.addView(ivArray.get(14));
			}catch(Exception erro){
				exibirMensagem("Erro", "Erro: "+erro.getMessage());
			}
		}
		//Retira medalha da linha 4 e coloca na 3
		if(troca.toString().equals("4 > 3")){
			try{
				tbRow = (TableRow) findViewById(R.id.tbRow4);
				tbRow.removeView((View)ivArray.get(21));
				tbRow = (TableRow) findViewById(R.id.tbRow3);
				tbRow.addView(ivArray.get(21));
			}catch(Exception erro){
				exibirMensagem("Erro", "Erro: "+erro.getMessage());
			}
		}
		//Retira medalha da linha 5 e coloca na 4
		if(troca.toString().equals("5 > 4")){
			try{
				tbRow = (TableRow) findViewById(R.id.tbRow5);
				tbRow.removeView((View)ivArray.get(28));
				tbRow = (TableRow) findViewById(R.id.tbRow4);
				tbRow.addView(ivArray.get(28));
			}catch(Exception erro){
				exibirMensagem("Erro", "Erro: "+erro.getMessage());
			}
		}
	}
	
}
