package com.example.iniprojectpbp;
import com.example.iniprojectpbp.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GempaAdapter extends RecyclerView.Adapter<GempaAdapter.ViewHolder> {

    private final List<Gempa> listGempa;

    public GempaAdapter(List<Gempa> listGempa) {
        this.listGempa = listGempa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gempa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gempa gempa = listGempa.get(position);
        holder.tvMagnitude.setText(gempa.getMagnitude());
        holder.tvWilayah.setText(gempa.getWilayah());
        String waktu = gempa.getTanggal() + " | " + gempa.getJam();
        holder.tvWaktu.setText(waktu);
    }

    @Override
    public int getItemCount() {
        return listGempa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvMagnitude;
        public final TextView tvWilayah;
        public final TextView tvWaktu;

        public ViewHolder(View view) {
            super(view);
            tvMagnitude = view.findViewById(R.id.tvItemMagnitude);
            tvWilayah = view.findViewById(R.id.tvItemWilayah);
            tvWaktu = view.findViewById(R.id.tvItemWaktu);
        }
    }
}