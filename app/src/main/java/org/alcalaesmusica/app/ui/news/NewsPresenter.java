package org.alcalaesmusica.app.ui.news;

import android.content.Context;
import android.content.Intent;

import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.NewsInteractor;
import org.alcalaesmusica.app.model.News;
import org.alcalaesmusica.app.ui.news_info.NewsInfoPresenter;

import java.util.List;

/**
 * Created by julio on 2/06/17.
 */


 public class NewsPresenter extends BasePresenter {

     private final NewsView view;
    private final NewsInteractor newsInteractor;

    public static Intent newNewsActivity(Context context) {

         Intent intent = new Intent(context, NewsActivity.class);

         return intent;
     }

     public static NewsPresenter newInstance(NewsView view, Context context) {

         return new NewsPresenter(view, context);

     }

     private NewsPresenter(NewsView view, Context context) {
         super(context, view);

         this.view = view;
         newsInteractor = new NewsInteractor(context, view);

     }

     public void onCreate() {

         refreshData();
     }

     public void onResume() {

     }

     public void refreshData() {

//         List<News> newsList = newsInteractor.getNewsFromDB();
         List<News> newsList = newsInteractor.getNewsFromDBAvoidPrevious();
         view.showNewsList(newsList);

     }

    public void onNewsClicked(int idNews) {
        context.startActivity(NewsInfoPresenter.newNewsInfoActivity(context, idNews));
    }

 }
