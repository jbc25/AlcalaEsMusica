package org.alcalaesmusica.app.ui.map;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Venue;

import java.util.List;

/**
 * Created by julio on 28/05/17.
 */

public interface MapView extends BaseView {

    void showVenues(List<Venue> venues);

    void configureMyLocationOverlay();

    void goToMyLocation();

    void showVenueInfo(Venue venue);

    void hideVenueInfo();

}
