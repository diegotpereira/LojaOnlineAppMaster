package br.java.lojaonlineappmaster.offline;

import android.app.Application;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class CapacidadeOffline extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));

        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);

        Picasso.setSingletonInstance(built);
    }
}
