package com.upvmaster.carlos.audiolibros.detail.view;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.upvmaster.carlos.audiolibros.R;
import com.upvmaster.carlos.audiolibros.main.data.BooksRepository;
import com.upvmaster.carlos.audiolibros.main.data.datasources.LibroSharedPrefenceStorage;
import com.upvmaster.carlos.audiolibros.main.view.MainActivity;
import com.upvmaster.carlos.audiolibros.main.data.datasources.Libro;
import com.upvmaster.carlos.audiolibros.main.data.datasources.LibrosSingleton;
import com.upvmaster.carlos.audiolibros.main.data.datasources.VolleySingleton;

import java.io.IOException;

import android.os.Handler;

/**
 * Created by carlos.cupeiro on 22/12/2016.
 */

public class DetalleFragment extends Fragment implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl,DetallePresenter.View {
    public static String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    private DetallePresenter presenter;
    private ZoomSeekBar zoombar;
    private Handler mHandler;
    private Runnable run_tiempo = new Runnable() {

        @Override
        public void run() {
            if(mediaPlayer != null){
                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                zoombar.setVal(mCurrentPosition);
            }
            mHandler.postDelayed(this, 1000);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_detalle, contenedor, false);
        Bundle args = getArguments();
        int id=0;
        if (args != null) {
            id = args.getInt(ARG_ID_LIBRO);
        }
        presenter = new DetallePresenter(id,vista.getContext(),this);
        ponInfoLibro(id,vista);
        //Poner aquí los cambios en ZoomSeekBar
        zoombar = (ZoomSeekBar) vista.findViewById(R.id.zoombar);
        zoombar.setVisibility(View.INVISIBLE);
        //Probar con el seekTo para cambiar el zoomSeekBar
        zoombar.setOnZoomSeekBarListener(new OnZoomSeekBarListener() {
            @Override
            public void colocarAudio(int posicion) {
                if(mediaPlayer!=null)
                    mediaPlayer.seekTo(posicion*1000);
            }
        });
        mHandler = new Handler();
        //Make sure you update Seekbar on UI thread
        getActivity().runOnUiThread(run_tiempo);


        return vista;
    }

    private void ponInfoLibro(int id, View vista) {
        Libro libro = LibrosSingleton.getInstance(vista.getContext()).getListaLibros().get(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((NetworkImageView) vista.findViewById(R.id.portada)).
                setImageUrl(libro.urlImagen, VolleySingleton.getInstance(vista.getContext()).getLectorImagenes());
        vista.setOnTouchListener(this);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        Uri audio = Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(getActivity(), audio);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir " + audio, e);
        }
    }

    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer");
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferencias.getBoolean("pref_autoreproducir", true)) {
            mediaPlayer.start();
        }
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(
                R.id.fragment_detalle));
        mediaController.setPadding(0, 0, 0, 110);
        mediaController.setEnabled(true);
        mediaController.show();
        //Poner el Zoombar
        int duracionAudio = mediaPlayer.getDuration() / 1000;
        zoombar.setValMin(0);
        zoombar.setEscalaMin(0);
        zoombar.setEscalaIni(0);
        zoombar.setEscalaRaya(duracionAudio/50);
        zoombar.setEscalaRayaLarga(10);
        zoombar.setValMax(duracionAudio);
        zoombar.setEscalaMax(duracionAudio);
        zoombar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        DetalleFragment detalleFragment = (DetalleFragment) getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null) {
            ((MainActivity) getActivity()).mostrarElementos(false);
        }
        super.onResume();
    }

    @Override
    public boolean onTouch(View vista, MotionEvent evento) {
        mediaController.show();
        return false;
    }

    @Override
    public void onStop() {
        mHandler.removeCallbacks(run_tiempo);
        mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void ponInfoLibro(Libro book) {
        //TODO
    }

    @Override
    public void updateZoomSeekBar() {
    //TODO
    }
}
