package dev.shaynemcneil.a8_intents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DrawingService drawingService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button insertImgBtn = findViewById(R.id.insertImageBtn);
        Button clearBtn = findViewById(R.id.clearBtn);
        drawingService = findViewById(R.id.drawingService);


        clearBtn.setOnClickListener(v -> clearCanvas());
        insertImgBtn.setOnClickListener(v -> insertImage());
    }

    // This method is called when the user returns from the image gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is for our image request (code 1) and if it was successful (RESULT_OK)
        // and that we actually got data back.
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            Uri imageUri = data.getData();

            // Pass the URI to the DrawingService to set it as the background
            if (drawingService != null) {
                drawingService.setBackgroundImage(imageUri);
            }
        }
    }

    private void insertImage() {
        // Change canvas background to an image using intents
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Log.d("InsertImage", "Image has been inserted into DrawingService");
        startActivityForResult(intent, 1);

    }

    public void clearCanvas() {
        drawingService.clearCanvas();
        Log.d("ClearCanvas", "Canvas has been cleared");
    }
}