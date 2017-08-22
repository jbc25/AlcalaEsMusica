package org.alcalaesmusica.app.ui.news;

import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.News;

import java.util.List;

/**
 * Created by julio on 2/06/17.
 */

public interface NewsView extends BaseView {

    void showNewsList(List<News> newsList);
}
