package cc.slotus.xuebasizheng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;

/**
 * Created by mengkeer on 2015/10/28.
 */
public class Activity_Settings extends AppCompatActivity {

    Switch aswitch;
    RadioGroup rg;
    Button btn;

    SharedPreferences pre;
    SharedPreferences.Editor edi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolBar();

        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();


        aswitch = (Switch) findViewById(R.id.switch_setting);
        rg = (RadioGroup) findViewById(R.id.rg_setting);
        btn = (Button) findViewById(R.id.btn_setting);


//        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.topMargin = 10;
//        lp.bottomMargin = 10;
//
//
//        RadioButton one=  new RadioButton(this);
//        one.setText("小鸟");
//        one.setTextSize(16);
//        one.setTextColor(Color.parseColor("#3e6372"));
//        one.setLayoutParams(lp);
//
//        RadioButton two=  new RadioButton(this);
//        two.setText("树叶");
//        two.setTextSize(16);
//        two.setTextColor(Color.parseColor("#3e6372"));
//        two.setLayoutParams(lp);
//
//        RadioButton three=  new RadioButton(this);
//        three.setText("水墨");
//        three.setTextSize(16);
//        three.setTextColor(Color.parseColor("#3e6372"));
//        three.setLayoutParams(lp);
//
//        RadioButton four=  new RadioButton(this);
//        four.setText("默认");
//        four.setTextSize(16);
//        four.setTextColor(Color.parseColor("#3e6372"));
//        four.setLayoutParams(lp);
//
//        rg.addView(one);
//        rg.addView(two);
//        rg.addView(three);
//        rg.addView(four);




        int isAudioOpen = pre.getInt("IsAudioOpen", 100);
        int currentStyle = pre.getInt("CurrentStyle", 100);

        if (isAudioOpen == 100 || isAudioOpen == 2) {
            aswitch.setChecked(false);
        } else if (isAudioOpen == 1) {
            aswitch.setChecked(true);
        }

        if (currentStyle == 100||currentStyle==4 ) {
            AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(3);
            rb.setChecked(true);
        } else {
            AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(currentStyle - 1);
            rb.setChecked(true);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isAudioOpen = aswitch.isChecked() ? 1 : 2;
                int currentStyle = 0;
                for (int i = 0; i < rg.getChildCount(); i++) {
                    AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(i);
                    if (rb.isChecked()) {
                        currentStyle = i+1;
                        break;
                    }
                }

                edi.putInt("IsAudioOpen",isAudioOpen);
                edi.putInt("CurrentStyle",currentStyle);
                edi.commit();

                finish();

            }
        });

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("设置");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
