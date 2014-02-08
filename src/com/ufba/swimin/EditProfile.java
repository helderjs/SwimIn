package com.ufba.swimin;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Athlete;
import com.ufba.swimin.model.Coach;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfile extends Activity {
    DatabaseHelper db;
    Bundle extras;

    TextView tvWeight, tvHeight;
	EditText etName, etBirthday, etWeight, etHeight;
	Button btSave;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);

        db = new DatabaseHelper(this);
        extras = getIntent().getExtras();
		etName = (EditText) findViewById(R.id.name);
		etBirthday = (EditText) findViewById(R.id.birthday);
        tvWeight = (TextView) findViewById(R.id.title_weight);
		etWeight = (EditText) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.title_height);
        etHeight = (EditText) findViewById(R.id.height);
		btSave = (Button) findViewById(R.id.btSave);

	    carregaDadosNaTela();

        btSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(etName.getText().toString().equals("")){
                    exibirMensagem("Aviso", "Preencha o nome do atleta.");
                    return;
                }

                if(etBirthday.getText().toString().equals("")){
                    exibirMensagem("Aviso", "Preencha a data de nascimento do atleta.");
                    return;
                }

                if (!extras.getString("person_type").equals("COACH")) {
                    if(etWeight.getText().toString().equals("")){
                        exibirMensagem("Aviso", "Informe o peso do atleta");
                        return;
                    }

                    if(etHeight.getText().toString().equals("")){
                        exibirMensagem("Aviso", "Informe a altura do atleta");
                        return;
                    }
                }

                Date birthday = new Date();
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    birthday = dateFormat.parse(etBirthday.getText().toString());
                } catch (Exception e) {
                    exibirMensagem("Aviso:", "Formato de data deve ser d/m/y");
                }

                if (extras.getString("person_type").equals("COACH")) {
                    Coach co = new Coach(1, etName.getText().toString(), birthday);

                    db.updateCoach(co);
                } else {
                    Athlete ath = new Athlete(
                        Long.valueOf(extras.getString("person_id")),
                        etName.getText().toString(),
                        birthday,
                        Float.valueOf(etWeight.getText().toString()),
                        Float.valueOf(etHeight.getText().toString())
                    );

                    db.updateAthlete(ath);
                }

                Toast.makeText(getApplicationContext(), "Perfil Atualizado.", Toast.LENGTH_SHORT).show();
                etName.setText("");
                etBirthday.setText("");
                etWeight.setText("");
                etHeight.setText("");
                finish();
            }
        });
	}
	
	public void carregaDadosNaTela() {
        if (extras.getString("person_type").equals("COACH")) {
            Coach ps = db.getCoach();
            etName.setText(ps.getName());
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            etBirthday.setText(dt.format(ps.getBirthday()));
            tvWeight.setVisibility(View.GONE);
            tvHeight.setVisibility(View.GONE);
            etWeight.setVisibility(View.GONE);
            etHeight.setVisibility(View.GONE);
        } else {
            Athlete ps = db.getAthlete(Long.valueOf(extras.getString("person_id")));
            etName.setText(ps.getName());
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            etBirthday.setText(dt.format(ps.getBirthday()));
            etWeight.setText(String.valueOf(ps.getWeight()));
            etHeight.setText(String.valueOf(ps.getHeight()));
        }
	}
	
	public void exibirMensagem(String titulo, String texto){
		AlertDialog.Builder mensagem = new AlertDialog.Builder(EditProfile.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("Fechar", null);
		mensagem.show();
	}
	
}
