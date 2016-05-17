package com.example.greenfield.scanapp;

import android.os.AsyncTask;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClassConnection extends AsyncTask<Void, Void, Void> {
    private static final String DB_URL = "jdbc:mysql://mydb.c2abvobvruo6.ap-southeast-2.rds.amazonaws.com:3306/registerdb";
    private static final String USER = "greg";
    private static final String PASS = "Pa55word";
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs;
    private int TID;

    public ClassConnection(int TID){
        this.TID = TID;
    }

    @Override
    protected java.lang.Void doInBackground(java.lang.Void... params) {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            stmt = conn.createStatement();
            String sql = "SELECT * FROM Class WHERE teacherID = " + TID +" AND available = 't';";
            rs = stmt.executeQuery(sql);

        }catch(SQLException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(java.lang.Void result){
        ClassSelection.addClass(rs);
        super.onPostExecute(result);
    }
}
