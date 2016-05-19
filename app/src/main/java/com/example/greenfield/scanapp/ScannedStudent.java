package com.example.greenfield.scanapp;

import android.os.AsyncTask;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Greenfield on 16/05/2016.
 */
public class ScannedStudent extends AsyncTask<Void, Void, Void> {
    private static final String DB_URL="jdbc:mysql://mydb.c2abvobvruo6.ap-southeast-2.rds.amazonaws.com:3306/registerdb";
    private static final String USER="greg";
    private static final String PASS="Pa55word";
    private Connection conn=null, conn2 = null, conn3 = null, conn4 = null;
    private Statement stmt4 = null;
    private PreparedStatement stmt = null,stmt2 = null, stmt3 = null;
    private ResultSet ss, rs;
    private String RFID, regID, classID, studentID;

    public ScannedStudent(String RFID, String regID, String classID){
        this.RFID = RFID;
        this.regID = regID;
        this.classID = classID;
    }

    @Override
    protected Void doInBackground(Void...params){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            conn2 = DriverManager.getConnection(DB_URL,USER,PASS);
            conn3 = DriverManager.getConnection(DB_URL,USER,PASS);
            conn4 = DriverManager.getConnection(DB_URL,USER,PASS);

            String studsql = "SELECT studentID FROM `registerdb`.`Student` WHERE RFID = ?;";
            stmt2 = conn2.prepareStatement(studsql);

            stmt2.setString(1, RFID.trim());

            ss = stmt2.executeQuery();

            if(ss.next()){
                studentID = ss.getString("studentID");

                String sql = "SELECT `Enrol`.`enrolId` FROM `registerdb`.`Enrol` WHERE studentID = ? AND classID = ?;";
                stmt = conn.prepareStatement(sql);

                stmt.setString(1, studentID);
                stmt.setString(2, classID);

                rs = stmt.executeQuery();
            }
            try {
                if (rs.next()){
                    String enrolID = rs.getString("enrolID");

                    String pre = "UPDATE `registerdb`.`RegEnrol` SET `Attened` = ? WHERE RegID = ? AND EnrolID = ?;";
                    stmt3 = conn3.prepareStatement(pre);

                    stmt3.setString(1, "t");
                    stmt3.setString(2, regID);
                    stmt3.setString(3, enrolID);
                    System.out.println(regID +" ... "+ enrolID);
                    stmt3.execute();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch(SQLException e){
        e.printStackTrace();
        }catch(IllegalAccessException e){
        e.printStackTrace();
        }catch(InstantiationException e){
        e.printStackTrace();
        }catch(ClassNotFoundException e){
        e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
    }
}