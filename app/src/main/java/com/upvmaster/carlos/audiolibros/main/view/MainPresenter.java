package com.upvmaster.carlos.audiolibros.main.view;

import com.upvmaster.carlos.audiolibros.main.domain.GetLastBook;
import com.upvmaster.carlos.audiolibros.main.domain.HasLastBook;
import com.upvmaster.carlos.audiolibros.main.domain.SaveLastBook;

/**
 * Created by Carlos on 25/01/2017.
 */

public class MainPresenter {
    private final SaveLastBook saveLastBook;
    private final GetLastBook getLastBook;
    private final HasLastBook hasLastBook;
    private final View view;

    public MainPresenter(SaveLastBook saveLastBook, GetLastBook getLastBook, HasLastBook hasLastBook, View view) {
        this.saveLastBook = saveLastBook;
        this.getLastBook = getLastBook;
        this.hasLastBook = hasLastBook;
        this.view = view;
    }

    public void clickFavoriteButton() {
        if (hasLastBook.execute()) {
            view.mostrarFragmentDetalle(getLastBook.execute());
        } else {
            view.mostrarNoUltimaVisita();
        }
    }

    public void openDetalle(int id) {
        saveLastBook.execute(id);
        view.mostrarFragmentDetalle(id);
    }

    public interface View {
        void mostrarFragmentDetalle(int lastBook);
        void mostrarNoUltimaVisita();
    }
}