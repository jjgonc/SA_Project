package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationSensorListener implements SensorEventListener {

    private SensorManager sensorManager;
    private DataInstance dataInstance;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private int id = 0;



    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public void setDataInstance(DataInstance dataInstance) {
        this.dataInstance = dataInstance;
    }

    @Override
    public void onSensorChanged(SensorEvent event){

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }

        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        SensorManager.getOrientation(rotationMatrix, orientationAngles);


        OrientationData orientationData = new OrientationData();
        orientationData.setValueX(orientationAngles[0]);
        orientationData.setValueY(orientationAngles[1]);
        orientationData.setValueZ(orientationAngles[2]);
        orientationData.setId(id);
        id++;
        dataInstance.incNumberData();
        if(orientationAngles[0] != 0 ||  orientationAngles[1] != 0 || orientationAngles[2] != 0){
            dataInstance.addOrientation(orientationData);
            System.out.println(String.format("[Orientation] - X=%f, Y=%f, Z=%f", orientationData.getValueX(),orientationData.getValueY(),orientationData.getValueZ() ));
        }


    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
