package dev.shaynemcneil.basiccalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView resultWindow;
    private EditText firstNum;
    private EditText secondNum;
    
    // I removed the ImageView buttons from class-level variables. Since they are only using
    // XML onClick attributes right now and aren't modified in the code, keeping them as global
    // variables is a memory leak risk and clutters the class.

    private double num1;
    private double num2;
    private double result;
    
    // I removed the hardcoded string INVALID_ERROR_MSG. Play Store best practices strongly 
    // dictate putting all user-facing text in res/values/strings.xml for translation support!

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

        // I removed the redundant casting (e.g., (TextView)) here. Modern Android Java 
        // infers the view type automatically, which makes the code much cleaner.
        resultWindow = findViewById(R.id.result_text);
        firstNum = findViewById(R.id.firstnum_edit);
        secondNum = findViewById(R.id.secondnum_edit);
    }

    // I added a 'throws' declaration here so the calling methods know they have to handle 
    // bad inputs (like empty strings) directly. Also changed visibility to private for encapsulation.
    private void getNums() throws NumberFormatException {
        num1 = Double.parseDouble(firstNum.getText().toString());
        num2 = Double.parseDouble(secondNum.getText().toString());
    }

    private void updateResult() {
        // I swapped the hardcoded hex color for a ContextCompat color resource. Hardcoded colors 
        // are flagged by AOSP linters because they break Dark Mode and dynamic theming!
        // (Note: Make sure R.color.purple_500 is defined in colors.xml)
        resultWindow.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
        resultWindow.setText(String.valueOf(result));
    }

    public void add(View view) {
        try {
            getNums();
            result = num1 + num2;
            updateResult();
        } catch(NumberFormatException e) {
            // I updated the catch block. Catching generic 'Exception' is an anti-pattern. 
            // Catching exactly what we expect (NumberFormatException) is much safer.
            Toast.makeText(this, getString(R.string.invalid_error_msg), Toast.LENGTH_LONG).show();
        }
    }

    public void subtract(View view) {
        try {
            getNums();
            result = num1 - num2;
            updateResult();
        } catch(NumberFormatException e) {
            Toast.makeText(this, getString(R.string.invalid_error_msg), Toast.LENGTH_LONG).show();
        }
    }

    public void multiply(View view) {
        try {
            getNums();
            result = num1 * num2;
            updateResult();
        } catch(NumberFormatException e) {
            Toast.makeText(this, getString(R.string.invalid_error_msg), Toast.LENGTH_LONG).show();
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
        } catch (NumberFormatException e) {
            // I got rid of the hacky string comparison for exception types and split this 
            // into proper multi-catch blocks. This is standard Java best practice.
            Toast.makeText(this, getString(R.string.invalid_error_msg), Toast.LENGTH_LONG).show();
        } catch (ArithmeticException e) {
            // Moved "ERROR" and Color.RED to proper resources for i18n and theming compliance.
            resultWindow.setText(getString(R.string.error_text));
            resultWindow.setBackgroundColor(ContextCompat.getColor(this, R.color.error_red));
            Toast.makeText(this, getString(R.string.error_prefix) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
