package com.ufba.swimin;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Athlete;
import com.ufba.swimin.model.Coach;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCoachActivity extends Activity {

    DatabaseHelper db;
    Cursor cursor;

    EditText etName, etBirthday;
    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_coach);

        db = new DatabaseHelper(getApplicationContext());
        etName = (EditText) findViewById(R.id.coach_name);
        etBirthday = (EditText) findViewById(R.id.coach_birthday);
        btSave = (Button) findViewById(R.id.coach_save);

        btSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(etName.getText().toString().equals("")){
                    exibirMensagem("Aviso", "Preencha o nome do treinador.");
                    return;
                }

                if(etBirthday.getText().toString().equals("")){
                    exibirMensagem("Aviso", "Preencha a data de nascimento do treinador.");
                    return;
                }

                Date birthday = new Date();
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    birthday = dateFormat.parse(etBirthday.getText().toString());
                } catch (Exception e) {
                    exibirMensagem("Aviso:", "Formato de data deve ser d/m/y");
                }

                Coach co = new Coach(
                    etName.getText().toString(),
                    birthday
                );

                db.addCoach(co);
                Toast.makeText(getApplicationContext(), "Perfil Atualizado.", Toast.LENGTH_SHORT).show();
                etName.setText("");
                etBirthday.setText("");
            }
        });
    }

    public void exibirMensagem(String titulo, String texto){
        AlertDialog.Builder mensagem = new AlertDialog.Builder(AddCoachActivity.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton("Salvar", null);
        mensagem.show();
    }
}
