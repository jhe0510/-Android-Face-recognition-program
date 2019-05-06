package com.example.myapplication;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Vision = (Button) findViewById(R.id.FaceVision);
        Vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView IMAGE = (ImageView) findViewById(R.id.imgview);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;

                Bitmap BitmapIMG = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.test1,
                        options
                );

                Paint RectPaint = new Paint();
                RectPaint.setStrokeWidth(4);
                RectPaint.setColor(Color.MAGENTA);
                RectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap = Bitmap.createBitmap(BitmapIMG.getWidth(), BitmapIMG.getHeight(), Bitmap.Config.RGB_565);
                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(BitmapIMG, 0, 0, null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();

                if (!faceDetector.isOperational()) {
                    new AlertDialog.Builder(v.getContext()).setMessage("다시 시도해주세요.").show();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(BitmapIMG).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                for (int i=0; i<faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();
                    tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, RectPaint);
                }

                IMAGE.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

                }

        });
    }
}