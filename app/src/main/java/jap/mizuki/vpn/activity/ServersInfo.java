package jap.mizuki.vpn.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import jap.mizuki.vpn.R;
import jap.mizuki.vpn.database.DBHelper;

public class ServersInfo extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_info);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5075400953666980/1462178765");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int widthWindow = dm.widthPixels;
        int heightWindow = dm.heightPixels;

        if (getResources().getConfiguration().orientation == 1) {
            getWindow().setLayout((int)(widthWindow * 0.7), (int)(heightWindow * 0.3));
        } else {
            getWindow().setLayout((int)(widthWindow * 0.4), (int)(heightWindow * 0.5));
        }

        DBHelper dbHelper = new DBHelper(this);
        mInterstitialAd.show();

        String basicServers = String.format(getResources().getString(R.string.info_basic), dbHelper.getCountBasic());
        ((TextView) findViewById(R.id.infoBasic)).setText(basicServers);

        String additionalServers = String.format(getResources().getString(R.string.info_additional), dbHelper.getCountAdditional());
        ((TextView) findViewById(R.id.infoAdditional)).setText(additionalServers);
    }
}
