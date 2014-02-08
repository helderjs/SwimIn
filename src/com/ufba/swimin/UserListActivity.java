package com.ufba.swimin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Athlete;

public class UserListActivity extends Activity {

	ListView users;
    ArrayAdapter<String> adapter;
    EditText etNome;
	ArrayList<String> lvArray;
    ArrayList<Long> lvArrayId;

    DatabaseHelper db;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        db = new DatabaseHelper(this);

        lvArray = new ArrayList<String>();
        lvArrayId = new ArrayList<Long>();
        List<Athlete> athletes = db.getAllAthletes();

        for (Athlete ath : athletes) {
            lvArray.add(ath.getName());
            lvArrayId.add(ath.getId());
        }
        
        users = (ListView) findViewById(R.id.listView);
        etNome = (EditText) findViewById(R.id.etNome);

        adapter = new ArrayAdapter<String>(UserListActivity.this,
                android.R.layout.simple_list_item_1, lvArray);
        users.setAdapter(adapter);

        etNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                UserListActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Context context = view.getContext();
                Long listItemId = lvArrayId.get(position);

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("person_id", String.valueOf(listItemId));
                intent.putExtra("person_type", "ATHLETE");
                startActivity(intent);
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
}

