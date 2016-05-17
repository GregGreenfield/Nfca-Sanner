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
    private Statement stmt2 = null, stmt4 = null;
    private PreparedStatement stmt = null, stmt3 = null;
    private ResultSet rs;
    private String RFID, regID, classID;
    private int count = 0;

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

            String sql = "SELECT `Enrol`.`enrolId`,`Enrol`.`ellrolled`,`Student`.`studentID`, `Student`.`RFID`"
                + "FROM registerdb.`Enrol`"
                + "INNER JOIN registerdb.`Student`"
                + "WHERE Student.`RFID` = ? AND `Enrol`.`classID` = ?;";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, RFID.trim());
            stmt.setString(2, classID.trim());

            rs = stmt.executeQuery();

            try {
                if (rs.next()){
                    String enrolID = rs.getString("enrolID");

                    stmt2 = conn2.createStatement();
                    String sql1 = "SELECT `RegEnrolID` FROM `registerdb`.`RegEnrol`";
                    ResultSet ss = stmt2.executeQuery(sql1);

                    while(ss.next()){
                        int RegEnrolID = ss.getInt("RegEnrolID");
                        count = count + RegEnrolID;
                    }

                    String pre = "INSERT INTO `registerdb`.`RegEnrol` (`RegEnrolID`, `RegID`, `EnrolID`, `Attened`) " +
                        "VALUES (?,?,?,?);";
                    stmt3 = conn3.prepareStatement(pre);

                    stmt3.setInt(1, count);
                    stmt3.setString(2, regID);
                    stmt3.setString(3, enrolID);
                    stmt3.setString(4, "t");

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