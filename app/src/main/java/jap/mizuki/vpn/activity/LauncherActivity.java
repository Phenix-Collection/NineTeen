package jap.mizuki.vpn.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import jap.mizuki.vpn.R;
import jap.mizuki.vpn.util.NetworkState;

public class LauncherActivity extends Activity {
    private static boolean loadStatus = false;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5075400953666980/1462178765");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (NetworkState.isOnline()) {
            mInterstitialAd.show();
            if (loadStatus) {
                Intent myIntent = new Intent(this, HomeActivity.class);
                startActivity(myIntent);
                finish();
            } else {
                loadStatus = true;
                Intent myIntent = new Intent(this, LoaderActivity.class);
                startActivity(myIntent);
                finish();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.network_error))
                    .setMessage(getString(R.string.network_error_message))
                    .setNegativeButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }



    }
}
