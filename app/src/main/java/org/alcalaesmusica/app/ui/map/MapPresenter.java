package org.alcalaesmusica.app.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;

import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.VenueInteractor;
import org.alcalaesmusica.app.model.Venue;
import org.alcalaesmusica.app.ui.venue_info.VenueInfoPresenter;
import org.alcalaesmusica.app.util.PermissionHelper;

import java.util.List;


/**
 * Created by julio on 28/05/17.
 */


 public class MapPresenter extends BasePresenter implements PermissionHelper.PermissionCallback {

     private final MapView view;
    private final VenueInteractor venuesInteractor;
    private PermissionHelper permissionHelper;
    private Location mLastLocation;
    private Venue lastVenueSelected;

    public static Intent newMapActivity(Context context) {

         Intent intent = new Intent(context, MapActivity.class);

         return intent;
     }

     public static MapPresenter newInstance(MapView view, Context context) {

         return new MapPresenter(view, context);

     }

     private MapPresenter(MapView view, Context context) {
         super(context, view);

         this.view = view;
         venuesInteractor = new VenueInteractor(context, view);

     }

     public void onCreate() {



         permissionHelper = new PermissionHelper((Activity)context, this);
         if (isLocationReady()) {
             view.configureMyLocationOverlay();
         }
         refreshData();
     }


    public void onResume() {

     }


     public void refreshData() {

         List<Venue> venues = venuesInteractor.getVenuesDB();
         view.showVenues(venues);

     }

    private boolean isLocationReady() {
        boolean permissionGranted = permissionHelper.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissionGranted;
    }

    public void onMyLocationButtonClick() {

        if (isLocationReady()) {
            view.goToMyLocation();
        } else {
            checkLocationPermissionGranted();
        }
    }


    // PERMISSIONS
    @Override
    public void onPermissionGranted(String permission) {
        view.goToMyLocation();
    }

    @Override
    public void onPermissionDenied(String permission) {

    }


    public boolean checkLocationPermissionGranted() {

        boolean permissionGranted = permissionHelper.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
        if (!permissionGranted) {
            permissionHelper.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    context.getString(R.string.explanation_permission_location));
        }

        return permissionGranted;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void onVenuesMarkerClick(Venue venue) {
        lastVenueSelected = venue;
        view.showVenueInfo(venue);
    }

    public void onMapClick() {
        lastVenueSelected = null;
        view.hideVenueInfo();
    }

    public void onVenueMoreInfoClick() {

        context.startActivity(VenueInfoPresenter.newVenueInfoActivity(context, lastVenueSelected.getId()));
    }

    public void onVenueIndicationsClick(Location location) {

        String destinationCoords = lastVenueSelected.getLatitude() + "," + lastVenueSelected.getLongitude();

        String uri = null;
        if (location != null) {

            String modeWalk = "!4m2!4m1!3e2";
            String originCoords = location.getLatitude() + "," + location.getLongitude();
            uri = "https://www.google.com/maps/dir/" + originCoords + "/" + destinationCoords + "/data=" + modeWalk;
        } else {
            uri = "geo:0,0?q=" + destinationCoords + " (" + lastVenueSelected.getName() + ")";

        }

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            view.toast(R.string.no_googlemaps_available);
        }

    }
}
