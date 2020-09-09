package com.dai.temperatureconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.EditText;
import android.text.method.ScrollingMovementMethod;



public class MainActivity extends AppCompatActivity {

        private RadioButton ftc;
        private RadioButton ctf;
        private EditText input;
        private TextView output;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private SharedPreference prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ftc = (RadioButton) findViewById(R.id.ftc);
        ctf = (RadioButton) findViewById(R.id.ctf);
        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.history);

        textView3.setMovementMethod(new ScrollingMovementMethod());
        prefs = new SharedPreference(this);
        output.setText(prefs.getValue(getString(R.string.outputKey)));
        textView3.setText(prefs.getValue(getString(R.string.historyKey)));
    }

     public void Convert(View v){
        if(input.getText().length() != 0){
            double in = Double.parseDouble(input.getText().toString());
            double out = 0.0;
            String res = null;
            String historyOut = textView3.getText().toString();
            if (ftc.isChecked()){
                out = (in - 32.0) / 1.8;
                res = String.format("%,.1f", in) + "F ==> " + String.format("%,.1f", out) + "C";
            }
            else if (ctf.isChecked()){
                out = (in * 1.8) + 32;
                res = String.format("%,.1f", in) + "C ==> " + String.format("%,.1f", out) + "F";
            }
            output.setText(String.format("%,.1f", out));
            textView3.setText(res + "\n" + historyOut);
            input.setText("");
            prefs.save(getString(R.string.outputKey),String.format("%,.1f", out));
            prefs.save(getString(R.string.historyKey),res + "\n" + historyOut);

        }
     }

     public void RadioClicked(View v){
        if (ftc.isChecked()){
            textView1.setText("Fahrenheit Degree:");
            textView2.setText("Celsius Degree:");
            input.setText("");
            output.setText("");
        }
        else if (ctf.isChecked()){
            textView1.setText("Celsius Degree:");
            textView2.setText("Fahrenheit Degree:");
            input.setText("");
            output.setText("");
        }
    }

     public void Clear(View v){
        input.setText("");
        output.setText("");
        textView3.setText("");
     }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", textView3.getText().toString());
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textView3.setText(savedInstanceState.getString("HISTORY"));
    }

}
