package com.upvmaster.carlos.audiolibros.entities;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.upvmaster.carlos.audiolibros.adapters.AdaptadorLibros;
import com.upvmaster.carlos.audiolibros.adapters.AdaptadorLibrosFiltro;

import java.util.List;

/**
 * Created by Carlos on 21/12/2016.
 */

public class Aplicacion extends Application {
    private List<Libro> listaLibros;
    private AdaptadorLibrosFiltro adaptador;
    private static RequestQueue colaPeticiones;
    private static ImageLoader lectorImagenes;

    @Override
    public void onCreate() {
        listaLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibrosFiltro(this, listaLibros);
        colaPeticiones = Volley.newRequestQueue(this);
        lectorImagenes = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });
    }

    public AdaptadorLibrosFiltro getAdaptador() {
        return adaptador;
    }

    public List<Libro> getListLibros() {
        return listaLibros;
    }

    public static RequestQueue getColaPeticiones() {
        return colaPeticiones;
    }

    public static ImageLoader getLectorImagenes() {
        return lectorImagenes;
    }
}
