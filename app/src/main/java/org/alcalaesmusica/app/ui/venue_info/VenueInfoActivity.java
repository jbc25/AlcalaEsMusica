package org.alcalaesmusica.app.ui.venue_info;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BaseActivity;
import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.model.Event;
import org.alcalaesmusica.app.model.Venue;

import java.util.List;

/**
 * Created by julio on 28/05/17.
 */


public class VenueInfoActivity extends BaseActivity implements VenueInfoView, TabLayout.OnTabSelectedListener, EventsVenueAdapter.OnItemClickListener {

    private static final long DURATION_CROSSFADE = 200;

    VenueInfoPresenter presenter;
    private TextView tvVenueName;
    private TabLayout tabsVenueInfo;
    private Venue venue;
    private List<Event> eventsVenue;
    private FrameLayout frameVenueInfo;

    private void findViews() {
        tvVenueName = (TextView) findViewById(R.id.tv_venue_name);
        tabsVenueInfo = (TabLayout) findViewById(R.id.tabs_venue_info);
        frameVenueInfo = (FrameLayout) findViewById(R.id.frame_venue_info);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = VenueInfoPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_info);
        findViews();

        configureSecondLevelActivity();
        setToolbarTitle(R.string.venue);



        tabsVenueInfo.addOnTabSelectedListener(this);

        presenter.onCreate(getIntent());

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    private void showView(int position) {
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_venue_info, VenueInfoMeetFragment.newInstance()).commit();
                break;

            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_venue_info, VenueInfoEventsFragment.newInstance()).commit();
                break;
        }
    }

    //PRESENTER CALLBACKS
    @Override
    public void showVenueInfo(Venue venue, List<Event> events) {
        this.venue = venue;
        this.eventsVenue = events;

        tvVenueName.setText(venue.getName());

        showView(tabsVenueInfo.getSelectedTabPosition());

    }


    // TABS
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        showView(tab.getPosition());
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBandClicked(int idBand) {
        presenter.onBandClick(idBand);
    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {
        presenter.onEventFavouriteClicked(idEvent);
    }

    public Venue getVenue() {
        return venue;
    }

    public List<Event> getEventsVenue() {
        return eventsVenue;
    }
}

