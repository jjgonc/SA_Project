package com.example.myapplication;

import static android.content.ContentValues.TAG;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ml.Tfconvertedmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class MainActivity extends AppCompatActivity {

    private DataInstance dataInstance;
    private SensorManager sensorManager;
    private AccelerometerSensorListener accelerometerSensorListener;
    private GyroscopicSensorListener gyroscopicSensorListener;
    private OrientationSensorListener orientationSensorListener;

    ConstraintLayout bgElement;
    TextView predict;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bgElement = (ConstraintLayout) findViewById(R.id.container);
        bgElement.setBackgroundColor(Color.GREEN);

        predict = (TextView) findViewById(R.id.textView1);
        predict.setText("Predicting...");

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio);

        mediaPlayer.stop();


        // Get SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Get The accelerometer sensor
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor gyroscopic = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        if (accelerometer != null && magnetometer != null && gyroscopic != null) {
            accelerometerSensorListener = new AccelerometerSensorListener();
            accelerometerSensorListener.setSensorManager(sensorManager);

            orientationSensorListener = new OrientationSensorListener();
            orientationSensorListener.setSensorManager(sensorManager);

            gyroscopicSensorListener = new GyroscopicSensorListener();
            gyroscopicSensorListener.setSensorManager(sensorManager);

        }

        dataInstance = new DataInstance(this,predict,bgElement,sensorManager,accelerometerSensorListener,gyroscopicSensorListener,orientationSensorListener);

        accelerometerSensorListener.setDataInstance(dataInstance);
        sensorManager.registerListener(accelerometerSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        gyroscopicSensorListener.setDataInstance(dataInstance);
        sensorManager.registerListener(gyroscopicSensorListener, gyroscopic, SensorManager.SENSOR_DELAY_NORMAL);

        orientationSensorListener.setDataInstance(dataInstance);
        sensorManager.registerListener(orientationSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(orientationSensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }







    }














