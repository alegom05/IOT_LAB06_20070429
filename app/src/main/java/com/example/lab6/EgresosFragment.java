package com.example.lab6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EgresosFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Movimiento> lista;
    MovimientoAdapter adapter;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_egresos, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lista = new ArrayList<>();
        adapter = new MovimientoAdapter(lista, "egresos");
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        cargarDatos();

/*
        fab.setOnClickListener(v -> mostrarDialogoNuevoEgreso());
*/

        return view;
    }

    void cargarDatos() {
        db.collection("egresos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            lista.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Movimiento m = doc.toObject(Movimiento.class);
                m.id = doc.getId();
                lista.add(m);
            }
            adapter.notifyDataSetChanged();
        });
    }

/*    void mostrarDialogoNuevoEgreso() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nuevo_movimiento, null);
        EditText etTitulo = dialogView.findViewById(R.id.etTitulo);
        EditText etMonto = dialogView.findViewById(R.id.etMonto);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);

        new AlertDialog.Builder(getContext())
                .setTitle("Nuevo Egreso")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    Movimiento nuevo = new Movimiento(
                            etTitulo.getText().toString(),
                            Double.parseDouble(etMonto.getText().toString()),
                            etDescripcion.getText().toString(),
                            etFecha.getText().toString()
                    );
                    db.collection("egresos").add(nuevo).addOnSuccessListener(doc -> cargarDatos());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }*/
}
