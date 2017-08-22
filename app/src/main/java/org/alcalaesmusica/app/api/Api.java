package org.alcalaesmusica.app.api;


import org.alcalaesmusica.app.model.Band;
import org.alcalaesmusica.app.model.Event;
import org.alcalaesmusica.app.model.News;
import org.alcalaesmusica.app.model.Venue;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface Api {

    @GET("bands")
    Observable<List<Band>> getBands();

    @GET("event")
    Observable<List<Event>> getEvents();

    @GET("venues")
    Observable<List<Venue>> getVenues();

    @GET("venues/13")
    Observable<Venue> getVenuePaloma();

    @GET("settings/app_version_market_aem")
    Observable<String> getAppVersionInMarket();

    @GET("news")
    Observable<List<News>> getNews();

    @GET("settings/data_version")
    Observable<String> getDataVersion();

    @GET("bands/{idBand}")
    Observable<Band> getBand(@Path("idBand") Integer idBand);
}
