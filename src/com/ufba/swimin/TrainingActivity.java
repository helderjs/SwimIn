package com.ufba.swimin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Athlete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingActivity extends Activity implements OnItemSelectedListener {

    DatabaseHelper db;
	EditText distance;
    TextView date;
    Spinner athleteList;
    List<String> athleteListLabel;
    List<Long> athleteListId;
    Spinner swimType;
    Button btStart;
    Long idselected;
	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training);

        db = new DatabaseHelper(this);

        date = (TextView) findViewById(R.id.training_date);
        swimType = (Spinner) findViewById(R.id.swim_type);
        athleteList = (Spinner) findViewById(R.id.athlete_list);
        distance = (EditText) findViewById(R.id.training_distance);
		btStart = (Button) findViewById(R.id.btStart);

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        date.setText("Data: " + dt.format(new Date()));

        athleteList.setOnItemSelectedListener(this);
        loadSpinnerData();

		btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distance.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "Defina os metros", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                    Intent intent = new Intent(context, CronometerActivity.class);
                    intent.putExtra("date", dt.format(new Date()));
                    intent.putExtra("type", swimType.getSelectedItem().toString());
                    intent.putExtra("athlete_id", idselected);
                    intent.putExtra("distance", distance.getText().toString());
                    startActivity(intent);
                }
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private void loadSpinnerData() {
        // Spinner Drop down elements
        List<Athlete> athletes = db.getAllAthletes();
        athleteListLabel = new ArrayList<String>();
        athleteListId = new ArrayList<Long>();

        for (Athlete ath : athletes) {
            athleteListLabel.add(ath.getName());
            athleteListId.add(ath.getId());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, athleteListLabel);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        athleteList.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        idselected = athleteListId.get(position);
        // On selecting a spinner item
        //String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "You selected: " + idselected,
        //       Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
