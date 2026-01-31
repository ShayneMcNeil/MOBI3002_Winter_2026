package dev.shaynemcneil.basiccalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView resultWindow;
    private EditText firstNum;
    private EditText secondNum;
    private ImageView addBtn;
    private ImageView subtractBtn;
    private ImageView multiplyBtn;
    private ImageView divideBtn;
    private double num1;
    private double num2;
    private double result;
    private final String INVALID_ERROR_MSG = "Error: Invalid values entered.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultWindow = (TextView) findViewById(R.id.result_text);
        firstNum = (EditText) findViewById(R.id.firstnum_edit);
        secondNum = (EditText) findViewById(R.id.secondnum_edit);
        addBtn = (ImageView) findViewById(R.id.addBtn);
        subtractBtn = (ImageView) findViewById(R.id.subtractBtn);
        multiplyBtn = (ImageView) findViewById(R.id.multiplyBtn);
        divideBtn = (ImageView) findViewById(R.id.divideBtn);
    }

    public void getNums() {
        num1 = Double.parseDouble(firstNum.getText().toString());
        num2 = Double.parseDouble(secondNum.getText().toString());
    }

    public void updateResult() {
        // Ensures that the result window has a purple background even after ERROR is run into
        resultWindow.setBackgroundColor(Color.parseColor("#673AB7"));
        resultWindow.setText(String.valueOf(result));
    }

    public void add(View view) {
        try {
            getNums();
            result = num1 + num2;
            updateResult();
        } catch(Exception e) {
            Toast.makeText(MainActivity.this, INVALID_ERROR_MSG, Toast.LENGTH_LONG).show();
        }
    }

    public void subtract(View view) {
        try {
            getNums();
            result = num1 - num2;
            updateResult();
        } catch(Exception e) {
            Toast.makeText(MainActivity.this, INVALID_ERROR_MSG, Toast.LENGTH_LONG).show();
        }
    }

    public void multiply(View view) {
        try {
            getNums();
            result = num1 * num2;
            updateResult();
        } catch(Exception e) {
            Toast.makeText(MainActivity.this, INVALID_ERROR_MSG, Toast.LENGTH_LONG).show();
        }
    }

    public void divide(View view) {
        try {
            getNums();
            if(num2 == 0.0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            result = num1 / num2;
            updateResult();
        } catch(Exception e) {
            String exceptionType = e.getClass().getSimpleName().toString();

            if(exceptionType.equals("ArithmeticException")) {
                resultWindow.setText("ERROR");
                resultWindow.setBackgroundColor(Color.RED);
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, INVALID_ERROR_MSG, Toast.LENGTH_LONG).show();
            }
        }
    }

}