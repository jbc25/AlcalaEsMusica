package org.alcalaesmusica.app.ui.band_info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;

import com.google.firebase.analytics.FirebaseAnalytics;
import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.BandInteractor;
import org.alcalaesmusica.app.interactor.EventInteractor;
import org.alcalaesmusica.app.model.Band;
import org.alcalaesmusica.app.model.Event;
import org.alcalaesmusica.app.ui.image_full.ImageFullActivity;

import java.util.List;

/**
 * Created by julio on 25/05/17.
 */


public class BandInfoPresenter extends BasePresenter {

    private static final String EXTRA_BAND_ID = "extra_band_id";

    private final BandInfoView view;
    private final BandInteractor bandInteractor;
    private final EventInteractor eventInteractor;
    private int idBand;

    public static Intent newBandInfoActivity(Context context, int idBand) {

        Intent intent = new Intent(context, BandInfoActivity.class);
        intent.putExtra(EXTRA_BAND_ID, idBand);
        return intent;
    }

    public static BandInfoPresenter newInstance(BandInfoView view, Context context) {

        return new BandInfoPresenter(view, context);

    }

    private BandInfoPresenter(BandInfoView view, Context context) {
        super(context, view);

        this.view = view;
        bandInteractor = new BandInteractor(context, view);
        eventInteractor = new EventInteractor(context, view);
    }

    public void onCreate(Intent intent) {

        idBand = intent.getIntExtra(EXTRA_BAND_ID, -1);
        if (idBand == -1) {
            throw new IllegalArgumentException("Band info must pass a idBand argument");
        }

        Band band = bandInteractor.getBandDB(idBand);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, band.getId()+"");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, band.getName());
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);


        refreshData();
    }

    public void onResume() {

    }

    public void refreshData() {

        Band band = bandInteractor.getBandDB(idBand);
        List<Event> eventsBand = eventInteractor.getEventsForBand(idBand);
        view.showBand(band, eventsBand);
    }

    public void onSocialNetworkClicked(int id) {

        String url = null;
        Band band = bandInteractor.getBandDB(idBand);

        switch (id) {
            case R.id.img_facebook:
                url = band.getFacebook_link();
                break;
            case R.id.img_twitter:
                url = band.getTwitter_link();
                break;
            case R.id.img_youtube:
                url = band.getYoutube_link();
                break;
            case R.id.img_bandcamp:
                url = band.getBandcamp_link();
                break;
            case R.id.img_presskit:
                url = band.getPresskit_link();
                break;
        }

        if (url != null && Patterns.WEB_URL.matcher(url).matches()) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public void onEventFavouriteClicked(int idEvent) {
        eventInteractor.toggleFavState(idEvent, false);
        refreshData();
    }

    public void onImageBandClick() {

        Band band = bandInteractor.getBandDB(idBand);
        context.startActivity(ImageFullActivity.newImageFullActivity(context, band.getImageCoverUrlFull().toString()));
    }

    public void onShareBandClick() {
        Band band = bandInteractor.getBandDB(idBand);
        String urlBandWeb = band.getUrlBandWeb();
        String text = String.format(context.getString(R.string.send_band_text), urlBandWeb);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(intent);
    }
}
