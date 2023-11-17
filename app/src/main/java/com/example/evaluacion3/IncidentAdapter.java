package com.example.evaluacion3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class IncidentAdapter extends BaseAdapter {
    private Context context;
    private List<Incident> incidentList;

    public IncidentAdapter(Context context, List<Incident> incidentList) {
        this.context = context;
        this.incidentList = incidentList;
    }

    @Override
    public int getCount() {
        return incidentList.size();
    }

    @Override
    public Object getItem(int position) {
        return incidentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_incident, parent, false);
        }

        TextView tvLaboratorio = convertView.findViewById(R.id.tvLaboratorio);
        TextView tvDateTime = convertView.findViewById(R.id.tvDateTime);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvRut = convertView.findViewById(R.id.tvRut);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);

        Incident incident = incidentList.get(position);

        tvLaboratorio.setText(incident.getLaboratorio());
        tvDateTime.setText(incident.getFechaHora());
        tvName.setText(incident.getNombre());
        tvRut.setText(incident.getRut());
        tvDescription.setText(incident.getDescripcion());

        return convertView;
    }
}

