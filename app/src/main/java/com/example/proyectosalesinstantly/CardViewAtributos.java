package com.example.proyectosalesinstantly;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;

import java.io.Serializable;

public class CardViewAtributos implements Serializable {
    //nombre del producto
    public String nombre;
    //descripcion del producto
    public String descripcion;
    //como tenemos nuestras imagenes en la carpeta drawable por eso es entero
    private String ImagenDeTienda;
    //precio producto
    private Double precioProducto;


    public CardViewAtributos(String nombre, String descripcion, String ImagenDeTienda, Double precioProducto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ImagenDeTienda = ImagenDeTienda;
        this.precioProducto = precioProducto;
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

    public String getImagenDeTienda() {
        return ImagenDeTienda;
    }

    public void setImagenDeTienda(String imageResource) {
        this.ImagenDeTienda = imageResource;
    }

    public Double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
    }
}
