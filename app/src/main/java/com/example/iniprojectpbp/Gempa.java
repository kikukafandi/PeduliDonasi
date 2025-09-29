package com.example.iniprojectpbp;

public class Gempa {
    private String tanggal;
    private String jam;
    private String magnitude;
    private String wilayah;
    private String kedalaman;
    private String lintang;
    private String bujur;

    // Constructor
    public Gempa(String tanggal, String jam, String magnitude, String wilayah, String kedalaman, String lintang, String bujur) {
        this.tanggal = tanggal;
        this.jam = jam;
        this.magnitude = magnitude;
        this.wilayah = wilayah;
        this.kedalaman = kedalaman;
        this.lintang = lintang;
        this.bujur = bujur;
    }

    // Getters
    public String getTanggal() { return tanggal; }
    public String getJam() { return jam; }
    public String getMagnitude() { return magnitude; }
    public String getWilayah() { return wilayah; }
    public String getKedalaman() { return kedalaman; }
    public String getLintang() { return lintang; }
    public String getBujur() { return bujur; }
}