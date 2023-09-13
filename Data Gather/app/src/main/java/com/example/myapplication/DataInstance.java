package com.example.myapplication;


import java.util.ArrayList;
import java.util.List;

public class DataInstance {

    public int id;

    public String label;

    public int numberData;
    public List<AccelerometerData> accelerometerData;
    public List<OrientationData> orientationData;
    public List<GyroscopicData> gyroscopicData;


    public DataInstance(String label, int idTeste ){

        accelerometerData = new ArrayList<>();
        orientationData = new ArrayList<>();
        gyroscopicData = new ArrayList<>();
        numberData = 0;
        this.label = label;
        this.id = idTeste;
    }

    public void addAccelerometerData(AccelerometerData a){

        this.accelerometerData.add(a);

    }

    public void addOrientation(OrientationData a){

        this.orientationData.add(a);

    }
    public void addGyroscopicData(GyroscopicData a){

        this.gyroscopicData.add(a);

    }

    public int getId() {
        return id;
    }

    public int getNumberData() {
        return numberData;
    }

    public void incNumberData() {
        numberData++;

    }
}

