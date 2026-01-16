package com.example.displaymessage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.russ.simple_user_interface";
    public final static String MY_MESSAGE = "custom_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // log what we are doing
        Log.w("SimpleUserInterface", "MainActivity.onCreate() was called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        if(message.isEmpty()) {
            Toast.makeText(MainActivity.this, "ERROR: You must provide a colour.", Toast.LENGTH_LONG).show();
        } else {
            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra(MY_MESSAGE, "Your colour choice is superb!"); // Added extra intent message

            // log what we are doing
            Log.w("SimpleUserInterface", "Starts DisplayMessageActivity with =>" + message);

            startActivity(intent);
        }
    }

}
