package com.example.greenfield.scanapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Greenfield on 15/05/2016.
 */
public class Scanning extends Activity{
    private NfcAdapter nfcAdapter;
    private Tag tag;
    private static TextView tv, tv2;
    private static String registerID, classID;
    private static int count;
    private ScannedStudent ss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        tv = (TextView) findViewById(R.id.textView3);
        tv2 = (TextView) findViewById(R.id.textView4);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            registerID = extras.getString("registerID");
            classID = extras.getString("ClassID");
            tv2.setText("");
        }

        Thread t1 = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeText();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t1.start();

        if(nfcAdapter != null && nfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC available", Toast.LENGTH_LONG).show();
        }else{
            finish();
        }
    }

    public static void changeText(){
        if (count == 0)
            tv.setText("Scanning .");
        else if (count == 1)
            tv.setText("Scanning ..");
        else if (count == 2)
            tv.setText("Scanning ...");

        count++;

        if (count == 3)
            count = 0;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcA nfca = NfcA.get(tagFromIntent);
        try{
            nfca.connect();
            byte[] ex = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            tv2.setText(byteToHex(ex));
            ss = new ScannedStudent(byteToHex(ex), registerID, classID);
            ss.execute();
            Toast.makeText(this, "NFC intent received", Toast.LENGTH_LONG).show();
            nfca.close();
        }
        catch(Exception e){
            tv2.setText("Error");
        }
    }

    public String byteToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, Scanning.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this,pendingIntent, intentFilter, null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

}
