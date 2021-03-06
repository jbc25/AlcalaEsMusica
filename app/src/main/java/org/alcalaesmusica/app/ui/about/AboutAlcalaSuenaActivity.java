package org.alcalaesmusica.app.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BaseActivity;
import org.alcalaesmusica.app.base.BasePresenter;

/**
 * Created by julio on 26/05/17.
 */

public class AboutAlcalaSuenaActivity extends BaseActivity {

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_alcalasuena);

        configureSecondLevelActivity();
        setToolbarTitle(R.string.ferias_2017);


        iterateLinkableViews((ViewGroup) getWindow().getDecorView().getRootView());
    }

    private void iterateLinkableViews(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (linkViewIfProceed(child)) {
                    continue;
                }
                iterateLinkableViews((ViewGroup) child);
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else {
                if (child != null) {
                    // DO SOMETHING WITH VIEW
                    linkViewIfProceed(child);
                }
            }
        }

    }

    private boolean linkViewIfProceed(View child) {
        if (child.getTag() != null) {
            final String url = (String) child.getTag();
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            });
            return true;
        }

        return false;
    }
}
