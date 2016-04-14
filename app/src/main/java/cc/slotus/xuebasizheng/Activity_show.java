package cc.slotus.xuebasizheng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import cc.slotus.Util.Model;

/**
 * Created by mengkeer on 2015/10/31.
 */
public class Activity_show extends AppCompatActivity {

    int pid;
    int qid;
    int type;
    String title;
    String option;
    String answer;

    SharedPreferences pre;
    SharedPreferences.Editor edi;

    LinearLayout container;
    LinearLayout ll;
    int currentStyle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        container = (LinearLayout) findViewById(R.id.container);
        ll = (LinearLayout) findViewById(R.id.ll_show);
        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();
        currentStyle = pre.getInt("CurrentStyle", 100);

        initData();
        initToolBar();

        if (type == 1 || type == 3) {
            initSingalView();
        } else {
            initMutiView();
        }
        initButtomView();

        initBackground();

    }

    private void initButtomView() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 40;
        lp.rightMargin = 10;

        TextView tv1 = new TextView(this);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setText("正确答案为"+answer);
        tv1.setLayoutParams(lp);

        TextView tv2 = new TextView(this);

        String str1 = "";
        if(pid==0){
            str1="序章";
        }else{
            str1 = "第"+pid+"章";
        }

        String str2 = "其为";
        if(type==1){
            str2 = "单选";
        }else  if(type==2){
            str2= "多选";
        }else if(type==3){
            str2 = "判断";
        }


        tv2.setText("题目来源:" + str1 + ",第" + qid + "题" + "," + str2);
        tv2.setLayoutParams(lp);
        tv2.setGravity(Gravity.RIGHT);
        ll.addView(tv1);
        ll.addView(tv2);

    }


    private void initMutiView() {

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.topMargin = 30;
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.topMargin = 15;
        lp3.bottomMargin = 15;
        lp3.rightMargin = 5;
        lp3.leftMargin = 5;
        lp3.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp4.setMargins(5,5,5,5);

        ArrayList<String> optionList = anylse(option);
        String ans = answer.trim();


        TextView tv1 = new TextView(this);
        tv1.setTextColor(Color.parseColor("#3e6372"));
        tv1.setText(title);
        tv1.setTextSize(18);
        tv1.setLayoutParams(lp4);


        LinearLayout ll2 = new LinearLayout(this);
        ll2.setLayoutParams(lp2);
        ll2.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < optionList.size(); i++) {
            String str = optionList.get(i);
            AppCompatCheckBox cb = new AppCompatCheckBox(this);
            cb.setText(str);
            cb.setChecked(false);
            cb.setClickable(false);
            cb.setLayoutParams(lp3);
            cb.setPadding(20, 0, 0, 0);
//            cb.setButtonDrawable(getResources().getDrawable(R.drawable.checkbox_select));
            cb.setTextSize(16);
            cb.setTextColor(Color.parseColor("#3e6372"));
            ll2.addView(cb);
        }

        ArrayList<Integer> answer = new ArrayList<Integer>();
        for (int i = 0; i < ans.length(); i++) {
            answer.add(ans.charAt(i) - 'A');
        }

        for (int i : answer) {

            AppCompatCheckBox cb = (AppCompatCheckBox) ll2.getChildAt(i);
            cb.setChecked(true);
            cb.setTextColor(Color.parseColor("#ff6900"));
        }

        ll.addView(tv1);
        ll.addView(ll2);


    }

    private void initSingalView() {


        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.topMargin = 30;
        RadioGroup.LayoutParams lp3 = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.topMargin = 15;
        lp3.rightMargin=  5;
        lp3.bottomMargin = 15;
        lp3.leftMargin = 5;
        lp3.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;


        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp4.setMargins(5,5,5,5);


        ArrayList<String> optionList = anylse(option);
        String ans = answer.trim();
        if (ans.equals("对")) ans = "A";
        if (ans.equals("错")) ans = "B";
        int answer = ans.trim().charAt(0) - 'A';

        TextView tv1 = new TextView(this);
        tv1.setText(title);
        tv1.setTextColor(Color.parseColor("#3e6372"));
        tv1.setTextSize(18);
        tv1.setLayoutParams(lp4);

        RadioGroup rg = new RadioGroup(this);
        rg.setLayoutParams(lp2);


        if (option != null && !option.trim().equals("")) {

            for (int i = 0; i < optionList.size(); i++) {
                String str = optionList.get(i);
                AppCompatRadioButton radio = new AppCompatRadioButton(this);
                radio.setText(str);
                radio.setChecked(false);
                radio.setTextSize(16);
                radio.setClickable(false);
                radio.setPadding(20, 0, 0, 0);
//                radio.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
                radio.setTextColor(Color.parseColor("#3e6372"));
                radio.setLayoutParams(lp3);
                rg.addView(radio);
            }


        } else {

            AppCompatRadioButton radio1 = new AppCompatRadioButton(this);
            radio1.setText("A. 对");
            radio1.setChecked(false);
            radio1.setTextSize(16);
            radio1.setClickable(false);
            radio1.setPadding(20, 0, 0, 0);
//            radio1.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
            radio1.setTextColor(Color.parseColor("#3e6372"));
            radio1.setLayoutParams(lp3);
            rg.addView(radio1);

            AppCompatRadioButton radio2 = new AppCompatRadioButton(this);
            radio2.setText("B. 错");
            radio2.setChecked(false);
            radio2.setPadding(20, 0, 0, 0);
//            radio2.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
            radio2.setTextSize(16);
            radio2.setClickable(false);
            radio2.setLayoutParams(lp3);
            radio2.setTextColor(Color.parseColor("#3e6372"));
            rg.addView(radio2);

        }
        AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(answer);
        rb.setChecked(true);
        rb.setTextColor(Color.parseColor("#ff6900"));

        ll.addView(tv1);
        ll.addView(rg);

    }


    private ArrayList<String> anylse(String str) {
        ArrayList<String> list = new ArrayList<String>();
        String temp = "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch <= 'H' && ch >= 'A') {
                temp = ch + "";
                ch = str.charAt(++i);
                while (ch < 'A' || ch > 'H') {
                    temp += ch + "";
                    if (i >= str.length() - 1)
                        break;
                    ch = str.charAt(++i);
                }
                list.add(temp);
                --i;
            }

        }
        return list;
    }


    private void initData() {

        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", -1);
        qid = intent.getIntExtra("qid", -1);
        title = intent.getStringExtra("title");
        option = intent.getStringExtra("option");
        answer = intent.getStringExtra("answer");
        type = intent.getIntExtra("type", -1);


    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("学霸思政");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.action_about:
                    startActivity(new Intent(Activity_show.this, Activiy_About.class));
                    break;
                case R.id.action_settings:
                    startActivityForResult(new Intent(Activity_show.this, Activity_Settings.class), 1);
                    break;
                default:
                    break;
            }


            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            currentStyle = pre.getInt("CurrentStyle", 100);
            initBackground();
        }
    }

    void initBackground(){

        if(currentStyle==4||currentStyle==100){
            container.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }else if(currentStyle==1){
            container.setBackground(getResources().getDrawable(R.drawable.background1));
        }else if(currentStyle==2){
            container.setBackground(getResources().getDrawable(R.drawable.background2));
        }else if(currentStyle==3){
            container.setBackground(getResources().getDrawable(R.drawable.background3));
        }

    }

}
