package com.example.proyectosalesinstantly;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class CardViewAtributos  {
    //nombre del producto
    public String nombre;
    //descripcion del producto
    public String descripcion;
    //como tenemos nuestras imagenes en la carpeta drawable por eso es entero
    private int ImagenDeTienda;

    private  String urlImg;


    public CardViewAtributos(String nombre,String descripcion,int ImagenDeTienda, String urlImg) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ImagenDeTienda = ImagenDeTienda;
        this.urlImg = urlImg;

    }



    public String getUrlImg() {
        return urlImg;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagenDeTienda() {
        return ImagenDeTienda;
    }

    public void setImagenDeTienda(int imageResource) {
        this.ImagenDeTienda = imageResource;
    }
}
