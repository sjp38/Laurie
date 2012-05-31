
package org.drykiss.android.app.laurie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.drykiss.android.app.laurie.ad.AdvertisementManager;

public class LaurieActivity extends Activity {
    View mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
