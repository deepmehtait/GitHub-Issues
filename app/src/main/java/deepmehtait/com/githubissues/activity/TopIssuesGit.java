package deepmehtait.com.githubissues.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import adapter.CommentsListAdapter;
import adapter.IssueListAdapter;
import deepmehtait.com.githubissues.R;
import modal.Comments;
import modal.Issues;
import modal.User;
import utils.ConnectionStatus;

/**
 * Created by Deep on 13-Apr-16.
 */
public class TopIssuesGit extends AppCompatActivity {
    private static final String TAG = "TopIssuesGit";
    ArrayList<Issues> issues = new ArrayList<Issues>();
    // For Reducing Network calls on click of comments. Will Store comments in HashMap with issue number as key and Comments as Object
    HashMap<String, ArrayList<Comments>> hm = new HashMap<String, ArrayList<Comments>>();
    // Issues & Comments ListView
    ListView lv, commentLv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_git_issues);

        // Get issues of Rails Repo which are q=is%3Aopen , sort%3Aupdated-desc sort by last update, page= "get 40 issues"
        String url = "https://api.github.com/repos/rails/rails/issues?q=is%3Aopen+is%3Aissue+sort%3Aupdated-desc&per_page=40";
        if (ConnectionStatus.getInstance(this).isOnline()) {
            // Get Issues using Async Task
            new getTopIssues().execute(url);
        } else {
            // If no network connectivity notify user
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class getTopIssues extends AsyncTask<String, Void, Integer> {
        // Show progressDialog to user while doing network calls
        private ProgressDialog progressD = new ProgressDialog(TopIssuesGit.this);

        @Override
        protected void onPreExecute() {
            this.progressD.setMessage("Loading ...");
            this.progressD.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // Create Input stream
            InputStream is = null;
            // Using HttpURLConnection
            HttpURLConnection urlConnection = null;
            Integer results = 0;
            try {
                // Form URL Object
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int StatusCode = urlConnection.getResponseCode();
                // 200 - http ok
                if (StatusCode == 200) {
                    is = new BufferedInputStream(urlConnection.getInputStream());
                    String response = InputStreamToString(is);
                    // parse the Response and Map Response to Issues modal Class
                    parseResponse(response);
                    results = 1;
                } else {
                    results = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI
            if (progressD.isShowing()) {
                progressD.dismiss();
            }
            // If result is 1 update UI
            if (result == 1) {
                // Generate List View
                lv = (ListView) findViewById(R.id.ListViewIssue);
                // Set IssueListAdapter
                lv.setAdapter(new IssueListAdapter(getApplicationContext(), issues));
                // Set on Item ClickListener for Comment Dialog box
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get Issue Number based on position
                        String number = issues.get(position).getNumber();
                        // If in HashMap display else make network call to get comments
                        // This will reduce network traffic.
                        if (hm.containsKey(number)) {

                            ArrayList<Comments> lComments = hm.get(number);
                            if (!lComments.isEmpty()) {
                                // if Comments display them
                                Log.d(TAG, "lComments size" + lComments.size());
                                final Dialog dialog = new Dialog(TopIssuesGit.this);
                                dialog.setContentView(R.layout.custom_dialog_box);
                                TextView titletv = (TextView) dialog.findViewById(R.id.TextTitle);
                                titletv.setText(issues.get(Integer.valueOf(position)).getTitle());
                                commentLv = (ListView) dialog.findViewById(R.id.ListViewComment);
                                commentLv.setAdapter(new CommentsListAdapter(dialog.getContext(), lComments));
                                dialog.show();
                            } else {
                                // Notify user in case of no comments
                                Toast.makeText(getApplicationContext(), "No Comments", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // Check for Internet Connectivity before making Network Calls
                            if (ConnectionStatus.getInstance(getApplicationContext()).isOnline()) {
                                // Making Network call for first time and get Comments for that issue
                                new getComments().execute(number, Integer.toString(position));
                            } else {
                                // If no network connectivity notify user
                                Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }

        }


    }

    // get comments on that issue
    public class getComments extends AsyncTask<String, Void, Integer> {
        // Show progressDialog to user while doing network calls
        private ProgressDialog progressD = new ProgressDialog(TopIssuesGit.this);
        String number, position;

        @Override
        protected void onPreExecute() {
            this.progressD.setMessage("Loading Comments...");
            this.progressD.show();
        }


        @Override
        protected Integer doInBackground(String... params) {
            InputStream is = null;
            HttpURLConnection urlConnection = null;
            Integer results = 0;
            try {
                // Form URL Object
                number = params[0];
                position = params[1];
                // Make url for Get call /:issueNumber/comments
                URL url = new URL("https://api.github.com/repos/rails/rails/issues/" + number + "/comments");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int StatusCode = urlConnection.getResponseCode();
                // 200 - http ok
                if (StatusCode == 200) {
                    is = new BufferedInputStream(urlConnection.getInputStream());
                    String response = InputStreamToString(is);
                    // parse the Response and Map Response to Comments modal Class
                    parseComments(response, number);
                    results = 1;
                } else {
                    results = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI
            if (progressD.isShowing()) {
                progressD.dismiss();
            }
            if (result == 1) {

                // Create Arraylist of Comments
                ArrayList<Comments> lComments = hm.get(number);
                if (!lComments.isEmpty()) {

                    // Make Dialog for displaying comments
                    final Dialog dialog = new Dialog(TopIssuesGit.this);
                    dialog.setContentView(R.layout.custom_dialog_box);
                    TextView titletv = (TextView) dialog.findViewById(R.id.TextTitle);
                    titletv.setText(issues.get(Integer.valueOf(position)).getTitle());
                    // Get List view Reference
                    commentLv = (ListView) dialog.findViewById(R.id.ListViewComment);
                    // Set CommentsListViewAdapter
                    commentLv.setAdapter(new CommentsListAdapter(dialog.getContext(), lComments));
                    dialog.show();
                } else {
                    // Notify user in case of no comments
                    Toast.makeText(getApplicationContext(), "No Comments", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }
        }

    }

    // Parse Raw String response to Comments Modal Class
    private void parseComments(String response, String number) {
        try {
            ArrayList<Comments> pComments = new ArrayList<Comments>();
            JSONArray arr = new JSONArray(response);
            Log.d(TAG, " Length= " + arr.length());
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject obj = arr.optJSONObject(i);
                    Comments cm = new Comments();
                    cm.setNumber(number);
                    cm.setBody(obj.getString("body"));
                    JSONObject UserObj = obj.getJSONObject("user");
                    User ur = new User();
                    ur.setLogin(UserObj.getString("login"));
                    cm.setUser(ur);
                    // Add in ArrayList
                    pComments.add(cm);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Add in HashMap for reducing network calls
            hm.put(number, pComments);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Parse Raw String response to Issues Modal Class
    private void parseResponse(String response) {
        try {

            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject obj = arr.optJSONObject(i);
                    Issues issue = new Issues();
                    issue.setTitle(obj.getString("title"));
                    issue.setNumber(obj.getString("number"));
                    issue.setBody(obj.getString("body"));
                    // Add to Issues ArrayList
                    issues.add(issue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Convert Input Stream into String
    private String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        // Close Stream
        if (null != is) {
            is.close();
        }
        return result;

    }
}
