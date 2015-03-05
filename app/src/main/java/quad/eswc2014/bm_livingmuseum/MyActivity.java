package quad.eswc2014.bm_livingmuseum;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Locale;

import eswcss.quads.museumpandora.main.Artefact;
import eswcss.quads.museumpandora.main.Mappings;
import eswcss.quads.museumpandora.main.Neighborings;
import eswcss.quads.museumpandora.main.User;


public class MyActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    ProgressDialog pDialog;
    ImageView img;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        AssetManager am = getAssets();
        try {
            InputStream inputStream = am.open("roommapping.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while((line = br.readLine())!= null) {
                    String[] args = line.split(",");
                    if(args!=null && args.length==2){
                        Mappings.locations.put(args[1], args[0]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
         }

        try {
            InputStream inputStream = am.open("floorplan.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while((line = br.readLine())!= null) {
                        String[] args = line.split(",");
                        if(args!=null && args.length == 2){
                            String a1 = args[0].replace("'", "");
                            String a2 = args[1].replace("'", "");
                            HashSet<String> val1 = Neighborings.relations.get(a1);
                            if(val1==null){
                                val1 = new HashSet<String>();
                                Neighborings.relations.put(a1, val1);
                            }
                            val1.add(a2);

                            HashSet<String> val2 = Neighborings.relations.get(a2);
                            if(val2==null){
                                val2 = new HashSet<String>();
                                Neighborings.relations.put(a2, val2);
                            }
                            val2.add(a1);

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.

        if(tab.getText().equals("USER")){
            TextView tv = (TextView) findViewById(R.id.userInfo);

            if(tv!=null) {
                if(User.getInstance().getProfile()!= "") {
                    tv.setText(User.getInstance().getProfile());
                } else {
                    tv.setText("User Profile empty!");
                }
            }
        }

        if(tab.getText().equals("CURRENT ARTIFACT")){
            Artefact artefact = User.getInstance().getCurrentArtefact();

            TextView title = (TextView) findViewById(R.id.textView);
            if(title!=null){
                title.setText(artefact.getTitle());
            }
            if(artefact.getTitle().equals("")) {
                title.setText("No title");
            }

            img = (ImageView) findViewById(R.id.img);
            new LoadImage().execute(artefact.getImageURL());

            TextView description = (TextView) findViewById(R.id.description);
            description.setText(artefact.getDescription());
            User.getInstance().visited(artefact);
        }
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(tab.getText().equals("USER")){
            TextView tv = (TextView) findViewById(R.id.userInfo);
            tv.setText(User.getInstance().getProfile());
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    return POIFragment.newInstance(position);
                case 1:
                    return ArtifactFragment.newInstance(position);
                case 2:
                    return UserFragment.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
            }
        }
    }
}
