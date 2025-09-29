package com.example.iniprojectpbp;

import com.example.iniprojectpbp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    // Deklarasi view yang akan kita gunakan
    private TextView tvCardBencana1;
    private BottomNavigationView bottomNav;
    private RecyclerView rvDaftarBencana;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi view
        tvCardBencana1 = findViewById(R.id.tvCardBencana1);
        rvDaftarBencana = findViewById(R.id.rvDaftarBencana);
        rvDaftarBencana.setLayoutManager(new LinearLayoutManager(this));
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Aksi saat item Beranda diklik
                Toast.makeText(HomeActivity.this, "Anda di Beranda", Toast.LENGTH_SHORT).show();
                return true; // Return true untuk menandai item ini dipilih
            } else if (itemId == R.id.nav_paper) {
                // Aksi saat item Laporan diklik
                Toast.makeText(HomeActivity.this, "Buka halaman Laporan", Toast.LENGTH_SHORT).show();
                // Di sini Anda bisa memulai Activity baru, contoh:
                // startActivity(new Intent(HomeActivity.this, LaporanActivity.class));
                return true;
            } else if (itemId == R.id.nav_card) {
                // Aksi saat item Donasi diklik
                Toast.makeText(HomeActivity.this, "Buka halaman Donasi", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Agar item home terpilih saat pertama kali buka
        bottomNav.setSelectedItemId(R.id.nav_home);
        // Memulai proses pengambilan data
        fetchDataFromBMKG();
    }

    private void fetchDataFromBMKG() {
        executor.execute(() -> {
            try {
                // Ambil data gempa terbaru (JSON)
                final Gempa gempaTerbaru = fetchGempaTerbaru();
                // Ambil daftar gempa terkini (XML)
                final List<Gempa> daftarGempa = fetchDaftarGempa();

                handler.post(() -> {
                    // Langkah 1: Update kartu bencana pertama
                    if (gempaTerbaru != null) {
                        String infoGempa = "Gempa M " + gempaTerbaru.getMagnitude() + " di " + gempaTerbaru.getWilayah();
                        tvCardBencana1.setText(infoGempa);
                    }

                    // Langkah 2: Tampilkan daftar gempa di RecyclerView
                    if (daftarGempa != null && !daftarGempa.isEmpty()) {
                        DaftarBencanaAdapter adapter = new DaftarBencanaAdapter(daftarGempa);
                        rvDaftarBencana.setAdapter(adapter);
                    }
                });

            } catch (Exception e) {
                Log.e("HomeActivity", "Error fetching data", e);
            }
        });
    }

    // --- FUNGSI-FUNGSI PENGAMBIL DATA (TIDAK ADA PERUBAHAN) ---
    // (Semua fungsi di bawah ini sudah benar, tidak perlu diubah)

    private String getResponseFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) { sb.append(line); }
        reader.close();
        conn.disconnect();
        return sb.toString();
    }

    private Gempa fetchGempaTerbaru() throws Exception {
        String jsonUrl = "https://data.bmkg.go.id/DataMKG/TEWS/autogempa.json";
        String response = getResponseFromUrl(jsonUrl);
        JSONObject jsonObj = new JSONObject(response);
        JSONObject gempaObj = jsonObj.getJSONObject("Infogempa").getJSONObject("gempa");
        return new Gempa(
                gempaObj.getString("Tanggal"), gempaObj.getString("Jam"),
                gempaObj.getString("Magnitude"), gempaObj.getString("Wilayah"),
                gempaObj.getString("Kedalaman"), gempaObj.getString("Lintang"),
                gempaObj.getString("Bujur")
        );
    }

    private List<Gempa> fetchDaftarGempa() throws Exception {
        String xmlUrl = "https://data.bmkg.go.id/DataMKG/TEWS/gempaterkini.xml";
        URL url = new URL(xmlUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        return parseGempaXml(inputStream);
    }

    private List<Gempa> parseGempaXml(InputStream inputStream) throws Exception {
        List<Gempa> listGempa = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, null);
        String text = "";
        String tanggal = "", jam = "", magnitude = "", wilayah = "", kedalaman = "", lintang = "", bujur = "";
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("gempa")) {
                        tanggal = jam = magnitude = wilayah = kedalaman = lintang = bujur = "";
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (tagName.equalsIgnoreCase("Tanggal")) tanggal = text;
                    else if (tagName.equalsIgnoreCase("Jam")) jam = text;
                    else if (tagName.equalsIgnoreCase("Magnitude")) magnitude = text;
                    else if (tagName.equalsIgnoreCase("Wilayah")) wilayah = text;
                    else if (tagName.equalsIgnoreCase("Kedalaman")) kedalaman = text;
                    else if (tagName.equalsIgnoreCase("Lintang")) lintang = text;
                    else if (tagName.equalsIgnoreCase("Bujur")) bujur = text;
                    else if (tagName.equalsIgnoreCase("gempa")) {
                        listGempa.add(new Gempa(tanggal, jam, magnitude, wilayah, kedalaman, lintang, bujur));
                    }
                    break;
            }
            eventType = parser.next();
        }
        return listGempa;
    }
}