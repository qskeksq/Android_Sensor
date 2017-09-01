package com.example.administrator.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import static com.example.administrator.sensor.R.id.graph;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sm;
    Sensor s, g;
    GraphX graphX, graph2, graph3, graph4, graph5, graph6;
    GraphY graphY;
    float x,y,z, g1,g2,g3;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        g = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);

        graphX = (GraphX) findViewById(graph);
        graph2 = (GraphX) findViewById(R.id.graph2);
        graph3 = (GraphX) findViewById(R.id.graph3);

        graph4 = (GraphX) findViewById(R.id.graph4);
        graph5 = (GraphX) findViewById(R.id.graph5);
        graph6 = (GraphX) findViewById(R.id.graph6);

    }

    private void getSensorInfo(){
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        for(Sensor sensor : sensors) {
            StringBuilder sb = new StringBuilder();
            sb.append("name : "+sensor.getName()).append("\n");
            sb.append("type : "+sensor.getType()).append("\n");
            sb.append("vendor : "+sensor.getVendor()).append("\n");
            sb.append("minDelay : "+sensor.getMinDelay()).append("\n");
            sb.append("resolution : "+sensor.getResolution()).append("\n");
            Log.e("sensor info", sb.toString());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sor = sensorEvent.sensor;
        if(sor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            Log.e("Sensor value called", Arrays.toString(sensorEvent.values));
        } else if(sor.getType() == Sensor.TYPE_PRESSURE) {
            g1 = sensorEvent.values[0];
//            g2 = sensorEvent.values[1];
//            g3 = sensorEvent.values[2];
        }
        graphX.setPoint((int)x*10);
        graph2.setPoint((int)y*10);
        graph3.setPoint((int)z*10);

        graph4.setPoint((int)g1*10);
//        graph5.setPoint((int)g2*10);
//        graph6.setPoint((int)g3*10);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
}
