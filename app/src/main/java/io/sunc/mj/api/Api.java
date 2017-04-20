package io.sunc.mj.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:
 * Date: 2017-04-12 14:41
 * Author: suncheng
 */
public class Api {
    public static MusicApi getMusicApi(String base) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(HttpClient.getDefaultHttpClient())
                .build();
        return retrofit.create(MusicApi.class);
    }
}
