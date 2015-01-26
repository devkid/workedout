package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class SelectExerciseActivity extends ActionBarActivity {

    private NfcAdapter mNfcAdapter;
    private List<String> mExercises;
    private ListAdapter mExercisesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise);

        // list and adapter for recommended exercises
        mExercises = new ArrayList<>();
        mExercisesListAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, mExercises);
        ((ListView) findViewById(R.id.exerciseListView)).setAdapter(mExercisesListAdapter);

        Intent intent = getIntent();

        // intent came from NFC event
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled() &&
            NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            handleNfcIntent(intent);
        }
        else {
            if(intent.hasExtra("bodyPartId")) {
                getExerciesFromBodyPart(intent.getIntExtra("bodyPartId", 0));
            }
        }

        // fill list:
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        List filling
     */

    public void getExercisesFromDevice(String device) {
        mExercises.clear();
        if(device.equals("Bench press")) {
            mExercises.add("Bankdrücken");
            mExercises.add("Schräges Bankdrücken");
        }
        ((BaseAdapter) mExercisesListAdapter).notifyDataSetChanged();
    }

    public void getExerciesFromBodyPart(int bodyPartId) {
        mExercises.clear();
        if(bodyPartId == 1) {
            mExercises.add("Shrugs");
            mExercises.add("Einarmiges Kurzhantel-Rudern");
            mExercises.add("Reverse Flys");
        }
        ((BaseAdapter) mExercisesListAdapter).notifyDataSetChanged();
    }

    /*
        NFC handling
     */

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }

    public void handleNfcIntent(Intent intent) {
        String type = intent.getType();
        if ("x-application/workedout-device".equals(type)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {

                NdefMessage ndefMessage = ndef.getCachedNdefMessage();

                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == NdefRecord.TNF_MIME_MEDIA) {
                        String device = new String(ndefRecord.getPayload());
                        Log.i("NFC payload", device);
                        getExercisesFromDevice(device);
                    }
                }
            }
        }
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1,
                          textEncoding);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("x-application/workedout-device");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
