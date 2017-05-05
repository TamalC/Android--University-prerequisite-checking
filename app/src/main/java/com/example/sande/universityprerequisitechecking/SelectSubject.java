package com.example.sande.universityprerequisitechecking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class SelectSubject extends AppCompatActivity {

    // End Declaring layout button, edit texts

    private ArrayList<String> arrayListSubject;
    private ArrayAdapter<String> arrayAdapterSubject;
    private Button submit;

    // Declaring connection variables
    public Connection con;
    String un,pass,db,ip;
    //End Declaring connection variables
    public TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set logo in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_select_subject);

        submit = (Button) findViewById(R.id.SubmitSub);
        submit.setVisibility(View.VISIBLE);
        arrayListSubject = new ArrayList<String>();
        arrayAdapterSubject = new ArrayAdapter<String>(this, R.layout.subject_row, R.id.subjectRow, arrayListSubject);
        ListView listView = (ListView) findViewById(R.id.listViewSubject);
        listView.setAdapter(arrayAdapterSubject);
        registerForContextMenu(listView);

        // Declaring Server ip, username, database name and password
        ip =  "tamalc.database.windows.net:1433";
        db =  "tamal";
        un =  "tamalc";
        pass = "LDCaprio001";
        // Declaring Server ip, username, database name and password

        // Setting up the connection

        CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
        checkLogin.execute("");


        //End Setting up the function when button login is clicked
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() != R.id.listViewSubject){
            return;
        }

        menu.setHeaderTitle("What do you want to do");

        String[] options = {"Select", "Deselect", "Return"};

        for(String option: options){
            menu.add(option);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex = info.position;
        ListView listView = (ListView) findViewById(R.id.listViewSubject);
        if(item.getTitle().equals("Select")){
            //if delete task is selected

            String listItem = arrayListSubject.get(selectedIndex);
            String[] values = listItem.split(" ");
            int Number = Integer.valueOf(values[0]);
            Toast.makeText(SelectSubject.this, "Number:" + Number, Toast.LENGTH_LONG).show();
            CreateTempSubTable();
            InsertintoTempSubTable(Number);


        }
        else if(item.getTitle().equals("Deselect")){
            String listItem = arrayListSubject.get(selectedIndex);
            String[] values = listItem.split(" ");
            int Number = Integer.valueOf(values[0]);
            Toast.makeText(SelectSubject.this, "Deselect Number:" + Number, Toast.LENGTH_LONG).show();
            DeleteFromTempSubTable(Number);


        }

        return true;
    }


    public void onSubmitButtonClicked(View v){

        Intent intent = new Intent(getApplicationContext(), SelectCourses.class);
        startActivity(intent);

    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String subjectInfo = "";


        @Override
        protected void onPostExecute(String r)
        {

            if(isSuccess)
            {

                arrayAdapterSubject.notifyDataSetChanged();

            }
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
                    String query = "select SubjectID, Alias from dbo.Subject;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        subjectInfo= rs.getString("SubjectID") + " " + rs.getString("Alias"); //Name is the string label of a column in database, read through the select query
                        z = "query successful";
                        isSuccess=true;
                        arrayListSubject.add(subjectInfo);

                    }
                    if(rs.isAfterLast())
                    {
                        con.close();
                    }
                    else
                    {
                        z = "Invalid Query!";
                        isSuccess = false;
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();

                Log.d ("sql error", z);
            }

            return z;
        }
    }

    public void CreateTempSubTable()
    {
        con = connectionclass();
        String query = "CREATE TABLE ListOfSubjects ( Number int );";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void InsertintoTempSubTable(int subjectID)
    {
        con = connectionclass();
        String query = "INSERT INTO ListOfSubjects (Number) values (" + subjectID + ");";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void DeleteFromTempSubTable(int subjectID){
        con = connectionclass();
        String query = "DELETE FROM ListOfSubjects WHERE Number = " + subjectID + ";";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
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


