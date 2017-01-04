package com.upvmaster.carlos.audiolibros.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.upvmaster.carlos.audiolibros.entities.Aplicacion;
import com.upvmaster.carlos.audiolibros.entities.Libro;
import com.upvmaster.carlos.audiolibros.R;

import java.util.List;

/**
 * Created by Carlos on 21/12/2016.
 */
public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {
    private LayoutInflater inflador; //Crea Layouts a partir del XML protected
    List<Libro> listaLibros; //Vector con libros a visualizar
    private Context contexto;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;

    public AdaptadorLibros(Context contexto, List<Libro> listaLibros) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaLibros = listaLibros;
        this.contexto = contexto;
    } //Creamos nuestro ViewHolder, con los tipos de elementos a modificar

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView portada;
        public TextView titulo;

        public ViewHolder(View itemView) {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
        }
    } // Creamos el ViewHolder con las vista de un elemento sin personalizar

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_selector, null);
        v.setOnClickListener(onClickListener);
        v.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(final ViewHolder holder, int posicion) {
        Libro libro = listaLibros.get(posicion);
        Aplicacion aplicacion = (Aplicacion) contexto.getApplicationContext();
        aplicacion.getLectorImagenes().get(libro.urlImagen, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                holder.portada.setImageBitmap(bitmap);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                holder.portada.setImageResource(R.drawable.books);
            }
        });
        holder.titulo.setText(libro.titulo);
    }

    // Indicamos el número de elementos de la lista
    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

}