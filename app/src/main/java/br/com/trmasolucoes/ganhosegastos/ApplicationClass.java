package br.com.trmasolucoes.ganhosegastos;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by tairo on 22/01/16.
 */
public class ApplicationClass extends Application{
    @Override public void onCreate() {
        super.onCreate();

        // setup default typefaces
        TypefaceProvider.registerDefaultIconSets();
    }
}
