package org.alcalaesmusica.app.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.alcalaesmusica.app.App;
import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.views.DialogShowNotification;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static org.alcalaesmusica.app.ui.splash.SplashPresenter.EXTRA_NOTIFICATION_CUSTOM_BUTTON_LINK;
import static org.alcalaesmusica.app.ui.splash.SplashPresenter.EXTRA_NOTIFICATION_CUSTOM_BUTTON_TEXT;
import static org.alcalaesmusica.app.ui.splash.SplashPresenter.EXTRA_NOTIFICATION_MESSAGE;
import static org.alcalaesmusica.app.ui.splash.SplashPresenter.EXTRA_NOTIFICATION_TITLE;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    private ProgressDialog progressDialog;
    private String[] dataEntitiesSubscribed;
    private ProgressBar progressBar;
    private TextView tvTitleToolbar;
    public Toolbar toolbar;

    // I need another result code custom
    public static final int REQ_CODE_EDIT = 1;
    public static final int RESULT_DELETED = 1234;
    private AppBarLayout appBarLayout;
    private ImageView imgTitleToolbar;
    private FirebaseAnalytics mFirebaseAnalytics;

    private BroadcastReceiver receiverShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.ACTION_SHOW_NOTIFICATION)) {
                showNotification(intent.getExtras());
//                view.toast("action refresh");
            }
        }
    };

    private void showNotification(Bundle extras) {

        String title = extras.getString(EXTRA_NOTIFICATION_TITLE);
        String message = extras.getString(EXTRA_NOTIFICATION_MESSAGE);
        String btnText = extras.getString(EXTRA_NOTIFICATION_CUSTOM_BUTTON_TEXT);
        String btnLink = extras.getString(EXTRA_NOTIFICATION_CUSTOM_BUTTON_LINK);

        DialogShowNotification.newInstace(this).show(title, message, btnText, btnLink);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getPresenter() != null) {
            getPresenter().setBaseView(this);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(App.ACTION_SHOW_NOTIFICATION);
//        registerReceiver(receiverShowNotification, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiverShowNotification);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(newBase);
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public abstract BasePresenter getPresenter();

    public App getApp() {
        return (App) getApplicationContext();
    }

    // ------------ UI NOTIFICATIONS -----------


    @Override
    public void onInvalidToken() {

        // Maybe an alertdialog?
//        toast(R.string.should_login_again);
        finish();
//        startActivity(LoginPresenter.newLoginActivity(this));
    }

    @Override
    public void toast(int stringResId) {
        toast(getString(stringResId));
    }

    @Override
    public void toast(final String mensaje) {

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

    }

    @Override
    public void alert(final String title, final String message) {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        if (title != null) {
            ab.setTitle(title);
        }

        ab.setMessage(message);
        ab.setNegativeButton(R.string.back, null);
        ab.show();


    }

    @Override
    public void alert(final String message) {

        alert(getString(R.string.attention), message);
    }


    @Override
    public void showProgressDialog(String message) {

        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        } catch (Exception e) {
            // Nothing to do. Most probably activity was destroyed
        }
    }


    @Override
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setRefresing(boolean refresing) {

        if (progressBar != null) {
            progressBar.setVisibility(refresing ? View.VISIBLE : View.INVISIBLE);
//            progressBar.setIndeterminate(refresing);
        }
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    // ----CONFIGURATIONS --

    public void configureToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        imgTitleToolbar = (ImageView) findViewById(R.id.img_title_toolbar);
        tvTitleToolbar = (TextView) findViewById(R.id.tv_title_toolbar);
//        tvTitleToolbar.setVisibility(View.GONE);

        tvTitleToolbar.setText(getSupportActionBar().getTitle());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);

    }

    protected void configureSecondLevelActivity() {

        configureToolbar();

        if (toolbar == null) {
            throw new IllegalStateException("No Toolbar in this second level activity");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white);

//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void setImageTitle(int imageId) {
        imgTitleToolbar.setImageResource(imageId);
    }

    public void setToolbarTitle(String title) {
        tvTitleToolbar.setText(title);
    }

    public void setToolbarTitle(int stringId) {
        imgTitleToolbar.setVisibility(View.GONE);
        tvTitleToolbar.setVisibility(View.VISIBLE);
        tvTitleToolbar.setText(stringId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tintIcon(Menu menu, int iconId) {

        Drawable drawable = menu.findItem(iconId).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.red_highlight));
        menu.findItem(iconId).setIcon(drawable);
    }

    public FirebaseAnalytics getAnalytics() {
        return mFirebaseAnalytics;
    }
}
