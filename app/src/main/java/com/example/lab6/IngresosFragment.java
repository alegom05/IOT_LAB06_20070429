package com.example.lab6;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.Movimiento;
import com.example.lab6.MovimientoAdapter;
import com.example.lab6.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class IngresosFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovimientoAdapter adapter;
    private ArrayList<Movimiento> listaIngresos;
    private FirebaseFirestore db;
    private CollectionReference ingresosRef;

    public IngresosFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ingresos, container, false);

        recyclerView = vista.findViewById(R.id.recycler);
        FloatingActionButton fab = vista.findViewById(R.id.fab_add);

        listaIngresos = new ArrayList<>();
        adapter = new MovimientoAdapter(listaIngresos, true);  // true = esIngreso
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        ingresosRef = db.collection("ingresos");

        cargarIngresos();

        fab.setOnClickListener(view -> mostrarDialogoNuevoIngreso());

        return vista;
    }

    private void cargarIngresos() {
        ingresosRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            listaIngresos.clear();
            for (var doc : queryDocumentSnapshots) {
                Movimiento m = doc.toObject(Movimiento.class);
                m.setId(doc.getId());
                listaIngresos.add(m);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void mostrarDialogoNuevoIngreso() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nuevo_movimiento, null);

        EditText etTitulo = dialogView.findViewById(R.id.etTitulo);
        EditText etMonto = dialogView.findViewById(R.id.etMonto);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        etMonto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(getContext())
                .setTitle("Nuevo Ingreso")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String titulo = etTitulo.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    String fecha = etFecha.getText().toString().trim();
                    String montoStr = etMonto.getText().toString().trim();

                    if (titulo.isEmpty() || montoStr.isEmpty() || fecha.isEmpty()) {
                        Toast.makeText(getContext(), "Todos los campos requeridos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double monto = Double.parseDouble(montoStr);
                    Movimiento nuevo = new Movimiento(titulo, monto, descripcion, fecha);

                    ingresosRef.add(nuevo).addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Ingreso guardado", Toast.LENGTH_SHORT).show();
                        cargarIngresos();
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
