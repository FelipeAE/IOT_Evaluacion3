package com.example.evaluacion3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private EditText etDateTime, etName, etRut, etIncidentDescription;
    private Spinner spinnerLab;
    private Button btnRecordIncident, btnViewIncidents;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isVertical = false;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDateTime = findViewById(R.id.tvDateTime);
        etName = findViewById(R.id.etName);
        etRut = findViewById(R.id.etRut);
        etIncidentDescription = findViewById(R.id.etIncidentDescription);
        spinnerLab = findViewById(R.id.spinnerLab);
        btnRecordIncident = findViewById(R.id.btnRecordIncident);
        btnViewIncidents = findViewById(R.id.btnViewIncidents);

        db = new DatabaseHelper(this);

        setCurrentDateTime();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        btnRecordIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordIncident();
            }
        });

        btnViewIncidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewIncidentsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCurrentDateTime() {
        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault()).format(new Date());
        etDateTime.setText(currentDateTime);
        etDateTime.setEnabled(false);
    }

    private void recordIncident() {
        String laboratorio = spinnerLab.getSelectedItem().toString();
        String name = etName.getText().toString().trim();
        String rut = etRut.getText().toString().trim();
        String incidentDescription = etIncidentDescription.getText().toString().trim();
        String dateTime = etDateTime.getText().toString();

        if (name.isEmpty() || rut.isEmpty() || incidentDescription.isEmpty() || laboratorio.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else if (validacionRUT(rut)) {
            Incident incident = new Incident(0, laboratorio, dateTime, name, rut, incidentDescription);
            db.addIncident(incident);
            Toast.makeText(MainActivity.this, "Incidente registrado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "RUT inválido", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validacionRUT(String rut) {
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "").replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        if (Math.abs(x) < 3) {
            if (!isVertical) {
                setCurrentDateTime();
                isVertical = true;
                recordIncident();
            }
        } else {
            isVertical = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
