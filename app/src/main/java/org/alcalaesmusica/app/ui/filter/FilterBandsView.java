package org.alcalaesmusica.app.ui.filter;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Tag;

import java.util.List;

/**
 * Created by julio on 27/05/17.
 */

public interface FilterBandsView extends BaseView {


    void showTags(List<Tag> tags);
}
