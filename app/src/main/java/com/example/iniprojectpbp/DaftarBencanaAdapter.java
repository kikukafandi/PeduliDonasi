package com.example.iniprojectpbp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iniprojectpbp.R;
import java.util.List;

public class DaftarBencanaAdapter extends RecyclerView.Adapter<DaftarBencanaAdapter.ViewHolder> {

    private final List<Gempa> listGempa;

    public DaftarBencanaAdapter(List<Gempa> listGempa) {
        this.listGempa = listGempa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_bencana, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gempa gempa = listGempa.get(position);
        String infoLengkap = "Gempa Magnitudo " + gempa.getMagnitude() +
                "\n" + gempa.getTanggal() + " | " + gempa.getJam() +
                "\nLokasi: " + gempa.getWilayah();
        holder.tvInfoBencana.setText(infoLengkap);
    }

    @Override
    public int getItemCount() {
        return listGempa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvInfoBencana;

        public ViewHolder(View view) {
            super(view);
            tvInfoBencana = view.findViewById(R.id.tvInfoBencana);
        }
    }
}