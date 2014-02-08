package com.ufba.swimin;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Athlete;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAthleteActivity extends Activity {

    DatabaseHelper db;
    Cursor cursor;

    EditText etName, etBirthday, etWeight, etHeight;
    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_athlete);

        db = new DatabaseHelper(getApplicationContext());
        etName = (EditText) findViewById(R.id.athlete_name);
        etBirthday = (EditText) findViewById(R.id.athlete_birthday);
        etWeight = (EditText) findViewById(R.id.athlete_weight);
        etHeight = (EditText) findViewById(R.id.athlete_height);
        btSave = (Button) findViewById(R.id.athlete_save);

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

                if(etWeight.getText().toString().equals("")){
                    exibirMensagem("Aviso", "Informe o peso do atleta");
                    return;
                }

                if(etHeight.getText().toString().equals("")){
                    exibirMensagem("Aviso", "Informe a altura do atleta");
                    return;
                }

                Date birthday = new Date();
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    birthday = dateFormat.parse(etBirthday.getText().toString());
                } catch (Exception e) {
                    exibirMensagem("Aviso:", "Formato de data deve ser d/m/y");
                }

                Athlete ath = new Athlete(
                    etName.getText().toString(),
                    birthday,
                    Float.valueOf(etWeight.getText().toString()),
                    Float.valueOf(etHeight.getText().toString())
                );


                db.addAthlete(ath);
                exibirMensagem("Mensagen: ", "Atleta adicionado.");
                etName.setText("");
                etBirthday.setText("");
                etWeight.setText("");
                etHeight.setText("");
            }
        });
    }

    public void exibirMensagem(String titulo, String texto){
        AlertDialog.Builder mensagem = new AlertDialog.Builder(AddAthleteActivity.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton("Fechar", null);
        mensagem.show();
    }
}
