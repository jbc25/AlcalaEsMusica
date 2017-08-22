package org.alcalaesmusica.app.ui.program;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BaseActivity;
import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.model.Event;
import org.alcalaesmusica.app.views.animation_adapter.AlphaInAnimationAdapter;
import org.alcalaesmusica.app.views.animation_adapter.AnimationAdapter;

import java.util.List;

public class ProgramActivity extends BaseActivity implements ProgramView, TabLayout.OnTabSelectedListener, ProgramAdapter.OnItemClickListener, View.OnClickListener {

    private RecyclerView recyclerEvents;
    private ProgramAdapter adapter;
    private TabLayout tabsDays;
    private ProgramPresenter presenter;
    private DrawerLayout drawerLayout;
    private MenuItem itemFav;
    private TextView tvEmptyMessage;
    private TextView btnShareFavs;
    private AlphaInAnimationAdapter animationAdapter;
    private View viewNewVersion;
    private View btnHideNewVersionView;
    private View btnUpdateNewVersion;

    private void findViews() {
        tabsDays = (TabLayout) findViewById(R.id.tabs_days);
        recyclerEvents = (RecyclerView) findViewById(R.id.recycler_events);
        tvEmptyMessage = (TextView) findViewById(R.id.tv_empty_message);
        btnShareFavs = (TextView) findViewById(R.id.btn_share_favs);

        viewNewVersion = findViewById(R.id.view_new_version);
        btnHideNewVersionView = findViewById(R.id.btn_hide_new_version);
        btnUpdateNewVersion = findViewById(R.id.btn_update_new_version);

        btnShareFavs.setOnClickListener(this);
        btnHideNewVersionView.setOnClickListener(this);
        btnUpdateNewVersion.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = ProgramPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        findViews();

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerEvents.setLayoutManager(layoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerBands.addItemDecoration(dividerItemDecoration);

//        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(20);
//        recyclerBands.addItemDecoration(spaceItemDecoration);

        configureToolbar();
        configureDrawerLayout();
        configureToolbarBackArrowBehaviour();

        setImageTitle(R.mipmap.logo_aem_color_horiz);

        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(1)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(2)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(3)));
        tabsDays.addTab(tabsDays.newTab().setCustomView(getTabView(4)));

        tabsDays.addOnTabSelectedListener(this);

        presenter.onCreate(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    private void configureToolbarBackArrowBehaviour() {

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {

                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        tintIcon(menu, R.id.menuItem_filter);
        tintIcon(menu, R.id.menuItem_fav);
//        Drawable iconFilter = getResources().getDrawable(R.mipmap.ic_filter);
//        iconFilter.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
//        menu.findItem(R.id.menuItem_filter).setIcon(iconFilter);

        itemFav = menu.findItem(R.id.menuItem_fav);
        return super.onCreateOptionsMenu(menu);
    }

    private void refreshShareFavsVisibility() {
        btnShareFavs.setVisibility(View.GONE);
//        if (itemFav != null) {
//            btnShareFavs.setVisibility(itemFav.isChecked() && adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
//        }
    }

    private void configureDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private View getTabView(int day) {
        View tabView = View.inflate(this, R.layout.view_tab, null);
        TextView tvDayWeek = (TextView) tabView.findViewById(R.id.tv_tab_day_week);
        TextView tvDayMonth = (TextView) tabView.findViewById(R.id.tv_tab_day_month);

        switch (day) {
            case 1:
                tvDayWeek.setText(R.string.wednesday_abrev);
                tvDayMonth.setText("30 " + getString(R.string.august_abrev));
                break;

            case 2:
                tvDayWeek.setText(R.string.thursday_abrev);
                tvDayMonth.setText("31 " + getString(R.string.august_abrev));
                break;

            case 3:
                tvDayWeek.setText(R.string.friday_abrev);
                tvDayMonth.setText("1 " + getString(R.string.september_abrev));
                break;

            case 4:
                tvDayWeek.setText(R.string.saturday_abrev);
                tvDayMonth.setText("2 " + getString(R.string.september_abrev));
                break;

        }
        return tabView;
    }

    // INTERACTIONS
    @Override
    public void onBandClicked(int idBand) {
        presenter.onBandClicked(idBand);
    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {
        presenter.onEventFavouriteClicked(idEvent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_share_favs:
                presenter.onShareFavsButtonClicked();
                break;

            case R.id.btn_update_new_version:
                presenter.onUpdateVersionClick();
                break;

            case R.id.btn_hide_new_version:
                viewNewVersion.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        presenter.onTabSelected(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_filter:

                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {

                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }

                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                return true;


            case R.id.menuItem_fav:
                itemFav.setChecked(!itemFav.isChecked());

                Drawable iconFilter = getResources().getDrawable(itemFav.isChecked() ? R.mipmap.ic_star_selected : R.mipmap.ic_star_unselected);
                iconFilter.setColorFilter(ContextCompat.getColor(this, R.color.red_highlight), PorterDuff.Mode.SRC_ATOP);
                itemFav.setIcon(iconFilter);
                presenter.onFilterFavouritesClicked(itemFav.isChecked());


                refreshShareFavsVisibility();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // PRESENTER CALLBACKS
    @Override
    public void showEvents(List<Event> events, String emptyMessage) {

        if (adapter == null) {

            adapter = new ProgramAdapter(this, events);
            adapter.setOnItemClickListener(this);

            animationAdapter = new AlphaInAnimationAdapter(adapter);
            animationAdapter.setFirstOnly(false);
            recyclerEvents.setAdapter(animationAdapter);

        } else {
            // Little trick to avoid animation when searching
            animationAdapter.setDuration(0);
            adapter.updateData(events);
            recyclerEvents.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
            recyclerEvents.post(new Runnable() {
                @Override
                public void run() {
                    animationAdapter.setDuration(AnimationAdapter.DEFAULT_DURATION);
                }
            });
        }

        tvEmptyMessage.setVisibility(emptyMessage != null ? View.VISIBLE : View.GONE);
        tvEmptyMessage.setText(emptyMessage);

        refreshShareFavsVisibility();

    }

    @Override
    public void goToTop() {
        if (adapter.getItemCount() > 0) {
            recyclerEvents.scrollToPosition(0);
        }
    }

    @Override
    public void setTabPosition(int position) {
        tabsDays.getTabAt(position).select();
    }

    @Override
    public void showNewVersionAvailable() {

        viewNewVersion.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToEventsTakingPlaceNow(int positionFirstEvent) {
        recyclerEvents.scrollToPosition(positionFirstEvent);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

}
