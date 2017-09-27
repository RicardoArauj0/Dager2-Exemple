package net.grzechocinski.android.dagger2example.home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.List;
import javax.inject.Inject;
import net.grzechocinski.android.dagger2example.D2EApplication;
import net.grzechocinski.android.dagger2example.R;
import net.grzechocinski.android.dagger2example.home.twitter.Twitter;
import net.grzechocinski.android.dagger2example.utils.D2ECollectionUtils;
import net.grzechocinski.android.dagger2example.utils.NetworkStateManager;

public class HomeActivity extends BaseActivity {

    private static final String PREF_KEY_SAMPLE = "PREF_KEY_SAMPLE";

    @Inject
    NetworkStateManager networkStateManager;

    @Inject
    Twitter twitter;

    @InjectView(R.id.d2e_id_tv_pref)
    TextView preferenceValueTextView;

    @InjectView(R.id.d2e_id_tv_util)
    TextView utilOutputTextView;

    @InjectView(R.id.d2e_id_tv_network_state)
    TextView networkStateTextView;

    @InjectView(R.id.d2e_id_btn_load_preference)
    Button button;

    private D2ECollectionUtils d2EStringUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        //Inject to non-private fields
        D2EApplication.component(this).inject(this);

        //Inject to private fields or local variables
        d2EStringUtils = D2EApplication.component(this).getD2EStringUtils();

        //Lambda usage example
        button.setOnClickListener(v -> {
            sharedPreferences.edit().putString(PREF_KEY_SAMPLE, "foo").apply();
            preferenceValueTextView.setText(sharedPreferences.getString(PREF_KEY_SAMPLE, "bar"));
            twitter.tweet("Hey, I clicked button!");
        });

        //Method reference example
        List<String> sortedAscending = d2EStringUtils.toSortedList(String::compareTo, "zombie", "dog", "animal");
        utilOutputTextView.setText(sortedAscending.get(0));

        //Custom class (NetworkStateManager) with nested dependencies provided by constructor
        networkStateTextView.setText(getString(R.string.network_text, networkStateManager.isConnectedOrConnecting()));

        twitter.tweet("Hello world!");
    }
}
