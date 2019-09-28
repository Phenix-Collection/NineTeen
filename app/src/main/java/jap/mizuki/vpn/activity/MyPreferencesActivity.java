package jap.mizuki.vpn.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import jap.mizuki.vpn.App;
import jap.mizuki.vpn.database.DBHelper;
import jap.mizuki.vpn.model.Server;
import jap.mizuki.vpn.util.CountriesNames;
import jap.mizuki.vpn.util.PropertiesService;

import java.util.List;

/**
 * Created by Kusenko on 13.12.2016.
 */

public class MyPreferencesActivity extends PreferenceActivity {
    private Toolbar toolbar;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jap.mizuki.vpn.R.layout.activity_preference);
        toolbar = (Toolbar) findViewById(jap.mizuki.vpn.R.id.preferenceToolbar);
        toolbar.setTitle(jap.mizuki.vpn.R.string.app_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFragmentManager().beginTransaction().replace(jap.mizuki.vpn.R.id.preferenceContent, new MyPreferenceFragment()).commit();
        App application = (App) getApplication();
        mTracker = application.getDefaultTracker();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(jap.mizuki.vpn.R.xml.preferences);

            DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
            List<Server> countryList = dbHelper.getUniqueCountries();
            CharSequence entriesValues[] = new CharSequence[countryList.size()];
            CharSequence entries[] = new CharSequence[countryList.size()];

            for (int i = 0; i < countryList.size(); i++) {
                entriesValues[i] = countryList.get(i).getCountryLong();
                String localeCountryName = CountriesNames.getCountries().get(countryList.get(i).getCountryShort()) != null ?
                        CountriesNames.getCountries().get(countryList.get(i).getCountryShort()) :
                        countryList.get(i).getCountryLong();
                entries[i] = localeCountryName;
            }

            ListPreference listPreference = (ListPreference) findPreference("selectedCountry");
            if (entries.length == 0) {
                PreferenceCategory countryPriorityCategory = (PreferenceCategory) findPreference("countryPriorityCategory");
                getPreferenceScreen().removePreference(countryPriorityCategory);
            } else {
                listPreference.setEntries(entries);
                listPreference.setEntryValues(entriesValues);
                if (PropertiesService.getSelectedCountry() == null)
                    listPreference.setValueIndex(0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Preference");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
