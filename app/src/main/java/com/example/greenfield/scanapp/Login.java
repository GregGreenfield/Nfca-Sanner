package com.example.greenfield.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends AppCompatActivity {
    private static EditText username, password;
    private Button loginBtn;
    private static int TID;
    private static String name;
    private static ResultSet result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.LoginBtn);

        TeacherConnection con = new TeacherConnection();
        con.execute();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    while (result.next()) {
                        if (username.getText().toString().toLowerCase().equals(result.getString(3).toLowerCase())
                                && password.getText().toString().toLowerCase().equals(result.getString(4).toLowerCase())) {
                            name = result.getString(2);
                            TID = result.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent nextScreen = new Intent(getApplicationContext(), ClassSelection.class);

                nextScreen.putExtra("TID", TID);
                nextScreen.putExtra("Name", name);

                startActivity(nextScreen);
                }
            });
    }

    public static void addTeachers(ResultSet rs){
        result = rs;
    }
}
