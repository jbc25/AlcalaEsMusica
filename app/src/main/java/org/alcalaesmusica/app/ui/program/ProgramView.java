package org.alcalaesmusica.app.ui.program;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Event;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public interface ProgramView extends BaseView {


    void showEvents(List<Event> events, String emptyMessage);

    void goToTop();

    void setTabPosition(int position);

    void showNewVersionAvailable();

    void goToEventsTakingPlaceNow(int positionFirstEvent);
}
