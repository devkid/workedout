package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.activeandroid.query.Select;

import de.tu_dresden.inf.es.workedout.workedout.utils.Nfc;

import java.util.List;

import de.tu_dresden.inf.es.workedout.workedout.models.BodyPart;



public class SelectBodyPartActivity extends ActionBarActivity implements ActionBar.TabListener {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_body_part);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // Add tabs to action bar
        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.front_body))
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.back_body))
                .setTabListener(this));
    }

    /*
        NFC handling
     */

    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            Nfc.setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        Nfc.stopForegroundDispatch(this, mNfcAdapter);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String device = new String(Nfc.getNdefRecord(intent).getPayload());
        Intent newIntent = new Intent(this, SelectExerciseActivity.class);
        newIntent.putExtra("device", device);
        startActivity(newIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_body_part, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onUpperBody(View view) {
        Intent intent = new Intent(this, SelectExerciseActivity.class);
        intent.putExtra("bodyPartId", 1);
        startActivity(intent);
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {

            return new BodyFragment(position);

        }
    }

    public static class FrontBodyFragment extends Fragment {



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View.OnTouchListener onTouchListener=new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    android.util.Log.v("test",String.valueOf((int)(event.getX() / v.getWidth()*100))+","+String.valueOf((int)(event.getY() / v.getHeight()*100)));
                    return true;
                }
            };

            View rootView =inflater.inflate(R.layout.fragment_select_front, container, false);
            rootView.findViewById(R.id.imageView).setOnTouchListener(onTouchListener);
            return  rootView;
        }
    }

    public static class BodyFragment extends Fragment {
        long front_or_back;
        public BodyFragment(){};
        public BodyFragment(long front_or_back){
            this.front_or_back=front_or_back;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

            View.OnTouchListener onTouchListener=new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.v("test",String.valueOf((int)(event.getX() / v.getWidth()*100))+","+String.valueOf((int)(event.getY() / v.getHeight()*100)));
                    int x=(int)(event.getX() / v.getWidth()*100);
                    int y=(int)(event.getY() / v.getHeight()*100);
                    List<BodyPart>bl= new Select().from(BodyPart.class).execute();
                    for(BodyPart b: bl) {
                        if(b.checkIfTouched(x,y,front_or_back)) {
                             Log.v("BodyPartName",b.name);
                            Intent intent = new Intent(getActivity(), SelectExerciseActivity.class);
                            intent.putExtra("bodyPartId", b.getId());
                            startActivity(intent);
                            break;
                        }
                    }

                    return true;
                }
            };
            View rootView;
            if (front_or_back==0) {

                    rootView = inflater.inflate(R.layout.fragment_select_front, container, false);
            }
            else
                rootView =inflater.inflate(R.layout.fragment_select_back, container, false);
            rootView.findViewById(R.id.imageView).setOnTouchListener(onTouchListener);
            return  rootView;
        }
    }
}
