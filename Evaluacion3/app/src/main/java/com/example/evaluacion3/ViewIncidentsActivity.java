package com.example.evaluacion3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewIncidentsActivity extends AppCompatActivity {

    private ListView listViewIncidents;
    private DatabaseHelper db;
    private IncidentAdapter incidentAdapter;
    private List<Incident> incidentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incidents);

        listViewIncidents = findViewById(R.id.listViewIncidents);
        db = new DatabaseHelper(this);

        incidentList = db.getAllIncidents();
        incidentAdapter = new IncidentAdapter(this, incidentList);
        listViewIncidents.setAdapter(incidentAdapter);

        listViewIncidents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Incident selectedIncident = incidentList.get(position);
                Intent intent = new Intent(ViewIncidentsActivity.this, IncidentDetailActivity.class);
                intent.putExtra("incidentId", selectedIncident.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        incidentList.clear();
        incidentList.addAll(db.getAllIncidents());
        incidentAdapter.notifyDataSetChanged();
    }
}
