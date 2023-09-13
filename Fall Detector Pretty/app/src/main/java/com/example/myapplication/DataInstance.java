package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.ml.Tfconvertedmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class DataInstance {

    public int id;
    public int numberData;
    public List<AccelerometerData> accelerometerData;
    public List<OrientationData> orientationData;
    public List<GyroscopicData> gyroscopicData;

    Context context;
    TextView predict;

    SensorManager sensorManager;
    SensorEventListener accelerometer;
    SensorEventListener gyroscopic;
    SensorEventListener orientation;

    ConstraintLayout bgElement;



    public DataInstance(Context context, TextView predict, ConstraintLayout bgElement, SensorManager sensorManager, SensorEventListener accelerometer, SensorEventListener gyroscopic, SensorEventListener orientation){

        accelerometerData = new ArrayList<>();
        orientationData = new ArrayList<>();
        gyroscopicData = new ArrayList<>();
        numberData = 0;
        this.context = context;
        this.predict = predict;
        this.sensorManager = sensorManager;
        this.accelerometer = accelerometer;
        this.gyroscopic = gyroscopic;
        this.orientation = orientation;
        this.bgElement = bgElement;

    }

    public void reset(){
        accelerometerData = new ArrayList<>();
        orientationData = new ArrayList<>();
        gyroscopicData = new ArrayList<>();
        numberData = 0;
    }

    public void addAccelerometerData(AccelerometerData a){

        this.accelerometerData.add(a);


        if(accelerometerData.size() > 30 && gyroscopicData.size() > 30){
            inference();
        }

    }

    public void addOrientation(OrientationData a){

        this.orientationData.add(a);

    }
    public void addGyroscopicData(GyroscopicData a){

        this.gyroscopicData.add(a);
        numberData++;
        System.out.println(this.numberData);

    }

    public int getId() {
        return id;
    }

    public int getNumberData() {
        return numberData;
    }




    public void inference(){
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{30, 9,1}, DataType.FLOAT32);


            int numElems = 30 * 9 * 1; // Total number of bits needed
            int numBytes = numElems * 4; // Calculate number of bytes needed


            ByteBuffer bitBuffer = ByteBuffer.allocate(numBytes); // Create the byte buffer

            // Set the order to little-endian to ensure correct bit ordering
            bitBuffer.order(ByteOrder.LITTLE_ENDIAN);

            // Set the limit to the capacity to ensure we don't accidentally write past the end
            bitBuffer.limit(bitBuffer.capacity());


            for (int i = 0; i < 30; i++) {

                bitBuffer.putFloat(this.accelerometerData.get(i).valueX);
                bitBuffer.putFloat(this.accelerometerData.get(i).valueY);
                bitBuffer.putFloat(this.accelerometerData.get(i).valueZ);
                bitBuffer.putFloat(this.gyroscopicData.get(i).valueY);
                bitBuffer.putFloat(this.gyroscopicData.get(i).valueZ);
                bitBuffer.putFloat(this.gyroscopicData.get(i).valueX);

                if(this.orientationData.size() > 60){
                    bitBuffer.putFloat(this.orientationData.get(i * 2).valueY);
                    bitBuffer.putFloat(this.orientationData.get(i * 2).valueZ);
                    bitBuffer.putFloat(this.orientationData.get(i * 2).valueX);

                }
                else{
                    bitBuffer.putFloat(this.orientationData.get(i).valueY);
                    bitBuffer.putFloat(this.orientationData.get(i).valueZ);
                    bitBuffer.putFloat(this.orientationData.get(i).valueX);
                }
            }

            inputFeature0.loadBuffer(bitBuffer);


            try {
                Tfconvertedmodel model = Tfconvertedmodel.newInstance(context.getApplicationContext());
                // Runs model inference and gets result.
                Tfconvertedmodel.Outputs outputs = model.process(inputFeature0);
                float[] outputFeature0  = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

                float max = -1;
                int index = -1;

                for(int i = 0; i< 2; i++){
                    System.out.println(outputFeature0[i]);
                    if(outputFeature0[i]>max){
                        max = outputFeature0[i];
                        index = i;
                    }
                }


               String[] lables = {"Fall","Normal"};

                // Releases model resources if no longer used.
                model.close();

                if(index == 1){
                    bgElement.setBackgroundColor(Color.RED);


                    Intent secondActivityIntent = new Intent(
                            context, questionFall.class
                    );

                    sensorManager.unregisterListener(accelerometer);
                    sensorManager.unregisterListener(gyroscopic);
                    sensorManager.unregisterListener(orientation);

                    context.startActivity(secondActivityIntent);

                }else{
                    bgElement.setBackgroundColor(Color.GREEN);
                }

                predict.setText(lables[index]);

                System.out.println(lables[index]);




            }catch (Exception e){
                System.out.println(e);
            }



            this.reset();

        }

    }





