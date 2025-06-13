package com.example.lab6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.MovViewHolder> {
    ArrayList<Movimiento> lista;
    String tipo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MovimientoAdapter(ArrayList<Movimiento> lista, boolean tipo) {
        this.lista = lista;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public MovViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimiento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovViewHolder holder, int position) {
        Movimiento m = lista.get(position);
        holder.tvTitulo.setText(m.titulo);
        holder.tvMonto.setText("S/ " + m.monto);
        holder.tvFecha.setText(m.fecha);

        holder.itemView.setOnLongClickListener(v -> {
            mostrarDialogoEditarEliminar(holder.itemView.getContext(), m);
            return true;
        });
    }

    void mostrarDialogoEditarEliminar(Context context, Movimiento m) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_editar_movimiento, null);
        EditText etMonto = view.findViewById(R.id.etMonto);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        etMonto.setText(String.valueOf(m.monto));
        etDescripcion.setText(m.descripcion);

        new AlertDialog.Builder(context)
                .setTitle("Editar / Eliminar")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    m.monto = Double.parseDouble(etMonto.getText().toString());
                    m.descripcion = etDescripcion.getText().toString();
                    db.collection(tipo).document(m.id).set(m);
                    notifyDataSetChanged();
                })
                .setNegativeButton("Eliminar", (dialog, which) -> {
                    db.collection(tipo).document(m.id).delete();
                    lista.remove(m);
                    notifyDataSetChanged();
                })
                .setNeutralButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class MovViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvMonto, tvFecha;
        public MovViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}
