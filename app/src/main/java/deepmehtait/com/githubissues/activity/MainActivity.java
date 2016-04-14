package deepmehtait.com.githubissues.activity;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import deepmehtait.com.githubissues.R;
import utils.ConnectionStatus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn=(Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Check for Internet Connectivity before making Network Calls
                if(ConnectionStatus.getInstance(MainActivity.this).isOnline()){
                    // If network connectivity Start Intent
                    Intent i=new Intent(MainActivity.this,TopIssuesGit.class);
                    startActivity(i);
                }else{
                    // If no network connectivity notify user
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}






/* final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog_box);
                dialog.setTitle("Issue Number");
                TextView tv=(TextView)dialog.findViewById(R.id.dialogText);
                tv.setText("Deep \nMehta \n\nAddress:");
                dialog.show();*/



/*
final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog_box);
                //dialog.setTitle("Issue Number");
                TextView titletv=(TextView)dialog.findViewById(R.id.TextTitle);
                titletv.setText("Issue Number");
                TextView tv=(TextView)dialog.findViewById(R.id.dialogText);
                tv.setText("\n\nDeep \nMehta \n\nAddress: \n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:\n" +
                        "\n" +
                        "Deep \n" +
                        "Mehta \n" +
                        "\n" +
                        "Address:");

                Button okButton=(Button)dialog.findViewById(R.id.dialogButton);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

 */
