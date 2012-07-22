
package org.drykiss.android.app.laurie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import org.drykiss.android.app.laurie.ad.AdvertisementManager;

public class LaurieActivity extends Activity {
    private SharedPreferences.Editor mPrefsEditor;

    private CompoundButton.OnCheckedChangeListener mFeatureCheckCangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.homeFeatureCheckBox:
                    mPrefsEditor.putBoolean(LaurieService.KEY_TURN_ON_HOME_BUTTON, isChecked);
                    mPrefsEditor.commit();
                    break;
                case R.id.recentAppsFeaturecheckBox:
                    mPrefsEditor
                            .putBoolean(LaurieService.KEY_TURN_ON_RECENT_APPS_BUTTON, isChecked);
                    mPrefsEditor.commit();
                    break;
                default:
                    break;
            }
        }
    };

    View mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPrefsEditor = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()).edit();

        CheckBox featureCheckBox = (CheckBox) findViewById(R.id.homeFeatureCheckBox);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        featureCheckBox.setChecked(prefs.getBoolean(LaurieService.KEY_TURN_ON_HOME_BUTTON, true));
        featureCheckBox.setOnCheckedChangeListener(mFeatureCheckCangeListener);

        featureCheckBox = (CheckBox) findViewById(R.id.recentAppsFeaturecheckBox);
        featureCheckBox.setChecked(prefs.getBoolean(LaurieService.KEY_TURN_ON_RECENT_APPS_BUTTON,
                true));
        featureCheckBox.setOnCheckedChangeListener(mFeatureCheckCangeListener);

        startService(new Intent(getApplicationContext(), LaurieService.class));

        mAdView = AdvertisementManager.getAdvertisementView(this);
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.advertiseLayout);
        adLayout.addView(mAdView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdvertisementManager.destroyAd(mAdView);
    }
}
