package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
		
		//O ImageView ivMedalha1 serve para passamarmos seus atributos,
		//como largura e altura, as outras medalhas - isso é feito com
		//ivMedalha.getLayoutParams()
		
		ImageView ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
		ivMedalha.setVisibility(View.GONE);
		
		if(buscarDados()){
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
				if(i==15){
					tbRow = (TableRow) findViewById(R.id.tbRow3);
				}
				if(i==22){
					tbRow = (TableRow) findViewById(R.id.tbRow4);
				}
				if(i==29){
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
					
					eventoClicaMedalha(medalha,"Medalha de prata");
					
					break;
				case 1: medalha.setImageResource(R.drawable.bronze_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					
					eventoClicaMedalha(medalha,"Medalha de bronze");
					
					break;
				case 2: medalha.setImageResource(R.drawable.gold_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					
					eventoClicaMedalha(medalha,"Medalha de ouro");
					
					break;
				}
				cursor.moveToNext();				
			}
		}
		
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
					gravarMedalhaBanco(1,etNome.getText().toString(),rbSelecionado);
					buscarDados();
					
					eventoClicaMedalha(medalha, "Medalha de prata");
					
					break;
				case 1: medalha.setImageResource(R.drawable.bronze_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					count++;
					gravarMedalhaBanco(1,etNome.getText().toString(),rbSelecionado);
					buscarDados();
					
					eventoClicaMedalha(medalha, "Medalha de bronze");
					
					break;
				case 2: medalha.setImageResource(R.drawable.gold_medal);
					medalha.setLayoutParams(ivMedalha.getLayoutParams());
					tbRow.addView(medalha);
					count++;
					gravarMedalhaBanco(1,etNome.getText().toString(),rbSelecionado);
					buscarDados();
					
					eventoClicaMedalha(medalha, "Medalha de ouro");
					
					break;
				default: exibirMensagem("Atenção", "É preciso selecionar o tipo de medalha");
				}
			}
		});
	}

	//Busca a tablea com todas as medalhas de determinado atleta
	
	private boolean buscarDados(){
		try{
			cursor = bancoDados.query("premios", 
					new String [] {"id","id_atleta","nome","tipo"}, 
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
									bancoDados.delete("premios", 
											"id = "+Integer.toString(cursor.getInt(
													cursor.getColumnIndex("id"))), 
											null);
								}
							});
				}catch(Exception erro){
					exibirMensagem("Erro","Erro: "+erro.getMessage());
				}
			}
		});
	}
	
}
