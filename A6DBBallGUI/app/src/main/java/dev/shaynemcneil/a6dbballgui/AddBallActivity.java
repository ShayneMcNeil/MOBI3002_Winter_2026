package dev.shaynemcneil.a6dbballgui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity that provides a simple form for the user to enter the properties of a new ball.
 * When the user clicks "Enter", the data is validated and packaged into an Intent to be sent back to the MainActivity.
 */
public class AddBallActivity extends AppCompatActivity {

    private EditText editBallName, editX, editY, editDx, editDy, editColor;
    private Button buttonEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ball);

        editBallName = findViewById(R.id.editBallName);
        editX = findViewById(R.id.editX);
        editY = findViewById(R.id.editY);
        editDx = findViewById(R.id.editDx);
        editDy = findViewById(R.id.editDy);
        editColor = findViewById(R.id.editColor);
        buttonEnter = findViewById(R.id.buttonEnter);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the string values from all EditText fields.
                String ballNameStr = editBallName.getText().toString();
                String xStr = editX.getText().toString();
                String yStr = editY.getText().toString();
                String dxStr = editDx.getText().toString();
                String dyStr = editDy.getText().toString();
                String colorStr = editColor.getText().toString();

                // Validate that no fields are empty. If any are, show a toast and stop.
                if (TextUtils.isEmpty(ballNameStr) || TextUtils.isEmpty(xStr) || TextUtils.isEmpty(yStr) ||
                    TextUtils.isEmpty(dxStr) || TextUtils.isEmpty(dyStr) || TextUtils.isEmpty(colorStr)) {
                    Toast.makeText(AddBallActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Try to parse the numeric fields from the input strings.
                    float x = Float.parseFloat(xStr);
                    float y = Float.parseFloat(yStr);
                    float dx = Float.parseFloat(dxStr);
                    float dy = Float.parseFloat(dyStr);

                    // If parsing is successful, create an Intent to hold the result data.
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ballName", ballNameStr);
                    resultIntent.putExtra("x", x);
                    resultIntent.putExtra("y", y);
                    resultIntent.putExtra("dx", dx);
                    resultIntent.putExtra("dy", dy);
                    resultIntent.putExtra("color", colorStr);

                    // Set the result of the activity to RESULT_OK and include the data.
                    setResult(RESULT_OK, resultIntent);
                    // Finish this activity and return to the previous one (MainActivity).
                    finish();

                } catch (NumberFormatException e) {
                    // If parsing fails (e.g., user entered text instead of a number),
                    // show an error message and do not close the activity.
                    Toast.makeText(AddBallActivity.this, "Please enter valid numbers for coordinates and speed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
