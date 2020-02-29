package com.lupa.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final int DATA_COUNT = 10;
    float[] data;

    MyChart myChart;
    TextView labelData;
    Button btnGenerateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myChart = findViewById(R.id.chart);
        labelData = findViewById(R.id.labelData);
        btnGenerateData = findViewById(R.id.btnGenerateData);

        btnGenerateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomData();
                showData();
                myChart.setChartData(data);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (data != null) {
            myChart.setChartData(data);
            showData();
        }
    }

    private void generateRandomData() {
        Random random = new Random();
        data = new float[DATA_COUNT];

        for (int i = 0; i < data.length; i ++) {
            data[i] = random.nextInt(100);
        }
    }

    private void showData() {
        if (data == null) return;

        labelData.setText("");

        for (int i = 0; i < data.length; i ++) {
            labelData.setText("" + labelData.getText() + (int)data[i] + " ");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloatArray("data", data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        data = savedInstanceState.getFloatArray("data");
    }
}
