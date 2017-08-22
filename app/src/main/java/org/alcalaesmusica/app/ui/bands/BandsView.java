package org.alcalaesmusica.app.ui.bands;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Band;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public interface BandsView extends BaseView {


    void showBands(List<Band> bands);
}
