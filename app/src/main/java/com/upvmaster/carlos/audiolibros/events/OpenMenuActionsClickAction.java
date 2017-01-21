package com.upvmaster.carlos.audiolibros.events;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.upvmaster.carlos.audiolibros.R;
import com.upvmaster.carlos.audiolibros.activities.MainActivity;
import com.upvmaster.carlos.audiolibros.entities.Aplicacion;
import com.upvmaster.carlos.audiolibros.entities.Libro;
import com.upvmaster.carlos.audiolibros.fragments.SelectorFragment;

/**
 * Created by Carlos on 21/01/2017.
 */

public class OpenMenuActionsClickAction implements ClickAction, Animation.AnimationListener{
    private final MainActivity mainActivity;
    private final View view;

    public OpenMenuActionsClickAction(MainActivity mainActivity, View view) {
        this.mainActivity = mainActivity;
        this.view = view;
    }

    @Override
    public void execute(final int position) {
        AlertDialog.Builder menu = new AlertDialog.Builder(mainActivity);
        CharSequence[] opciones = {"Compartir", "Borrar ", "Insertar"};
        menu.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int opcion) {
                switch (opcion) {
                    case 0: //Compartir
                        Animator anim = AnimatorInflater
                                .loadAnimator(mainActivity, R.animator.compartir);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                Libro libro = ((Aplicacion)mainActivity.getApplication()).getListLibros().get(position);
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                                i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                                mainActivity.startActivity(Intent.createChooser(i, "Compartir"));
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        anim.setTarget(view);
                        anim.start();
                        break;
                    case 1: //Borrar
                        Snackbar.make(view, "¿Estás seguro?", Snackbar.LENGTH_LONG).setAction("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Animation anim = AnimationUtils.loadAnimation(mainActivity, R.anim.menguar);
                                anim.setAnimationListener(OpenMenuActionsClickAction.this);
                                view.startAnimation(anim);
                                ((Aplicacion)mainActivity.getApplication()).getAdaptador().borrar(position);
                            }
                        }).show();
                        break;
                    case 2: //Insertar
                        ((Aplicacion)mainActivity.getApplication()).getAdaptador().insertar(
                                ((Aplicacion)mainActivity.getApplication()).getAdaptador().getItem(position));
                        //adaptador.notifyDataSetChanged();
                        ((Aplicacion)mainActivity.getApplication()).getAdaptador().notifyItemInserted(0);
                        Snackbar.make(view, "Libro insertado", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }).show();
                        break;
                }
            }
        });
        menu.create().show();
    }

    //Animation
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        ((Aplicacion)mainActivity.getApplication()).getAdaptador().notifyDataSetChanged();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
