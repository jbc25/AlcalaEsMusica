package org.alcalaesmusica.app.ui.venues;

import android.content.Context;
import android.content.Intent;

import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.VenueInteractor;
import org.alcalaesmusica.app.model.Venue;
import org.alcalaesmusica.app.ui.venue_info.VenueInfoPresenter;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */


public class VenuesPresenter extends BasePresenter {

    private static final String PREF_INTRO_VENUES_FIRST_TIME = "pref_intro_venues_first_time";
    private final VenuesView view;
    private final VenueInteractor venueInteractor;

    public static Intent newVenuesActivity(Context context) {

        Intent intent = new Intent(context, VenuesActivity.class);

        return intent;
    }

    public static VenuesPresenter newInstance(VenuesView view, Context context) {

        return new VenuesPresenter(view, context);

    }

    private VenuesPresenter(VenuesView view, Context context) {
        super(context, view);

        this.view = view;
        venueInteractor = new VenueInteractor(context, view);

    }

    public void onCreate() {

        if (getPrefs().getBoolean(PREF_INTRO_VENUES_FIRST_TIME, true)) {
            view.animateIntro();
            getPrefs().edit().putBoolean(PREF_INTRO_VENUES_FIRST_TIME, false).commit();
        }
    }


    public void onResume() {

        refreshData();
    }

    public void refreshData() {

        List<Venue> venues = venueInteractor.getVenuesDB();
        view.showVenues(venues);

    }


    public void onVenueClicked(int idVenue) {
        context.startActivity(VenueInfoPresenter.newVenueInfoActivity(context, idVenue));
    }
}
