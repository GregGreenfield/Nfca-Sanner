package com.example.greenfield.scanapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Greenfield on 14/05/2016.
 */
public class RegisterSelection extends Activity {
    private static ListView ls;
    private static ArrayList<String> values = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
    private TextView tv;
    private RegisterConnection regSel;
    private String className, classID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerselection);

        tv = (TextView) findViewById(R.id.textView2);
        ls = (ListView) findViewById(R.id.listView2);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            className = extras.getString("className");
            classID = extras.getString("classID");
            System.out.println(classID + " " + className);
        }

        tv.setText(classID + " " + className +"'s registers.");
        regSel = new RegisterConnection(classID);
        regSel.execute();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                Intent nextScreen = new Intent(getApplicationContext(), Scanning.class);

                nextScreen.putExtra("registerID",values.get(i).substring(0, values.get(i).indexOf(" ")));
                nextScreen.putExtra("ClassID", classID);

                startActivity(nextScreen);
            }
        });
    }

    public static void addRegister(ResultSet rs){
        values.clear();
        try {
            while(rs.next()){
                values.add(rs.getString("registerID") + " " + rs.getString("registerDate") + " " + rs.getString("registerTime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < values.size(); i++)
            System.out.println(values.get(i));

        ls.setAdapter(adapter);
    }
}
