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

public class ClassSelection extends Activity {
    private static ListView ls;
    private static ArrayList<String> values = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
    private TextView tv;
    private ClassConnection classCon;
    private int TID;
    private String name;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classselection);
        tv = (TextView) findViewById(R.id.textView);
        ls = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Name");
            TID = extras.getInt("TID");
            System.out.println(name + " " + TID);
        }

        tv.setText(name + "'s Classes!");
        classCon = new ClassConnection(TID);
        classCon.execute();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                Intent nextScreen = new Intent(getApplicationContext(), RegisterSelection.class);

                nextScreen.putExtra("classID", values.get(i).substring(0, values.get(i).indexOf(" ")));
                nextScreen.putExtra("className", values.get(i).substring(values.get(i).indexOf(" ")));

                startActivity(nextScreen);
            }
        });
    }

    public static void addClass(ResultSet rs){
        values.clear();
        try {
            while(rs.next()){

                values.add(rs.getString("classID") + " " + rs.getString("className"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < values.size(); i++)
            System.out.println(values.get(i));

        ls.setAdapter(adapter);
    }
}
