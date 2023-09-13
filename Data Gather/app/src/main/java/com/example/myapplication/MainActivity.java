package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity {

    private String label;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean started = false;
    private  int testeID;
    private DataInstance dataInstance;
    private  SensorManager sensorManager;
    private  AccelerometerSensorListener accelerometerSensorListener;
    private GyroscopicSensorListener gyroscopicSensorListener;
    private OrientationSensorListener orientationSensorListener;
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.STD:
                if (checked)
                    label = "STD";
                    break;
            case R.id.WAL:
                if (checked)
                    label = "WAL";
                    break;
            case R.id.JOG:
                if (checked)
                    label = "JOG";
                    break;
            case R.id.JUM:
                if (checked)
                    label = "JUM";
                    break;
            case R.id.STU:
                if (checked)
                    label = "STU";
                    break;
            case R.id.STN:
                if (checked)
                    label = "STN";
                break;
            case R.id.SCH:
                if (checked)
                    label = "SCH";
                break;
            case R.id.FOL:
                if (checked)
                    label = "FOL";
                break;
            case R.id.FKL:
                if (checked)
                    label = "FKL";
                break;
            case R.id.BSC:
                if (checked)
                    label = "BSC";
                break;
            case R.id.SDL:
                if (checked)
                    label = "SDL";
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Query query = db.collection("testes2");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    testeID = 0;
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });

        // Get SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Get The accelerometer sensor
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor gyroscopic = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        if (accelerometer != null && magnetometer != null && gyroscopic != null){
            accelerometerSensorListener = new AccelerometerSensorListener();
            accelerometerSensorListener.setSensorManager(sensorManager);

            orientationSensorListener= new OrientationSensorListener();
            orientationSensorListener.setSensorManager(sensorManager);

            gyroscopicSensorListener = new GyroscopicSensorListener();
            gyroscopicSensorListener.setSensorManager(sensorManager);

        }

        //Set buttons functions

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                    if(started == false){
                        dataInstance = new DataInstance(label, testeID);

                        accelerometerSensorListener.setDataInstance(dataInstance);
                        sensorManager.registerListener(accelerometerSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

                        gyroscopicSensorListener.setDataInstance(dataInstance);
                        sensorManager.registerListener(gyroscopicSensorListener, gyroscopic, SensorManager.SENSOR_DELAY_NORMAL);

                        orientationSensorListener.setDataInstance(dataInstance);
                        sensorManager.registerListener(orientationSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
                        sensorManager.registerListener(orientationSensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

                        started = true;
                    }

                }

        });

        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(started == true){
                    db.collection("testes2").document(Integer.toString(testeID)).set(dataInstance);
                    testeID++;
                    sensorManager.unregisterListener(accelerometerSensorListener);
                    sensorManager.unregisterListener(gyroscopicSensorListener);
                    sensorManager.unregisterListener(orientationSensorListener);

                    accelerometerSensorListener.setId(0);
                    gyroscopicSensorListener.setId(0);
                    orientationSensorListener.setId(0);


                    started = false;
                }
            }
        });




    }

}