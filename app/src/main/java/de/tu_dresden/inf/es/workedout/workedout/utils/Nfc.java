package de.tu_dresden.inf.es.workedout.workedout.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by devkid on 29.01.15.
 */
public class Nfc {

    public static boolean isNfcIntent(Intent intent) {
        return NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction());
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

    public static NdefRecord getNdefRecord(Intent intent) {
        String type = intent.getType();
        if ("x-application/workedout-device".equals(type)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == NdefRecord.TNF_MIME_MEDIA) {
                        return ndefRecord;
                    }
                }
            }
        }
        return null;
    }

    public String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1,
                textEncoding);
    }
}
