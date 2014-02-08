package com.ufba.swimin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ufba.swimin.model.Athlete;
import com.ufba.swimin.model.Award;
import com.ufba.swimin.model.Coach;
import com.ufba.swimin.model.Person;
import com.ufba.swimin.model.Training;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "swimInDB";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS people "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, birthday TEXT, "
                + "weight REAL, height REAL, type INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS trainings "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_athlete INTEGER, swim_type TEXT, swim_time TEXT, "
                + "swim_date TEXT, distance TEXT, "
                + "FOREIGN KEY (id_athlete) REFERENCES people(id))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS awards "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_athlete INTEGER, name TEXT, type INTEGER, "
                + "FOREIGN KEY (id_athlete) REFERENCES people(id))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS pessoas "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, data_nasc TEXT, endereco TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS atletas "
                + "(id INTEGER PRIMARY KEY REFERENCES pessoas (id), "
                + "peso REAL, altura REAL)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS treinadores "
                + "(id INTEGER PRIMARY KEY REFERENCES pessoas(id), "
                + "numero_atletas INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS atletas_treinadores "
                + "(id_atleta INTEGER PRIMARY KEY REFERENCES atletas(id), "
                + "id_treinador INTEGER, "
                + "FOREIGN KEY (id_treinador) REFERENCES treinadores(id))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS treinos "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_treinador INTEGER, "
                + "id_atleta INTEGER, tipo_nado TEXT, metros REAL, tempo TEXT, "
                + "data TEXT, hora TEXT, "
                + "FOREIGN KEY (id_treinador) REFERENCES treinadores(id), "
                + "FOREIGN KEY (id_atleta) REFERENCES atletas(id))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS premios "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_atleta INTEGER, nome TEXT, tipo INTEGER, "
                + "FOREIGN KEY (id_atleta) REFERENCES atletas(id))";
        db.execSQL(sql);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS people");
        db.execSQL("DROP TABLE IF EXISTS trainings");
        db.execSQL("DROP TABLE IF EXISTS pessoas");
        db.execSQL("DROP TABLE IF EXISTS atletas");
        db.execSQL("DROP TABLE IF EXISTS treinadores");
        db.execSQL("DROP TABLE IF EXISTS atletas_treinadores");
        db.execSQL("DROP TABLE IF EXISTS treinos");
        db.execSQL("DROP TABLE IF EXISTS premios");

        // Create tables again
        onCreate(db);
    }

    public Athlete addAthlete(Athlete ath) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", ath.getName());

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        values.put("birthday", dt.format(ath.getBirthday()));

        values.put("weight", ath.getWeight());
        values.put("height", ath.getHeight());
        values.put("type", 2);

        long id = db.insert("people", null, values);
        ath.setId(id);
        db.close();

        return ath;
    }

    public Coach addCoach(Coach co) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", co.getName());

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        values.put("birthday", dt.format(co.getBirthday()));

        values.put("type", 1);

        long id = db.insert("people", null, values);
        co.setId(id);
        db.close();

        return co;
    }

    public List<Athlete> getAllAthletes() {
        List<Athlete> AthleteList = new ArrayList<Athlete>();
        String selectQuery = "SELECT  * FROM people WHERE type = 2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Athlete ath = new Athlete();
                ath.setId(Integer.parseInt(cursor.getString(0)));
                ath.setName(cursor.getString(1));

                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date birthday = dateFormat.parse(cursor.getString(2).toString());
                    ath.setBirthday(birthday);
                } catch (Exception e) {
                    Log.d("Erro: ", e.getMessage());
                }

                ath.setWeight(cursor.getFloat(3));
                ath.setHeight(cursor.getFloat(4));

                AthleteList.add(ath);
            } while (cursor.moveToNext());
        }

        return AthleteList;
    }

    public Coach getCoach() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
            "people",
            new String[] { "id", "name", "birthday" },
            "type=?",
            new String[] { String.valueOf(1) },
            null,
            null,
            null,
            null
        );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Coach co = new Coach();
        co.setId(Integer.parseInt(cursor.getString(0)));
        co.setName(cursor.getString(1));

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date birthday = dateFormat.parse(cursor.getString(2).toString());
            co.setBirthday(birthday);
        } catch (Exception e) {
            Log.d("Erro: ", e.getMessage());
        }

        return co;
    }

    public Person getPerson(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "people",
                new String[] { "id", "name", "birthday", "weight", "height", "type" },
                "id=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        if (Integer.parseInt(cursor.getString(5)) == 1) {
            Coach ps = new Coach();
            ps.setId(Integer.parseInt(cursor.getString(0)));
            ps.setName(cursor.getString(1));

            try {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date birthday = dateFormat.parse(cursor.getString(2).toString());
                ps.setBirthday(birthday);
            } catch (Exception e) {
                Log.d("Erro: ", e.getMessage());
            }

            return ps;
        } else {
            Athlete ps = new Athlete();
            ps.setId(Integer.parseInt(cursor.getString(0)));
            ps.setName(cursor.getString(1));

            try {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date birthday = dateFormat.parse(cursor.getString(2).toString());
                ps.setBirthday(birthday);
            } catch (Exception e) {
                Log.d("Erro: ", e.getMessage());
            }

            ps.setWeight(cursor.getFloat(3));
            ps.setHeight(cursor.getFloat(4));

            return ps;
        }
    }

    public Athlete getAthlete(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
            "people",
            new String[] { "id", "name", "birthday", "weight", "height", "type" },
            "id=?",
            new String[] { String.valueOf(id) },
            null,
            null,
            null,
            null
        );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Athlete ath = new Athlete();
        ath.setId(Integer.parseInt(cursor.getString(0)));
        ath.setName(cursor.getString(1));

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date birthday = dateFormat.parse(cursor.getString(2).toString());
            ath.setBirthday(birthday);
        } catch (Exception e) {
            Log.d("Erro: ", e.getMessage());
        }

        ath.setWeight(cursor.getFloat(3));
        ath.setHeight(cursor.getFloat(4));

        return ath;
    }

    public int updateCoach(Coach co) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", co.getName());

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        values.put("birthday", dt.format(co.getBirthday()));

        values.put("type", 1);

        // updating row
        return db.update("people", values, "id = ?", new String[] { String.valueOf(co.getId()) });
    }

    public int updateAthlete(Athlete ath) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", ath.getName());

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        values.put("birthday", dt.format(ath.getBirthday()));

        values.put("weight", ath.getWeight());
        values.put("height", ath.getHeight());
        values.put("type", 2);

        // updating row
        return db.update("people", values, "id = ?", new String[] { String.valueOf(ath.getId()) });
    }

    public Training addTraining(Training train) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_athlete", train.getAthlete_id());
        values.put("swim_type", train.getType());
        values.put("swim_time", train.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        values.put("swim_date", dt.format(train.getDate()));
        values.put("distance", train.getDistance());

        long id = db.insert("trainings", null, values);
        train.setId(id);
        db.close();

        return train;
    }

    public List<Training> getTrainings(String athleteId) {
        List<Training> TrainingList = new ArrayList<Training>();
        String selectQuery = "SELECT * FROM trainings WHERE id_athlete = " + athleteId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Training train = new Training();
                train.setId(Long.parseLong(cursor.getString(0)));
                train.setAthlete_id(cursor.getLong(1));
                train.setType(cursor.getString(2));
                train.setTime(cursor.getLong(3));

                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = dateFormat.parse(cursor.getString(4).toString());
                    train.setDate(date);
                } catch (Exception e) {
                    Log.d("Erro: ", e.getMessage());
                }

                train.setDistance(cursor.getString(5));

                TrainingList.add(train);
            } while (cursor.moveToNext());
        }

        return TrainingList;
    }

    public Award addAward(Award aw) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_athlete", aw.getAthlete_id());
        values.put("name", aw.getName());
        values.put("type", aw.getType());

        long id = db.insert("awards", null, values);
        aw.setId(id);
        db.close();

        return aw;
    }

    public List<Award> getAwards(String athleteId) {
        List<Award> AwardList = new ArrayList<Award>();
        String selectQuery = "SELECT * FROM awards WHERE id_athlete = " + athleteId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Award aw = new Award();
                aw.setId(Long.parseLong(cursor.getString(0)));
                aw.setAthlete_id(cursor.getLong(1));
                aw.setName(cursor.getString(2));
                aw.setType(cursor.getInt(3));

                AwardList.add(aw);
            } while (cursor.moveToNext());
        }

        return AwardList;
    }

    public void deleteAward(Award award) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("awards", "id = ?",
                new String[] { String.valueOf(award.getId()) });
        db.close();
    }
}
