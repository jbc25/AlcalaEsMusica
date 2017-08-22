package org.alcalaesmusica.app.ui.venues;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Venue;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public interface VenuesView extends BaseView {


    void showVenues(List<Venue> venues);

    void animateIntro();
}
