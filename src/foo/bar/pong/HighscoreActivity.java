package foo.bar.pong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import singleton.UtilitySingleton;

import com.google.gson.Gson;

import constants.Values;
import foo.bar.pong.util.FetchHighscoreDataThread;
import foo.bar.pong.util.ListAdapter;
import android.app.ActionBar;
import android.app.FragmentTransaction;
//import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HighscoreActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_main_layout);

        UtilitySingleton.getInstance().setCurrentActivity(this);
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
//        actionBar.hide();
//        if(actionBar == null){
//        	System.out.println("DEBUUG >>>>>>>>>> ES IST NULL");
//        }
        

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FirstSectionFragment();
                    
                case 1:
                	return new SecondSectionFragment();

                default:
//                    // The other sections of the app are dummy placeholders.
//                    Fragment fragment = new DummySectionFragment();
//                    Bundle args = new Bundle();
//                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
//                    fragment.setArguments(args);
//                    return fragment;

                  // TODO ueberhaupt noetig?
                  Fragment fragment = new FirstSectionFragment();
                  Bundle args = new Bundle();
                  args.putInt(FirstSectionFragment.ARG_SECTION_NUMBER, i + 1);
                  fragment.setArguments(args);
                  return fragment;

            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	if(position == 0) {
        		return "normal game";
        	}
        	else {
        		return "expert game";
        	}
        }
    }

	public static class FirstSectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.highscore_first_layout,
					container, false);
			String data = "";
			Boolean isReady = false;
			String[][] plainData = {};

			FetchHighscoreDataThread dataThread = new FetchHighscoreDataThread(
					"http://141.45.204.198:8080/MuscleRecoveryWebServer/MuscleRecovery_HighscoreSend.jsp",
					data, isReady);
			dataThread.start();
			long startTime = System.nanoTime();
			while (data.equals("")) {
				data = dataThread.getData();
				if ((System.nanoTime() - startTime) >= 1000000000l) {
					break;
				}
			}
			TextView tv = (TextView) rootView.findViewById(R.id.yourPosNormal);
			if (!data.equals("")) {
				Gson gson = new Gson();
				plainData = gson.fromJson(data, String[][].class);

				// create listview and fill with test data
				ListView lv = (ListView) rootView
						.findViewById(R.id.normalHighscoreList);
				ListAdapter listAdapter = new ListAdapter(UtilitySingleton
						.getInstance().getCurrentActivity(), plainData, false);
				lv.setAdapter(listAdapter);
				// get ones own position
				int myposition = 99;
				for (int i = 0; i < plainData.length; i++) {
					if (plainData[i][1].equals(this.getActivity()
							.getSharedPreferences(Values.CONFIG, MODE_PRIVATE)
							.getString(Values.USER_NAME, ""))) {
						myposition = i;
						break;
					}
				}
				if (myposition != 99) {
					myposition++;
					tv.setText(tv.getText() + " " + myposition);
				} else {
					tv.setText(tv.getText() + " /");
				}
			} else {
				tv.setText(tv.getText() + " /");
				Toast.makeText(getActivity(),
						"Wasn't able to fetch NormalMode data!",
						Toast.LENGTH_LONG).show();
			}

			return rootView;
		}
	}
    
  

    
	public static class SecondSectionFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.highscore_second_layout,
					container, false);
			String data = "";
			Boolean isReady = false;
			String[][] plainData = {};

			FetchHighscoreDataThread dataThread = new FetchHighscoreDataThread(
					"http://141.45.204.198:8080/MuscleRecoveryWebServer/MuscleRecovery_HighscoreSend_ExpertMode.jsp",
					data, isReady);
			dataThread.start();
			long startTime = System.nanoTime();
			while (data.equals("")) {
				data = dataThread.getData();
				if ((System.nanoTime() - startTime) >= 1000000000l) {
					break;
				}
			}
			TextView tv = (TextView) rootView.findViewById(R.id.yourPosExpert);
			if (!data.equals("")) {
				Gson gson = new Gson();
				plainData = gson.fromJson(data, String[][].class);

				// create listview and fill with data
				ListView lv = (ListView) rootView
						.findViewById(R.id.expertHighscoreList);
				ListAdapter listAdapter = new ListAdapter(UtilitySingleton
						.getInstance().getCurrentActivity(), plainData, true);
				lv.setAdapter(listAdapter);
				// get ones own position

				int myposition = 99;
				for (int i = 0; i < plainData.length; i++) {
					if (plainData[i][1].equals(this.getActivity()
							.getSharedPreferences(Values.CONFIG, MODE_PRIVATE)
							.getString(Values.USER_NAME, ""))) {
						myposition = i;
						break;
					}
				}
				if (myposition != 99) {
					myposition++;
					tv.setText(tv.getText() + " " + myposition);
				} else {
					tv.setText(tv.getText() + " /");
				}
			} else {
				tv.setText(tv.getText() + " /");
				Toast.makeText(getActivity(), "Wasn't able to fetch ExpertMode data!",
						Toast.LENGTH_LONG).show();
			}
			return rootView;
		}
	}
}