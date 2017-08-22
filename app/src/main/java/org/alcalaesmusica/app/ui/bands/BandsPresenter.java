package org.alcalaesmusica.app.ui.bands;

import android.content.Context;
import android.content.Intent;

import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.BandInteractor;
import org.alcalaesmusica.app.model.Band;
import org.alcalaesmusica.app.ui.band_info.BandInfoPresenter;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */


 public class BandsPresenter extends BasePresenter {

     private final BandsView view;
    private final BandInteractor bandInteractor;

    public static Intent newBandsActivity(Context context) {

         Intent intent = new Intent(context, BandsActivity.class);

         return intent;
     }

     public static BandsPresenter newInstance(BandsView view, Context context) {

         return new BandsPresenter(view, context);

     }

     private BandsPresenter(BandsView view, Context context) {
         super(context, view);

         this.view = view;
         bandInteractor = new BandInteractor(context, view);

     }

     public void onCreate() {

         refreshData();
     }


    public void onResume() {

     }

     public void refreshData() {

         List<Band> bands = bandInteractor.getBandsDB();
         view.showBands(bands);

     }


    public void onSearchTextChanged(String text) {

        List<Band> bands = bandInteractor.getBandsDB(text);
        view.showBands(bands);
    }

    public void onBandClicked(int idBand) {
        context.startActivity(BandInfoPresenter.newBandInfoActivity(context, idBand));
    }
}
