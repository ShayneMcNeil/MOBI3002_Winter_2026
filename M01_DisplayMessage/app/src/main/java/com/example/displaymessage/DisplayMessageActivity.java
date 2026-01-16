package com.example.displaymessage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_message);

        // Receive the Intent, get the message
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE).toLowerCase();
        // CHANGED: Added toLowerCase so that BLACK, Black, black, etc. all work
        String cust_message = intent.getStringExtra(MainActivity.MY_MESSAGE);

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);

        // Put main thread to sleep
        /* try {
            Thread.sleep(5000);
            Toast.makeText(DisplayMessageActivity.this, "Main thread is running slow.", Toast.LENGTH_SHORT).show();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } */

        // Set the text view as the activity layout
        setContentView(textView);

        // Set color of container
        switch (message) {
            case "red":
                textView.setBackgroundColor(Color.RED);
                break;
            case "white":
                textView.setBackgroundColor(Color.WHITE);
                break;
            case "blue":
                textView.setBackgroundColor(Color.BLUE);
                break;
            case "black":
                textView.setBackgroundColor(Color.BLACK);
                break;
            case "green":
                textView.setBackgroundColor(Color.GREEN);
                break;
            default: // Color not found...just use dark gray
                try {
                    int hexColor = Color.parseColor(message); // Assume the user entered HEX colour string instead
                    textView.setBackgroundColor(hexColor);
                    Log.i("HexColorSet", "DisplayMessageActivity background colour set to user-provided HEX value");
                } catch(IllegalArgumentException e) {
                    textView.setBackgroundColor(Color.DKGRAY);
                    textView.setTextColor(Color.WHITE);
                    message = "dark grey"; // So that our log output below works properly
                    Log.i("DefaultColorFallback", "No valid colour provided. Default colour used as fallback.");
                }
        }

        textView.setText("You chose: " + message + ". " + cust_message);

        // Add to the text
        //textView.setText(message + "\n\n  AHA!!!!  I have changed the text colour!!!");


        // Start logging stuff we can find out
        Log.v("SimpleUserInterface", " getGravity =>" + Integer.toHexString(textView.getGravity()));
        Log.v("SimpleUserInterface", " getCurrentTextColor =>" + Integer.toHexString(textView.getCurrentTextColor()));
        Log.v("SimpleUserInterface", " getSolidColor =>" + Integer.toHexString(textView.getSolidColor()));
        Log.v("SimpleUserInterface", " getMaxLines =>" + textView.getMaxLines());
        Log.v("SimpleUserInterface", " getLineHeight =>" + textView.getLineHeight());
        Log.v("SimpleUserInterface", " getText =>" + textView.getText());
        Log.v("SimpleUserInterface", " getText.length =>" + textView.getText().length());
        // Log out what happens to background colour of activity
        Log.v("ActivityChanged", "DisplayMessageActivity background colour changed to " + message);

        try {
            Thread.sleep(20);
        } catch (java.lang.InterruptedException e) {
            Log.v("SimpleUserInterface", "java.lang.InterruptedException: " + e.getMessage());
        }


    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_display_message, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
