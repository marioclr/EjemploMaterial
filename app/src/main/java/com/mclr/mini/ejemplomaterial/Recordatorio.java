package com.mclr.mini.ejemplomaterial;

public class Recordatorio {
    private long id;
    private String mNombre;
    private int mImportante;
    private int color_resource;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        this.mNombre = nombre;
    }

    public int getImportante() {
        return mImportante;
    }

    public void setImportante(int importante) {
        mImportante = importante;
    }

    public int getColorResource() {
        return color_resource;
    }

    public void setColorResource(int color_resource) {
        this.color_resource = color_resource;
    }
}
