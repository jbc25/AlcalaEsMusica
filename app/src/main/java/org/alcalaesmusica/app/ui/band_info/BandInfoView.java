package org.alcalaesmusica.app.ui.band_info;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Band;
import org.alcalaesmusica.app.model.Event;

import java.util.List;

/**
 * Created by julio on 25/05/17.
 */

public interface BandInfoView extends BaseView {

    void showBand(Band band, List<Event> eventsBand);
}
