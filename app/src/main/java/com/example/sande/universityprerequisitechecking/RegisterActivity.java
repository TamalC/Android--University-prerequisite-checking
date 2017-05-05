package com.example.sande.universityprerequisitechecking;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    public Connection con;
    String un,pass,db,ip;
    public Button register;
    public String userName;
    public String password;
    public String firstName;
    public String lastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set logo in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_register);
        register = (Button) findViewById(R.id.register);
        // Declaring Server ip, username, database name and password
        ip =  "tamalc.database.windows.net:1433";
        db =  "tamal";
        un =  "tamalc";
        pass = "LDCaprio001";
    }
    public void onRegisterButtonClick(View v){

        EditText a = (EditText) findViewById(R.id.UserName);
        EditText b = (EditText) findViewById(R.id.password);
        EditText c = (EditText) findViewById(R.id.firstName);
        EditText d = (EditText) findViewById(R.id.lastName);

        userName = a.getText().toString();
        password = b.getText().toString();
        firstName = c.getText().toString();
        lastName = d.getText().toString();

        con = connectionclass();
        String query = "insert into student values('"+ userName + "', '" + password + "', '" + firstName + "', '" + lastName + "');";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            Intent intent = new Intent(getApplicationContext(), SelectCourses.class);
            startActivity(intent);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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
