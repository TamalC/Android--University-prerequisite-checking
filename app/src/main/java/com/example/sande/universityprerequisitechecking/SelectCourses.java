package com.example.sande.universityprerequisitechecking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.sql.Connection;
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

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SelectCourses extends AppCompatActivity {


    // End Declaring layout button, edit texts

    private ArrayList<String> arrayListResult;
    private ArrayAdapter<String> arrayAdapterResult;
    private Button submit;

    // Declaring connection variables
    public Connection con;
    String un,pass,db,ip;
    //End Declaring connection variables
    public TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_courses);

        submit = (Button) findViewById(R.id.Submit);
        submit.setVisibility(View.VISIBLE);
        arrayListResult = new ArrayList<String>();
        arrayAdapterResult = new ArrayAdapter<String>(this, R.layout.course_row, R.id.textRow, arrayListResult);
        ListView listView = (ListView) findViewById(R.id.listViewCourse);
        listView.setAdapter(arrayAdapterResult);
        registerForContextMenu(listView);

        // Declaring Server ip, username, database name and password
        ip =  "malekse.database.windows.net:1433";
        db =  "Course_prereq";
        un =  "malekse";
        pass = "Msis5133";
        // Declaring Server ip, username, database name and password

        // Setting up the connection

        CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
        checkLogin.execute("");


        //End Setting up the function when button login is clicked
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() != R.id.listViewCourse){
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
        ListView listView = (ListView) findViewById(R.id.listViewCourse);
        if(item.getTitle().equals("Select")){
            //if delete task is selected
            //arrayListToDo.remove(selectedIndex);
            //arrayAdapterToDo.notifyDataSetChanged();
            String listItem = arrayListResult.get(selectedIndex);
            String[] values = listItem.split(" ");
            int Number = Integer.valueOf(values[1]);
            Toast.makeText(SelectCourses.this, "Number:" + Number, Toast.LENGTH_LONG).show();
            CreateTempTable();
            InsertintoTempTable(Number);
            //info.targetView.setBackgroundColor(Color.GRAY);
            //listView.getChildAt(selectedIndex).setBackgroundColor(Color.YELLOW);


        }
        else if(item.getTitle().equals("Deselect")){
            String listItem = arrayListResult.get(selectedIndex);
            String[] values = listItem.split(" ");
            int Number = Integer.valueOf(values[1]);
            Toast.makeText(SelectCourses.this, "Deselect Number:" + Number, Toast.LENGTH_LONG).show();
            DeleteFromTempTable(Number);
            //info.targetView.setBackgroundColor(Color.TRANSPARENT);
            //listView.getChildAt(selectedIndex).setBackgroundColor(Color.TRANSPARENT);


        }

        return true;
    }


    public void onSubmitButtonClicked(View v){

        //CreateTempTable();
        //InsertintoTempTable();
        Intent intent = new Intent(getApplicationContext(), view_source.class);
        startActivity(intent);

    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String courseInfo = "";


       /* protected void onPreExecute()
        {
            listView1.setVisibility(View.VISIBLE);
        }*/

        @Override
        protected void onPostExecute(String r)
        {
            //progressBar.setVisibility(View.GONE);
            //Toast.makeText(SelectCourses.this, r, Toast.LENGTH_LONG).show();
            if(isSuccess)
            {
                /*message = (TextView) findViewById(R.id.textView2);
                message.setText(name1);*/
                //arrayAdapterResult.add;
                arrayAdapterResult.notifyDataSetChanged();

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
                    String query = "select s.Alias, c.Number, c.Name from dbo.Course c, dbo.Subject s\n" +
                            "where c.SubjectID = s.SubjectID and c.SubjectID = 90;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        courseInfo= rs.getString("Alias") + " " + rs.getString("Number") + " " + rs.getString("Name"); //Name is the string label of a column in database, read through the select query
                        z = "query successful";
                        isSuccess=true;
                        //arrayAdapterResult.add(courseInfo);
                        arrayListResult.add(courseInfo);
                        //con.close();

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

    public void CreateTempTable()
    {
        con = connectionclass();
        String query = "CREATE TABLE ListOfCourses ( Number int );";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            //Toast.makeText(SelectCourses.this, "Temp Table Created", Toast.LENGTH_LONG).show();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void InsertintoTempTable(int courseNumber)
    {
        con = connectionclass();
        String query = "INSERT INTO ListOfCourses (Number) values (" + courseNumber + ");";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            //Toast.makeText(SelectCourses.this, "courses inserted in Temp Table", Toast.LENGTH_LONG).show();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void DeleteFromTempTable(int courseNumber){
        con = connectionclass();
        //String query = "INSERT INTO ListOfCourses (Number) values(5303),(5213),(5123);";
        String query = "DELETE FROM ListofCourses WHERE Number = courseNumber;";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            //Toast.makeText(SelectCourses.this, "course deleted from Temp Table", Toast.LENGTH_LONG).show();
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
            ConnectionURL = "jdbc:jtds:sqlserver://malekse.database.windows.net:1433;DatabaseName=Course_prereq;user=malekse@malekse;password=Msis5133;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
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

