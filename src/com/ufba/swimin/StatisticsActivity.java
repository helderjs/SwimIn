package com.ufba.swimin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TextView;

import com.ufba.swimin.helper.DatabaseHelper;
import com.ufba.swimin.model.Training;

import java.util.List;

public class StatisticsActivity extends Activity {
    DatabaseHelper db;
    Bundle extras;
    TextView crawlMax;
    TextView crawlMin;
    TextView crawlMed;
    TextView PeitoMax;
    TextView PeitoMin;
    TextView PeitoMed;
    TextView CostaMax;
    TextView CostaMin;
    TextView CostaMed;
    TextView BorboletaMax;
    TextView BorboletaMin;
    TextView BorboletaMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        crawlMax = (TextView) findViewById(R.id.maxTimeCrawl);
        crawlMin = (TextView) findViewById(R.id.minTimeCrawl);
        crawlMed = (TextView) findViewById(R.id.medTimeCrawl);

        PeitoMax = (TextView) findViewById(R.id.maxTimePeito);
        PeitoMin = (TextView) findViewById(R.id.minTimePeito);
        PeitoMed = (TextView) findViewById(R.id.medTimePeito);

        CostaMax = (TextView) findViewById(R.id.maxTimeCostas);
        CostaMin = (TextView) findViewById(R.id.minTimeCostas);
        CostaMed = (TextView) findViewById(R.id.medTimeCostas);

        BorboletaMax = (TextView) findViewById(R.id.maxTimeBorboleta);
        BorboletaMin = (TextView) findViewById(R.id.minTimeBorboleta);
        BorboletaMed = (TextView) findViewById(R.id.medTimeBorboleta);

        db = new DatabaseHelper(this);
        extras = getIntent().getExtras();
        List<Training> trainings = db.getTrainings(extras.getString("person_id"));
        Long maxCra = Long.MIN_VALUE;
        Long minCra = Long.MAX_VALUE;
        Long medCra = Long.parseLong("0");
        Integer numCra = 0;

        Long maxPei = Long.MIN_VALUE;
        Long minPei = Long.MAX_VALUE;
        Long medPei = Long.parseLong("0");
        Integer numPei = 0;

        Long maxCos = Long.MIN_VALUE;
        Long minCos = Long.MAX_VALUE;
        Long medCos = Long.parseLong("0");
        Integer numCos = 0;

        Long maxBor = Long.MIN_VALUE;
        Long minBor = Long.MAX_VALUE;
        Long medBor = Long.parseLong("0");
        Integer numBor = 0;

        for (Training training: trainings) {
            Long value = training.getTime();

            if (training.getType().equals("Crawl")) {
                if (value > maxCra) {
                    maxCra = value;
                }

                if (value < minCra) {
                    minCra = value;
                }

                medCra += value;
                numCra++;
            }

            if (training.getType().equals("Costas")) {
                if (value > maxCos) {
                    maxCos = value;
                }

                if (value < minCos) {
                    minCos = value;
                }

                medCos += value;
                numCos++;
            }

            if (training.getType().equals("Peito")) {
                if (value > maxPei) {
                    maxPei = value;
                }

                if (value < minPei) {
                    minPei = value;
                }

                medPei += value;
                numPei++;
            }

            if (training.getType().equals("Borboleta")) {
                if (value > maxBor) {
                    maxBor = value;
                }

                if (value < minBor) {
                    minBor = value;
                }

                medBor += value;
                numBor++;
            }
        }

        if (numCra > 0) {
            medCra /= numCra;

            crawlMax.setText("Maior Tempo: " + formatTime(maxCra));
            crawlMin.setText("Menor Tempo: " + formatTime(minCra));
            crawlMed.setText("Tempo Medio: " + formatTime(medCra));
        } else {
            crawlMax.setText("Maior Tempo: Sem informaçoes");
            crawlMin.setText("Menor Tempo: Sem informaçoes");
            crawlMed.setText("Tempo Medio: Sem informaçoes");
        }

        if (numCos > 0) {
            medCos /= numCos;

            CostaMax.setText("Maior Tempo: " + formatTime(maxCos));
            CostaMin.setText("Menor Tempo: " + formatTime(minCos));
            CostaMed.setText("Tempo Medio: " + formatTime(medCos));
        } else {
            CostaMax.setText("Maior Tempo: Sem informaçoes");
            CostaMin.setText("Menor Tempo: Sem informaçoes");
            CostaMed.setText("Tempo Medio: Sem informaçoes");
        }

        if (numPei > 0) {
            medPei /= numPei;

            PeitoMax.setText("Maior Tempo: " + formatTime(maxPei));
            PeitoMin.setText("Menor Tempo: " + formatTime(minPei));
            PeitoMed.setText("Tempo Medio: " + formatTime(medPei));
        } else {
            PeitoMax.setText("Maior Tempo: Sem informaçoes");
            PeitoMin.setText("Menor Tempo: Sem informaçoes");
            PeitoMed.setText("Tempo Medio: Sem informaçoes");
        }

        if (numBor > 0) {
            medBor /= numBor;

            BorboletaMax.setText("Maior Tempo: " + formatTime(maxBor));
            BorboletaMin.setText("Menor Tempo: " + formatTime(minBor));
            BorboletaMed.setText("Tempo Medio: " + formatTime(medBor));
        } else {
            BorboletaMax.setText("Maior Tempo: Sem informaçoes");
            BorboletaMin.setText("Menor Tempo: Sem informaçoes");
            BorboletaMed.setText("Tempo Medio: Sem informaçoes");
        }

        TabHost tabhost = (TabHost)findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec spec1 = tabhost.newTabSpec("Craw");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Craw");

        TabHost.TabSpec spec2 = tabhost.newTabSpec("Peito");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Peito");

        TabHost.TabSpec spec3 = tabhost.newTabSpec("Costa");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Costa");

        TabHost.TabSpec spec4 = tabhost.newTabSpec("Borboleta");
        spec4.setContent(R.id.tab4);
        spec4.setIndicator("Borboleta");

        tabhost.addTab(spec1);
        tabhost.addTab(spec2);
        tabhost.addTab(spec3);
        tabhost.addTab(spec4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public String formatTime(Long millis) {
        millis = millis * -1;

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;

        String time = String.format("%02d:%02d", minute, second);

        return time;
    }
}

