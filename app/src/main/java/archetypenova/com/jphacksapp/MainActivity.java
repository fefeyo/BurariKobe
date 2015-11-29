package archetypenova.com.jphacksapp;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import archetypenova.com.jphacksapp.fragments.PlaceDetailFragment;
import archetypenova.com.jphacksapp.fragments.SelectDistanceFragment;
import archetypenova.com.jphacksapp.fragments.TopSplashFragment;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * メイン画面
 */
public class MainActivity extends AppCompatActivity implements LocationListener{

    private FragmentTransaction mTransaction;
    private LocationManager lm;
    private TopSplashFragment top;
    @InjectView(R.id.front_container)
    FrameLayout front;
    @InjectView(R.id.container)
    FrameLayout container;


    public static double myLat = 0.0;
    public static double myLng = 0.0;
    public static int r = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        top = new TopSplashFragment();
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.front_container, top);
        mTransaction.commit();
        checkMyLocation();
    }

    public void fadeoutTop() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.fadeout, R.anim.fadein);
        mTransaction.addToBackStack(null);
        mTransaction.remove(top);
        mTransaction.commit();
        mTransaction.add(R.id.container, new SelectDistanceFragment());
    }

    public void nextPage(final Fragment fragment) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.slideinright, R.anim.slideoutleft);
        mTransaction.addToBackStack(null);
        mTransaction.replace(R.id.container, fragment);
        mTransaction.commit();
    }

    public void showDialogDetail(final Fragment fragment){
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.dialog_start, R.anim.dialog_dismiss);
        mTransaction.addToBackStack(null);
        mTransaction.add(R.id.front_container, fragment);
        front.setBackgroundColor(Color.parseColor("#a4000000"));
        container.getChildAt(0).setEnabled(false);
        container.getChildAt(0).setClickable(false);
        mTransaction.commit();
    }

    public void dismissDialogDetail(final Fragment fragment){
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.dialog_start, R.anim.dialog_dismiss);
        mTransaction.remove(fragment);
        front.setBackgroundColor(Color.parseColor("#00000000"));
        container.setEnabled(true);
        mTransaction.commit();
    }

    public void checkMyLocation(){
        try {
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    10,
                    this
            );
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            lm.removeUpdates(this);
            myLat = location.getLatitude();
            myLng = location.getLongitude();
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
