package org.alcalaesmusica.app.ui.filter;

import android.content.Context;

import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.BandInteractor;
import org.alcalaesmusica.app.model.Filter;
import org.alcalaesmusica.app.model.Tag;
import org.alcalaesmusica.app.ui.program.ProgramActivity;
import org.alcalaesmusica.app.ui.program.ProgramPresenter;

import java.util.List;

/**
 * Created by julio on 27/05/17.
 */


public class FilterBandsPresenter extends BasePresenter {

    private final FilterBandsView view;
    private final BandInteractor bandInteractor;
    private Filter filter = new Filter();

    public static FilterBandsPresenter newInstance(FilterBandsView view, Context context) {

        return new FilterBandsPresenter(view, context);

    }

    private FilterBandsPresenter(FilterBandsView view, Context context) {
        super(context, view);

        this.view = view;
        bandInteractor = new BandInteractor(context, view);

    }

    public void onCreate() {

        // todo get from db
//        if (bandInteractor.getTags().isEmpty()) {
//            bandInteractor.initializeMockTags();
//        }

        refreshData();

    }

    public void onResume() {

    }

    public void refreshData() {

        List<Tag> tags = bandInteractor.getTags();
        view.showTags(tags);

    }

    public void onTagClick(String idTag) {

        if (bandInteractor.areAllTagsActive()) {
            bandInteractor.setAllTagsInactiveUnlessThisOne(idTag);
        } else {
            bandInteractor.toggleTagState(idTag);
        }

        if (bandInteractor.areAllTagsInactive()) {
            bandInteractor.setAllTagsActive();
        }

        refreshData();

        ((ProgramPresenter)((ProgramActivity)context).getPresenter()).refreshData();

    }

    public void onAllTagsButtonClick() {
        bandInteractor.setAllTagsActive();
        refreshData();

        ((ProgramPresenter)((ProgramActivity)context).getPresenter()).refreshData();
    }
}
