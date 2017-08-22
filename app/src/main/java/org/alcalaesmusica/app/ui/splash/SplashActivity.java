package org.alcalaesmusica.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.alcalaesmusica.app.DebugHelper;
import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BaseActivity;
import org.alcalaesmusica.app.base.BasePresenter;

/**
 * Created by julio on 29/05/17.
 */

public class SplashActivity extends BaseActivity implements SplashView {
    private SplashPresenter presenter;

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    private RelativeLayout viewLoading;
    private TextView tvLoadingText;

    public void findViews() {
        viewLoading = (RelativeLayout) findViewById(R.id.view_loading);
        tvLoadingText = (TextView) findViewById(R.id.tv_loading_text);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = SplashPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }

        presenter.onCreate(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void showLoadingDataMessage(String message) {
        viewLoading.setVisibility(message != null ? View.VISIBLE : View.GONE);
        if (message != null) {
            tvLoadingText.setText(message);
        }
    }
}
