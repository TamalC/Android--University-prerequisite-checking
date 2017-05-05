package com.example.sande.universityprerequisitechecking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class view_source extends AppCompatActivity {

    private ArrayList<String> arrayListAvailable;
    private ArrayAdapter<String> arrayAdapterAvailable;
    private ArrayList<String> arrayListUnavailable;
    private ArrayAdapter<String> arrayAdapterUnavailable;

    // Declaring connection variables
    public Connection con;
    String un,pass,db,ip;
    //End Declaring connection variables
    public TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set logo in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_view_source);

        arrayListAvailable = new ArrayList<String>();
        arrayAdapterAvailable = new ArrayAdapter<String>(this, R.layout.available_row, R.id.textAvail_row, arrayListAvailable);
        ListView listView = (ListView) findViewById(R.id.ListViewAvailable);
        listView.setAdapter(arrayAdapterAvailable);

        arrayListUnavailable = new ArrayList<String>();
        arrayAdapterUnavailable = new ArrayAdapter<String>(this, R.layout.unavailable_row, R.id.UnavailableRow, arrayListUnavailable);
        ListView listViewUnavailble = (ListView) findViewById(R.id.ListViewUnavailable);
        listViewUnavailble.setAdapter(arrayAdapterUnavailable);

        // Declaring Server ip, username, database name and password
        ip =  "tamalc.database.windows.net:1433";
        db =  "tamal";
        un =  "tamalc";
        pass = "LDCaprio001";
        // Declaring Server ip, username, database name and password

        // Setting up the connection

        CheckLogin checkLogin = new CheckLogin();// this is not the Asynctask, which is used to process in background to reduce load on app process
        //checkLogin.execute("");


        //End Setting up the function when button login is clicked
    }

    /*public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String courseInfo = "";
        String courseInfoUnavailable = "";



        @Override
        protected void onPostExecute(String r)
        {

            //if(isSuccess)
            //{

                arrayAdapterAvailable.notifyDataSetChanged();
                arrayAdapterUnavailable.notifyDataSetChanged();

            //}
        }
        @Override
        protected String doInBackground(String... params)
        {

            try
            {
                con = connectionclass();        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "(select s.Alias, c.Number, c.Name from dbo.Course c, dbo.Subject s\n" +
                            "where c.SubjectID = s.SubjectID and c.SubjectID = 90 and c.Prerequisite is null and c.Number NOT IN (select Number from ListOfCourses))\n" +
                            "union\n" +
                            "(select b.Alias, b.Number, b.Name\n" +
                            "from\n" +
                            "(select a.Alias, a.Number, c.Name,s.Alias as PreAlias, c.Number as PreNum\n" +
                            "from dbo.Course c, dbo.Subject s,(\n" +
                            "select s.Alias, c.Number,  c.Prerequisite from dbo.Course c, dbo.Subject s where \n" +
                            "c.SubjectID = s.SubjectID and c.SubjectID = 90 and c.Prerequisite is not null) a \n" +
                            "where\n" +
                            "c.CourseID = a.Prerequisite and \n" +
                            "c.SubjectID = s.SubjectID) b\n" +
                            "where b.PreNum IN (select Number from ListOfCourses));";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        courseInfo= rs.getString("Alias") + " " + rs.getString("Number") + " " + rs.getString("Name"); //Name is the string label of a column in database, read through the select query
                        z = "query successful";
                        isSuccess=true;
                        arrayListAvailable.add(courseInfo);

                    }
                    if(!rs.isAfterLast())
                    {
                        z = "Invalid Query!";
                        isSuccess = false;
                    }

                    String query2 = "select b.Alias, b.Number, b.Name, b.PreAlias, b.PreNum, b.PreName\n" +
                            "from\n" +
                            "(select a.Alias, a.Number, a.Name, s.Alias as PreAlias, c.Number as PreNum, c.Name as PreName\n" +
                            "from dbo.Course c, dbo.Subject s,(\n" +
                            "select s.Alias, c.Number, c.Name, c.Prerequisite from dbo.Course c, dbo.Subject s where \n" +
                            "c.SubjectID = s.SubjectID and c.SubjectID = 90 and c.Prerequisite is not null) a \n" +
                            "where\n" +
                            "c.CourseID = a.Prerequisite and \n" +
                            "c.SubjectID = s.SubjectID) b\n" +
                            "where b.PreNum NOT IN (select Number from ListOfCourses);";

                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query2);
                    while(rs2.next())
                    {
                        courseInfoUnavailable= "Course: " + rs2.getString("Alias") + " " + rs2.getString("Number") + " " +rs2.getString("Name") + "\n" + "Prerequisite:" + rs2.getString("PreAlias") + " " + rs2.getString("PreNum")+ " " + rs2.getString("PreName"); //Name is the string label of a column in database, read through the select query
                        z = "query successful";
                        isSuccess=true;
                        arrayListUnavailable.add(courseInfoUnavailable);

                    }
                    if(rs2.isAfterLast())
                    {
                        DropTempTable();
                        con.close();
                    }
                    else
                    {
                        z = "Invalid Query!";
                        isSuccess = false;
                        DropTempTable();
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();
                DropTempTable();
                Log.d ("sql error", z);
            }

            return z;
        }
}*/


    public class CheckLogin
    {
        String z = "";
        Boolean isSuccess = false;
        String courseInfo = "";
        String courseInfoUnavailable = "";



           {

            try
            {
                con = connectionclass();        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "(select s.Alias, c.Number, c.Name from dbo.Course c, dbo.Subject s\n" +
                            "where c.SubjectID = s.SubjectID and c.SubjectID IN (select Number from ListOfSubjects) and c.Prerequisite is null and c.Number NOT IN (select Number from ListOfCourses))\n" +
                            "union\n" +
                            "(select b.Alias, b.Number, b.Name\n" +
                            "from\n" +
                            "(select a.Alias, a.Number, a.Name, s.Alias as PreAlias, c.Number as PreNum\n" +
                            "from dbo.Course c, dbo.Subject s,(\n" +
                            "select s.Alias, c.Number, c.Name, c.Prerequisite from dbo.Course c, dbo.Subject s where \n" +
                            "c.SubjectID = s.SubjectID and c.SubjectID IN (select Number from ListOfSubjects) and c.Prerequisite is not null) a \n" +
                            "where\n" +
                            "c.CourseID = a.Prerequisite and \n" +
                            "c.SubjectID = s.SubjectID) b\n" +
                            "where b.PreNum IN (select Number from ListOfCourses));";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        courseInfo= rs.getString("Alias") + " " + rs.getString("Number") + " " + rs.getString("Name"); //Name is the string label of a column in database, read through the select query
                        z = "query successful";
                        isSuccess=true;
                        arrayListAvailable.add(courseInfo);

                    }
                    if(!rs.isAfterLast())
                    {
                        z = "Invalid Query!";
                        isSuccess = false;
                    }

                    String query2 = "select b.Alias, b.Number, b.Name, b.PreAlias, b.PreNum, b.PreName\n" +
                            "from\n" +
                            "(select a.Alias, a.Number, a.Name, s.Alias as PreAlias, c.Number as PreNum, c.Name as PreName\n" +
                            "from dbo.Course c, dbo.Subject s,(\n" +
                            "select s.Alias, c.Number, c.Name, c.Prerequisite from dbo.Course c, dbo.Subject s where \n" +
                            "c.SubjectID = s.SubjectID and c.SubjectID IN (select Number from ListOfSubjects) and c.Prerequisite is not null) a \n" +
                            "where\n" +
                            "c.CourseID = a.Prerequisite and \n" +
                            "c.SubjectID = s.SubjectID) b\n" +
                            "where b.PreNum NOT IN (select Number from ListOfCourses);";

                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query2);
                    while(rs2.next())
                    {
                        courseInfoUnavailable= "Course: " + rs2.getString("Alias") + " " + rs2.getString("Number") + " " +rs2.getString("Name") + "\n" + "Prerequisite: " + rs2.getString("PreAlias") + " " + rs2.getString("PreNum")+ " " + rs2.getString("PreName"); //Name is the string label of a column in database, read through the select query
                        z = "query successful";
                        isSuccess=true;
                        arrayListUnavailable.add(courseInfoUnavailable);

                    }
                    if(rs2.isAfterLast())
                    {
                        DropTempSubTable();
                        DropTempTable();
                        con.close();
                    }
                    else
                    {
                        z = "Invalid Query!";
                        isSuccess = false;
                        DropTempTable();
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();
                DropTempTable();
                Log.d ("sql error", z);
            }

            if(isSuccess)
            {

                arrayAdapterAvailable.notifyDataSetChanged();
                arrayAdapterUnavailable.notifyDataSetChanged();

            }


        }
    }
    public void DropTempTable()
    {
        con = connectionclass();
        String query = "DROP TABLE ListOfCourses;";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            //Toast.makeText(view_source.this, "Temp Table dropped", Toast.LENGTH_LONG).show();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void DropTempSubTable()
    {
        con = connectionclass();
        String query = "DROP TABLE ListOfSubjects;";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            //Toast.makeText(view_source.this, "Temp Table dropped", Toast.LENGTH_LONG).show();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //your database connection string goes below
            ConnectionURL = "jdbc:jtds:sqlserver://tamalc.database.windows.net:1433;DatabaseName=tamal;user=tamalc@tamalc;password=LDCaprio001;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}