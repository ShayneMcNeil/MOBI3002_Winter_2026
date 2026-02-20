package dev.shaynemcneil.a6dbballgui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

/**
 * The main activity of the application. It serves as the main UI controller (the "View" layer).
 * It displays the BallView, handles user button clicks, and delegates all database operations
 * to the BallDataSource, keeping the UI logic separate from the data logic.
 */
public class MainActivity extends AppCompatActivity {

    private BallView ballView;
    private Button addBallButton;
    private Button clearBallsButton;
    private BallDataSource dataSource;

    private final ActivityResultLauncher<Intent> addBallLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String ballName = data.getStringExtra("ballName");
                        float x = data.getFloatExtra("x", 0);
                        float y = data.getFloatExtra("y", 0);
                        float dx = data.getFloatExtra("dx", 0);
                        float dy = data.getFloatExtra("dy", 0);
                        String colorStr = data.getStringExtra("color");
                        float radius = 50f; // Use a default radius for newly created balls.

                        // Delegate the creation and saving of the ball to the data source.
                        Ball newBall = dataSource.createBall(ballName, x, y, dx, dy, radius, colorStr);

                        // If the ball was created successfully, add it to the BallView to be rendered.
                        if (newBall != null) {
                            ballView.addBall(newBall);
                        } else {
                            Log.e("MainActivity", "Failed to create and save ball");
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballView = findViewById(R.id.ballView);
        addBallButton = findViewById(R.id.addBallButton);
        clearBallsButton = findViewById(R.id.clearBallsButton);

        // Initialize the data source and open the database.
        dataSource = new BallDataSource(this);
        dataSource.open();

        // Load the initial set of balls from the database only once.
        loadBallsFromDb();

        addBallButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBallActivity.class);
            addBallLauncher.launch(intent);
        });

        clearBallsButton.setOnClickListener(v -> {
            dataSource.deleteAllBalls();
            ballView.clearBalls();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Loads all balls from the database via the data source and adds them to the BallView.
     */
    private void loadBallsFromDb() {
        List<Ball> balls = dataSource.getAllBalls();
        for (Ball ball : balls) {
            ballView.addBall(ball);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database only when the Activity is actually destroyed
        dataSource.close();
    }
}
