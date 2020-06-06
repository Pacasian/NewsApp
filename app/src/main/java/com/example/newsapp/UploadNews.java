package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadNews extends AppCompatActivity {
Button btnPostNews;
EditText edTitle,edLink,edBrief;
    Connection con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_news);
        btnPostNews=findViewById(R.id.btnPost);
        edTitle=findViewById(R.id.idTitleNews);
        edLink=findViewById(R.id.idDriveLink);
        edBrief=findViewById(R.id.idBriefNews);
        con = new ConnectionClass().CONN();
        if (con != null) {
            System.out.println("-------------------------------");
            System.out.println("Connection valid");
            System.out.println("-------------------------------");

        }
        else{
            System.out.println("-------------------------------");
            System.out.println("Connection invalid");
            System.out.println("-------------------------------");
        }
        btnPostNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsUpload newUpload = new NewsUpload();
                newUpload.execute("");
            }
        });
    }
    public class NewsUpload extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(UploadNews.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(UploadNews.this, "Login Successful", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            try
            {

                String title=edTitle.getText().toString();
                String brief=edBrief.getText().toString() ;
                String link= edLink.getText().toString();


                /**
                 *
                 * Check whether the TITLE and NEWS BODY are not EMPTY
                 *
                 *
                 */
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                System.out.println("-------------------------------");
                System.out.println(currentDate);
                System.out.println("-------------------------------");
                /**

                 *    System.out.println(currentDateandTime);
                 System.out.println(stMatter);
                 System.out.println(stPriority);
                 System.out.println(stType);
                 System.out.println(stSendTo);
                 *
                 */
                //String query = "select * from newsTable";
                String query = "insert into newsTable (imgLink,title,inBrief,newsTime) values ('" + link + "','" + title + "','" + brief + "','" + currentDate + "');";

                Statement stmt = con.createStatement();


                stmt.executeQuery(query);
            }
            catch (SQLException se)
            {
                Log.e("ERROR", se.getMessage());
            }
            return "suuss";
        }
    }
}
