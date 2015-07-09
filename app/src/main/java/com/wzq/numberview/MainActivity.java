package com.wzq.numberview;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wzq.library.NumberView;
import com.wzq.library.NumberViewGroup;
import com.wzq.library.PaintProvider;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements PaintProvider, View.OnClickListener{

    private Button go, stop;

    private NumberView singleNum;

    private NumberViewGroup[] groupNum = new NumberViewGroup[4];

    private int time;

    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go = (Button) findViewById(R.id.go);go.setOnClickListener(this);
        stop = (Button) findViewById(R.id.stop);stop.setOnClickListener(this);
        singleNum = (NumberView) findViewById(R.id.single_num);
        groupNum[0] = (NumberViewGroup) findViewById(R.id.group_num);
        groupNum[1] = (NumberViewGroup) findViewById(R.id.group_num1);
        groupNum[2] = (NumberViewGroup) findViewById(R.id.group_num2);
        groupNum[3] = (NumberViewGroup) findViewById(R.id.group_num3);
        for (int i = 0; i < groupNum.length; i++) {
            groupNum[i].setPaintProvider(this);
        }
        timer.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    singleNum.advance(time++);
                    int[] temp = getTime();
                    for (int i = 0; i < groupNum.length; i++) {
                        groupNum[i].advance(temp[i]);
                    }
                }
            });
        }
    }

    private int[] getTime() {
        int[] temp = new int[groupNum.length];
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse("2015-07-30T12:59:00");
            long l = date.getTime() - new Date().getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            DecimalFormat decimalFormat = new DecimalFormat("00");

            temp[0] = Integer.valueOf(decimalFormat.format(day));
            temp[1] = Integer.valueOf(decimalFormat.format(hour));
            temp[2] = Integer.valueOf(decimalFormat.format(min));
            temp[3] = Integer.valueOf(decimalFormat.format(s));
        } catch (Exception e) {
        }
        return temp;
    }

    @Override
    public void mutate(Paint paint, int digit) {
        paint.setTextSize(sp2px(this, 24));
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        if(view == stop){
            timer.cancel();
        }else{
            timer.cancel();
            timer = new Timer();
            timer.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
