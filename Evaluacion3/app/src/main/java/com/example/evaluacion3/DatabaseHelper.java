package com.example.evaluacion3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "IncidentesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_INCIDENTES = "incidentes";
    private static final String KEY_ID = "id";
    private static final String KEY_LAB = "laboratorio";
    private static final String KEY_DATE_TIME = "fechaHora";
    private static final String KEY_NAME = "nombre";
    private static final String KEY_RUT = "rut";
    private static final String KEY_DESCRIPTION = "descripcion";

    private static final String CREATE_TABLE_INCIDENTES = "CREATE TABLE "
            + TABLE_INCIDENTES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAB + " TEXT," + KEY_DATE_TIME + " TEXT," + KEY_NAME + " TEXT,"
            + KEY_RUT + " TEXT," + KEY_DESCRIPTION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INCIDENTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENTES);
        onCreate(db);
    }

    public void addIncident(Incident incident) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAB, incident.getLaboratorio());
        values.put(KEY_DATE_TIME, incident.getFechaHora());
        values.put(KEY_NAME, incident.getNombre());
        values.put(KEY_RUT, incident.getRut());
        values.put(KEY_DESCRIPTION, incident.getDescripcion());

        db.insert(TABLE_INCIDENTES, null, values);
        db.close();
    }

    public List<Incident> getAllIncidents() {
        List<Incident> incidentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INCIDENTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    Incident incident = new Incident();
                    incident.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    incident.setLaboratorio(cursor.getString(cursor.getColumnIndex(KEY_LAB)));
                    incident.setFechaHora(cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME)));
                    incident.setNombre(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                    incident.setRut(cursor.getString(cursor.getColumnIndex(KEY_RUT)));
                    incident.setDescripcion(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                    incidentList.add(incident);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        db.close();
        return incidentList;
    }
    public Incident getIncident(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Incident incident = null;

        Cursor cursor = db.query(TABLE_INCIDENTES, new String[]{KEY_ID, KEY_LAB, KEY_DATE_TIME, KEY_NAME, KEY_RUT, KEY_DESCRIPTION}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            incident = new Incident(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            cursor.close();
        }

        return incident;
    }

    public int updateIncident(Incident incident) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAB, incident.getLaboratorio());
        values.put(KEY_DATE_TIME, incident.getFechaHora());
        values.put(KEY_NAME, incident.getNombre());
        values.put(KEY_RUT, incident.getRut());
        values.put(KEY_DESCRIPTION, incident.getDescripcion());

        // Actualizar fila
        return db.update(TABLE_INCIDENTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(incident.getId())});
    }

    public void deleteIncident(int incidentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INCIDENTES, KEY_ID + " = ?",
                new String[]{String.valueOf(incidentId)});
        db.close();
    }
}

class Incident {
    private int id;
    private String laboratorio, fechaHora, nombre, rut, descripcion;

    // Constructor vacío
    public Incident() {
    }

    // Constructor con parámetros
    public Incident(int id, String laboratorio, String fechaHora, String nombre, String rut, String descripcion) {
        this.id = id;
        this.laboratorio = laboratorio;
        this.fechaHora = fechaHora;
        this.nombre = nombre;
        this.rut = rut;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
