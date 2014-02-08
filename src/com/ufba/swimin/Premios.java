package com.ufba.swimin;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Award;

public class Premios extends Activity {
	DatabaseHelper db;
    Bundle extras;
	TableLayout tbMedalhas;
	Button btAdicionar;
	TableRow tbRow = null;
	ImageView ivMedalha;
    List<Award> awards;
    Award removeAward;
	
	ArrayList <ImageView> ivArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premios);
		
		db = new DatabaseHelper(this);
        extras = getIntent().getExtras();
		
		tbMedalhas = (TableLayout) findViewById(R.id.tbMedalhas);
		btAdicionar = (Button) findViewById(R.id.btAdicionar);
	    ivArray = new ArrayList<ImageView>();
		
		//O ImageView ivMedalha1 serve para passamarmos seus atributos,
		//como largura e altura, as outras medalhas - isso � feito com
		//ivMedalha.getLayoutParams()
        ivMedalha = (ImageView) findViewById(R.id.ivMedalha1);
        ivMedalha.setVisibility(View.GONE);
        loadAwards();

        btAdicionar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                EditText etNome = (EditText) findViewById(R.id.etNome);
                RadioGroup rgMedalha = (RadioGroup) findViewById(R.id.rgMedalha);
                int rbId = rgMedalha.getCheckedRadioButtonId();
                int rbSelecionado = rgMedalha.indexOfChild(rgMedalha.findViewById(rbId));

                Award aw = new Award(
                    Long.parseLong(extras.getString("person_id")),
                    etNome.getText().toString(),
                    rbSelecionado
                );

                db.addAward(aw);
                loadAwards();
                Toast.makeText(getApplicationContext(), "Premiçao adiconada com sucesso.", Toast.LENGTH_LONG).show();
            }
        });
	}

	private void loadAwards() {
        TableRow row = (TableRow) findViewById(R.id.tbRow1);
        row.removeAllViews();
        row = (TableRow) findViewById(R.id.tbRow2);
        row.removeAllViews();
        row = (TableRow) findViewById(R.id.tbRow3);
        row.removeAllViews();
        row = (TableRow) findViewById(R.id.tbRow4);
        row.removeAllViews();
        row = (TableRow) findViewById(R.id.tbRow5);
        row.removeAllViews();

        tbRow = (TableRow) findViewById(R.id.tbRow1);

        awards = db.getAwards(extras.getString("person_id"));

        if (awards.size() > 0) {
			for (int i = 0; i < awards.size(); i++) {
				Award award = awards.get(i);
				/*Caso existam 7 medalhas em uma mesma linha, as 
				 * pr�ximas ser�o adicionadas na linha logo abaixo
				 * */
				
				if (i == 7) {
					tbRow = (TableRow) findViewById(R.id.tbRow2);
				}

				if (i ==14) {
					tbRow = (TableRow) findViewById(R.id.tbRow3);
				}

				if (i == 21) {
					tbRow = (TableRow) findViewById(R.id.tbRow4);
				}

				if (i == 28) {
					tbRow = (TableRow) findViewById(R.id.tbRow5);
				}
				
				//Cria um novo elemento ImageView para a nova medalha adicionada
				
				ImageView medalha = new ImageView(Premios.this);
				
				/*Se o valor da coluna tipo for 0, a medalha � de prata;
				 * se for 1, � bronze; se for 2, ouro.
				 * */
				
				medalha.setTag(i);
				
				switch(award.getType()){
                    case 0:
                        medalha.setImageResource(R.drawable.silver_medal);
                        medalha.setLayoutParams(ivMedalha.getLayoutParams());
                        tbRow.addView(medalha);
                        ivArray.add(medalha);

                        eventoClicaMedalha(medalha, "Medalha de prata");

                        break;
                    case 1: medalha.setImageResource(R.drawable.bronze_medal);
                        medalha.setLayoutParams(ivMedalha.getLayoutParams());
                        tbRow.addView(medalha);
                        ivArray.add(medalha);

                        eventoClicaMedalha(medalha, "Medalha de bronze");

                        break;
                    case 2: medalha.setImageResource(R.drawable.gold_medal);
                        medalha.setLayoutParams(ivMedalha.getLayoutParams());
                        tbRow.addView(medalha);
                        ivArray.add(medalha);

                        eventoClicaMedalha(medalha, "Medalha de ouro");

                        break;
				}
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Exibe caixa de di�logo com um �nico bot�o
	
	public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(Premios.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.show();
	}
	
	// Exibe caixa de di�logo com bot�o extra; � necess�rio passar o evento listener
	
	public void exibirMensagem(String titulo, String texto, String extraButton, 
			DialogInterface.OnClickListener listener){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(Premios.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Ok", null);
		mensagem.setNegativeButton(extraButton, listener);
		mensagem.show();
	}
	
	//M�todo utilizado para lidar com o evento de click nas medalhas
	
	public void eventoClicaMedalha(ImageView medalha, String tipoMedalha) {
		final String tipo = tipoMedalha;
		
		medalha.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					final ImageView mdAux = (ImageView) v;
					removeAward = awards.get((Integer) v.getTag());
					exibirMensagem(tipo, "Evento: "
							+ removeAward.getName(),
							"Remover", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									mdAux.setVisibility(View.GONE);
									
									int pos = 0;
									ImageView ivAux = null;
									
									//Corrige o �ndice (tag) das medalhas
									for (int i = 0; i < ivArray.size(); i++) {
										ivAux = (ImageView) ivArray.get(i);
										if (ivAux.getTag() == mdAux.getTag()) {
											pos = i;
											for (int j = pos + 1; j<ivArray.size(); j++) {
												ivAux = (ImageView) ivArray.get(j);
												ivAux.setTag(((Integer)ivAux.getTag())-1);
											}

											break;
										}
									}
									
									db.deleteAward(removeAward);
                                    loadAwards();
								}
							});
				} catch(Exception erro) {
					exibirMensagem("Erro","Erro: "+erro.getMessage());
				}
			}
		});
	}
}
