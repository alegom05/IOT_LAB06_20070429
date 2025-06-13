package com.example.lab6;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab6.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class ResumenFragment extends Fragment {

    private PieChart pieChart;
    private BarChart barChart;
    private FirebaseFirestore db;

    public ResumenFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);

        db = FirebaseFirestore.getInstance();

        obtenerDatosYMostrarGraficos();

        return view;
    }

    private void obtenerDatosYMostrarGraficos() {
        db.collection("ingresos").get().addOnSuccessListener(ingresosSnap -> {
            double totalIngresos = 0;
            for (QueryDocumentSnapshot doc : ingresosSnap) {
                Double monto = doc.getDouble("monto");
                if (monto != null) totalIngresos += monto;
            }

            db.collection("egresos").get().addOnSuccessListener(egresosSnap -> {
                double totalEgresos = 0;
                for (QueryDocumentSnapshot doc : egresosSnap) {
                    Double monto = doc.getDouble("monto");
                    if (monto != null) totalEgresos += monto;
                }

                mostrarPieChart(totalIngresos, totalEgresos);
                mostrarBarChart(totalIngresos, totalEgresos);
            });
        });
    }

    private void mostrarPieChart(double ingresos, double egresos) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (ingresos > 0) {
            float porcentajeEgresos = (float) ((egresos / ingresos) * 100.0);
            float porcentajeIngresos = 100.0f - porcentajeEgresos;

            entries.add(new PieEntry(porcentajeIngresos, "Ingresos"));
            entries.add(new PieEntry(porcentajeEgresos, "Egresos"));
        } else {
            entries.add(new PieEntry(1f, "Sin ingresos"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.GREEN, Color.RED);
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void mostrarBarChart(double ingresos, double egresos) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) ingresos));
        entries.add(new BarEntry(1, (float) egresos));
        entries.add(new BarEntry(2, (float) (ingresos + egresos)));

        BarDataSet dataSet = new BarDataSet(entries, "Resumen mensual");
        dataSet.setColors(Color.GREEN, Color.RED, Color.BLUE);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.invalidate();
    }
}
