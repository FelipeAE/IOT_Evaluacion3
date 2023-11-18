package com.example.evaluacion3;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class IncidentDetailActivity extends AppCompatActivity {

    private Spinner spinnerLaboratorio;
    private EditText etDateTime, etName, etRut, etDescription;
    private Button btnUpdate, btnDelete;
    private DatabaseHelper db;
    private int incidentId;
    private Incident currentIncident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_detail);

        db = new DatabaseHelper(this);

        spinnerLaboratorio = findViewById(R.id.spinnerLaboratorio);
        etDateTime = findViewById(R.id.etDateTime);
        etName = findViewById(R.id.etName);
        etRut = findViewById(R.id.etRut);
        etDescription = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.laboratory_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLaboratorio.setAdapter(adapter);

        incidentId = getIntent().getIntExtra("incidentId", -1);
        if (incidentId != -1) {
            loadIncidentData(incidentId);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIncident();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteIncident();
            }
        });
    }

    private void loadIncidentData(int id) {
        currentIncident = db.getIncident(id);
        if (currentIncident != null) {
            int spinnerPosition = ((ArrayAdapter<CharSequence>) spinnerLaboratorio.getAdapter()).getPosition(currentIncident.getLaboratorio());
            spinnerLaboratorio.setSelection(spinnerPosition);
            etDateTime.setText(currentIncident.getFechaHora());
            etName.setText(currentIncident.getNombre());
            etRut.setText(currentIncident.getRut());
            etDescription.setText(currentIncident.getDescripcion());
        }
    }

    private void updateIncident() {
        if (currentIncident != null) {
            String laboratorio = spinnerLaboratorio.getSelectedItem().toString();
            String dateTime = etDateTime.getText().toString();
            String name = etName.getText().toString();
            String rut = etRut.getText().toString();
            String description = etDescription.getText().toString();

            Incident updatedIncident = new Incident(incidentId, laboratorio, dateTime, name, rut, description);
            int updateCount = db.updateIncident(updatedIncident);

            if (updateCount > 0) {
                Toast.makeText(IncidentDetailActivity.this, "Incidente actualizado con éxito", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(IncidentDetailActivity.this, "Error al actualizar el incidente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteIncident() {
        if (currentIncident != null) {
            db.deleteIncident(incidentId);
            Toast.makeText(IncidentDetailActivity.this, "Incidente eliminado con éxito", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

