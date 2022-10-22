package com.example.proyecto_v2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private List<ItemList> items;

    public RecyclerAdapter(List<ItemList> items) {
        this.items = items;
    }//final constructor.

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new RecyclerHolder(view);
    }//final onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        ItemList item = items.get(position);
        holder.svgBien.setImageResource(item.getSvgBien());
        holder.svgMal.setImageResource(item.getSvgMal());
        holder.tvNumeroPalabra.setText(item.getNumeroPalabra());
        holder.tvPalabra.setText(item.getPalabra());

        holder.svgBien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pal = (String) holder.tvPalabra.getText();
                Examen.listaRespuestas.put(pal, 0);//Se añade la respuesta del usuario a la lista de respuestas
                //Toast.makeText(view.getContext(), pal, Toast.LENGTH_SHORT).show();
            }
        });//final del evento que controla la pulsación del botón bien.

        holder.svgMal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pal = (String) holder.tvPalabra.getText();
                Examen.listaRespuestas.put(pal, 1);//Se añade la respuesta del usuario a la lista de respuestas
            }//final onClick
        });//final del evento que controla la pulsación del botón bien.

    }//final onBindViewHolder

    @Override
    public int getItemCount() {
        return items.size();
    }//final getItemConunt


    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView svgMal;
        private ImageView svgBien;
        private TextView tvNumeroPalabra;
        private TextView tvPalabra;

        public RecyclerHolder(@NonNull View itemView){
            super(itemView);
            svgBien = itemView.findViewById(R.id.bien_image_view);
            svgMal = itemView.findViewById(R.id.mal_image_view);
            tvNumeroPalabra = itemView.findViewById(R.id.numero_palabra_text_view);
            tvPalabra = itemView.findViewById(R.id.palabra_text_view);
        }//final constructor
    }//final inner class RecyclerHolder.
}//final class RecyclerAdapter