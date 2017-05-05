package com.example.sande.universityprerequisitechecking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // Declaring connection variables
    public Connection con;
    String un,pass,db,ip;
    public Button logIn;
    public String userName;
    public String password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set logo in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_login);

        logIn = (Button) findViewById(R.id.buttonLogIn);
        // Declaring Server ip, username, database name and password
        ip =  "tamalc.database.windows.net:1433";
        db =  "tamal";
        un =  "tamalc";
        pass = "LDCaprio001";
        // Declaring Server ip, username, database name and password

        // Setting up the connection

        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });



    }

    public void onRegisterButtonClick(View v){

            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);

    }
    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r)
        {

            if(isSuccess)
            {
                Intent intent = new Intent(getApplicationContext(), SelectSubject.class);
                startActivity(intent);

            }
            else{

                Toast.makeText(LoginActivity.this, r, Toast.LENGTH_LONG).show();
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
                    EditText a = (EditText) findViewById(R.id.Name);
                    EditText b = (EditText) findViewById(R.id.password);

                    userName = a.getText().toString();
                    password = b.getText().toString();


                    String query = "select * from student\n" +
                            "where username = '"+ userName + "' and password = '" + password + "';";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next())
                    {
                        z = "query successful";
                        isSuccess=true;

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


