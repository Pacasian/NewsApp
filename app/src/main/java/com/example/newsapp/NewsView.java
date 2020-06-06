package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NewsView extends AppCompatActivity {
    private ArrayList<NewsViewModel> itemArrayList;
    Connection con;
    //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        itemArrayList = new ArrayList<NewsViewModel>(); // Arraylist Initialization






        ViewNews vNews = new ViewNews();
        vNews.execute("");
    }

    private class ViewNews extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(NewsView.this, "Synchronising",
                    "RecyclerView Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                con = new ConnectionClass().CONN();
                if (con != null) {
                    System.out.println("-------------------------------");
                    System.out.println("Connection valid");
                    System.out.println("-------------------------------");

                } else {
                    System.out.println("-------------------------------");
                    System.out.println("Connection invalid");
                    System.out.println("-------------------------------");
                }

                // Change below query according to your own database.
                String query = "SELECT * FROM newsTable  ORDER BY Nid DESC ;";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                {
                    while (rs.next()) {
                        try {
                            itemArrayList.add(new NewsViewModel(rs.getString("imgLink"), rs.getString("title"), rs.getString("inBrief")));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println("-------------------------------");
                    //System.out.println(itemArrayList.get(0).title);
                    System.out.println("-------------------------------");
                    msg = "Found";
                    success = true;
                } else {
                    msg = "No Data found!";
                    success = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            progress.dismiss();
            Toast.makeText(NewsView.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, NewsView.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<NewsViewModel> values;
        public Context context;


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // public image title and image url
            public TextView showTitle;
            public TextView showBrief;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                layout = v;
                showTitle = (TextView) v.findViewById(R.id.txtTitle);
                showBrief = (TextView) v.findViewById(R.id.txtBrief);
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "position = " + itemArrayList.get(getLayoutPosition()).title, Toast.LENGTH_SHORT).show();

            }
        }

        // Constructor
        public MyAppAdapter(ArrayList<NewsViewModel> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.news_layout_view, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final NewsViewModel newsModel = values.get(position);
            holder.showTitle.setText(newsModel.getTitle());
            // Seperate the time and date and only show the date
            //String[] dateTime = newsModel.getImg().split("\\s+");
            //System.out.println();
            holder.showBrief.setText(newsModel.getBrief());

            //Picasso.with(context).load("http://"+classListItems.getImg()).into(holder.imageView);
        }


        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }

    }
}