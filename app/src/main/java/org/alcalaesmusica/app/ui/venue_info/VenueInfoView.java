package org.alcalaesmusica.app.ui.venue_info;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Event;
import org.alcalaesmusica.app.model.Venue;

import java.util.List;

/**
 * Created by julio on 28/05/17.
 */

public interface VenueInfoView extends BaseView {

    void showVenueInfo(Venue venue, List<Event> eventsVenue);
}
