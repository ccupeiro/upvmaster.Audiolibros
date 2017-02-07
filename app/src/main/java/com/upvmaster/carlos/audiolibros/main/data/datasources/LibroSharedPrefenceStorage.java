package com.upvmaster.carlos.audiolibros.main.data.datasources;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Carlos on 21/01/2017.
 */

public class LibroSharedPrefenceStorage implements LibroStorage {

    public static final String PREF_AUDIOLIBROS = "com.example.audiolibros_internal";
    public static final String KEY_ULTIMO_LIBRO = "ultimo";
    private final Context context;

    private static LibroSharedPrefenceStorage instance;

    public static LibroStorage getInstance(Context context) {
        if (instance == null) {
            instance = new LibroSharedPrefenceStorage(context);
        }
        return instance;
    }

    private LibroSharedPrefenceStorage(Context context) {
        this.context = context;
    }

    public boolean hasLastBook() {
        return getPreference().contains(KEY_ULTIMO_LIBRO);
    }

    private SharedPreferences getPreference() {
        return context.getSharedPreferences(PREF_AUDIOLIBROS, Context.MODE_PRIVATE);
    }

    public String getLastBook() {
        return getPreference().getString(KEY_ULTIMO_LIBRO, "");
    }

    public void saveLastBook(String key) {
        getPreference().edit().putString(KEY_ULTIMO_LIBRO,key).commit();
    }

}
