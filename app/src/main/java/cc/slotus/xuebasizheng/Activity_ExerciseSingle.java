package cc.slotus.xuebasizheng;

import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ExerciseSingle extends AppCompatActivity implements OnClickListener {
    int pis = 0;
    String course;
    int pid;
    Intent intent;
    ArrayList<Model> list = new ArrayList<Model>();
    ArrayList<Model> wronglist = new ArrayList<Model>();
    Model current;
    TextView tv;
    RadioGroup rg;
    Button btn;

    LinearLayout ll;
    String answer;

    TextView tv_leftnumber;             //显示做题进度
    TextView tv_wrongnumber;            //显示错误题数
    TextView tv_showanswer;             //用来显示正确答案
    int wrongtimes = 0;                 //用来标记错误次数
    int state = 1;          //用于标记第一次答题之后的状态
    double rate;

    private Boolean flag0 = false;              //表示开关灯状态
    Boolean flag = true;                          //表示是否选择了题目
    String ans, str;

    SharedPreferences pre;
    SharedPreferences.Editor edi;

    //初始化音频信息
    SoundPool soundPool;
    HashMap<Integer, Integer> soundMap =
            new HashMap<Integer, Integer>();

    LinearLayout container;
    int isAudioOpen;
    int currentStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();

        setContentView(R.layout.activity_singlechoice);


        tv = (TextView) findViewById(R.id.other_tv);
        tv_leftnumber = (TextView) findViewById(R.id.tv_leftnumber);
        tv_wrongnumber = (TextView) findViewById(R.id.tv_wrongnumber);
        tv_showanswer = (TextView) findViewById(R.id.tv_showanswer);
        rg = (RadioGroup) findViewById(R.id.other_rg);
        btn = (Button) findViewById(R.id.btn_next);

        ll = (LinearLayout) findViewById(R.id.other_ll);

        container= (LinearLayout) findViewById(R.id.container);
        currentStyle = pre.getInt("CurrentStyle", 100);
        isAudioOpen = pre.getInt("IsAudioOpen", 100);

        initData();
        initOption();
        initToolBar();
        initEffect();
        initBackground();
        btn.setOnClickListener(this);


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

    public void initEffect(){
        soundPool = new SoundPool(10
                , AudioManager.STREAM_SYSTEM, 5);  //①
        // load方法加载指定音频文件，并返回所加载的音频ID。
        // 此处使用HashMap来管理这些音频流
        soundMap.put(1, soundPool.load(this, R.raw.correct, 1));
        soundMap.put(2, soundPool.load(this, R.raw.mistake, 1));
    }


    private void initOption() {                                     //initOption

        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 15;
        lp.bottomMargin= 15;
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

        current = list.get(pis++);
        state = 1;
        tv_showanswer.setText("");
        btn.setText("确认");
        tv.setText(current.getTitle());

        String option = current.getOption();
        if (option != null && !option.trim().equals("")) {
            ArrayList<String> optionList = anylse(option);
//			Toast.makeText(this, optionList.size()+"", Toast.LENGTH_SHORT).show();
            rg.removeAllViews();
            rg.clearCheck();
            for (int i = 0; i < optionList.size(); i++) {
                String str = optionList.get(i);
                AppCompatRadioButton radio = new AppCompatRadioButton(this);
                radio.setText(str);
                radio.setChecked(false);
                radio.setId(i);
                radio.setTextSize(16);
                radio.setLayoutParams(lp);

                radio.setPadding(20, 0, 0, 0);
//                radio.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));

                radio.setTextColor(Color.parseColor("#3e6372"));
                rg.addView(radio);
            }
        } else {
            AppCompatRadioButton radio1 = new AppCompatRadioButton(this);
            radio1.setText("A. 对");
            radio1.setChecked(false);
            radio1.setId(0);
            radio1.setTextSize(16);
            radio1.setLayoutParams(lp);
            radio1.setTextColor(Color.parseColor("#3e6372"));
            radio1.setPadding(20, 0, 0, 0);
//            radio1.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
            rg.addView(radio1);

            AppCompatRadioButton radio2 = new AppCompatRadioButton(this);
            radio2.setText("B. 错");
            radio2.setChecked(false);
            radio2.setTextSize(16);
            radio2.setLayoutParams(lp);
            radio2.setId(1);
            radio2.setPadding(20, 0, 0, 0);
//            radio2.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
            radio2.setTextColor(Color.parseColor("#3e6372"));
            rg.addView(radio2);
        }

        tv_leftnumber.setText("进度:"+pis + "/" + list.size());
        tv_wrongnumber.setText("错误:"+wrongtimes + "/" + list.size());

        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                btn.setBackgroundColor(Color.parseColor("#ff6900"));
                btn.setBackground(getResources().getDrawable(R.drawable.shape2));
                GradientDrawable myGrad = (GradientDrawable) btn.getBackground();
                myGrad.setColor(0xffff6900);
                Log.e("mess", "将颜色设置为橙色");
                if (flag == false) {
                    flag = true;
                    Log.e("mess", "将颜色设置为蓝色");
                    btn.setBackground(getResources().getDrawable(R.drawable.shape2));
                    GradientDrawable mmyGrad = (GradientDrawable) btn.getBackground();
                    mmyGrad.setColor(0xff3e6372);

                }
            }
        });
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
                    if (i >= str.length() - 1) break;
                    ch = str.charAt(++i);
                }
                list.add(temp);
                --i;
            }

        }
        return list;
    }

    private void initData() {                                                //initData
        course = pre.getString("subject", null);
        String p = pre.getString("unit", null);
        pid = Integer.parseInt(p);
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.rawQuery("select * from " + course + " where id != 0 and  pid = " + pid + " and type = 1", null);
        while (cursor.moveToNext()) {
            Model temp = new Model();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int pid = cursor.getInt(cursor.getColumnIndex("pid"));
            int qid = cursor.getInt(cursor.getColumnIndex("qid"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String option = cursor.getString(cursor.getColumnIndex("option"));
            String answer = cursor.getString(cursor.getColumnIndex("answer"));
            int flag = cursor.getInt(cursor.getColumnIndex("flag"));

            temp.setId(id);
            temp.setPid(pid);
            temp.setQid(qid);
            temp.setType(type);
            temp.setTitle(title);
            temp.setOption(option);
            temp.setAnswer(answer);
            temp.setFlag(flag);
            list.add(temp);
        }
        Collections.shuffle(list);
    }

    @Override
    public void onClick(View v) {                                        //press next
        if (state == 0) {

            if (pis < list.size()) {
                initOption();
                return;
            }

        } else {
            str = getAns();
            if (str.equals("")) {
                Toast.makeText(this, "未选中", Toast.LENGTH_SHORT).show();
                return;
            }

            ans = current.getAnswer().trim();
            if (ans.equals("对")) ans = "A";
            if (ans.equals("错")) ans = "B";
            if (!ans.equals(str) && pis < list.size()) {
                flag = false;
                wrongtimes++;
                wronglist.add(list.get(pis - 1));
                setWrong(list.get(pis - 1));
                btn.setText("下一题");
                tv_showanswer.setText("正确答案为：" + ans);
                setAllUnabled();
                setEffect(false);           //设置错误音效
                state = 0;
                return;
            } else if (ans.equals(str) && pis < list.size()) {
                flag = false;
                setEffect(true);            //设置正确音效
                initOption();
                return;
            }
            if (!ans.equals(str)) {
                setEffect(false);
                wrongtimes++;
                flag = false;
                wronglist.add(list.get(pis - 1));
                setWrong(list.get(pis - 1));
                tv_showanswer.setText("正确答案为：" + ans);
                setAllUnabled();
                btn.setText("下一题");
                state = 0;
                return;
            } else if (ans.equals(str)) {
                flag = false;
                setEffect(true);
            }

        }

        if (pis >= list.size()) {
            if (wronglist.size() == 0) {
                Toast.makeText(this, "该章节单选复习完毕", Toast.LENGTH_SHORT);
                finish();
                return;
            } else {
                testfinish();
            }
        }

    }

    public void setWrong(Model temp) {
        if (temp.getFlag() == 1) return;
        int id = temp.getId();
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        String sql = "update " + course + " set flag = 1 where id = " + id;
        sqliteDatabase.execSQL(sql);
        sqliteDatabase.close();
        myDbHelper.close();
    }

    private void testfinish() {                                               //test finished

        //test finished
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth = (int)(dm.widthPixels*0.8);
        int mheight = (int)(mwidth*0.6);


        LayoutInflater li = LayoutInflater.from(Activity_ExerciseSingle.this);
        View view = li.inflate(R.layout.new_dialog2, null);

        Button btn_review = (Button) view.findViewById(R.id.btn_review);
        Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
        TextView tv_rate = (TextView) view.findViewById(R.id.tv_rate);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);

        double correct = list.size() - wronglist.size();
        double all = list.size();
        rate = (correct / all) * 100;
        tv_rate.setText((int) rate + "%");
        if (rate < 30) {
            tv_message.setText("学海无涯，回头是岸");
        } else if (rate < 60) {
            tv_message.setText("再接再厉");
        } else if (rate < 80) {
            tv_message.setText("力争上游");
        } else {
            tv_message.setText("做得不错");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ExerciseSingle.this);
        builder.setCancelable(false);
        final AlertDialog ad = builder.create();
        ad.show();
        ad.getWindow().setContentView(view);
        ad.getWindow().setLayout(mwidth, mheight);

        btn_review.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                list = (ArrayList<Model>) wronglist.clone();
                Collections.shuffle(list);
                wronglist.clear();
                pis = 0;
                wrongtimes = 0;
                initOption();
                ad.cancel();
            }
        });

        btn_exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity_ExerciseSingle.this.finish();
            }
        });
    }



    public void setAllUnabled(){
        for (int i = 0; i < rg.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(i);
            rb.setClickable(false);
        }
    }


    public String getAns() {                                            //get answer
        String str = "";
        for (int i = 0; i < rg.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(i);
            if (rb.isChecked()) {
                str = (char) ('A' + i) + "";
            }
        }
        return str;
    }

    @Override
    public void onBackPressed() {                                         //press back
      setBack();
    }


    public  void setBack(){

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth = (int)(dm.widthPixels*0.8);
        int mheight = (int)(mwidth*0.6);

        LayoutInflater li = LayoutInflater.from(Activity_ExerciseSingle.this);
        View view = li.inflate(R.layout.new_dialog, null);

        Button btn_dialogconfirm = (Button) view.findViewById(R.id.btn_dialogconfirm);
        Button btn_dialogcancel = (Button) view.findViewById(R.id.btn_dialogcancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ExerciseSingle.this);
        final AlertDialog ad = builder.create();
        ad.show();
        ad.getWindow().setContentView(view);
        ad.getWindow().setLayout(mwidth, mheight);

        btn_dialogconfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_ExerciseSingle.this.finish();
            }
        });

        btn_dialogcancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("学霸思政");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setBack();
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
                    startActivity(new Intent(Activity_ExerciseSingle.this,Activiy_About.class));
                    break;
                case R.id.action_settings:
                    startActivityForResult(new Intent(Activity_ExerciseSingle.this,Activity_Settings.class),1);
                    break;
                default:
                    break;
            }

            return true;
        }
    };

    public void setEffect(boolean isRight){

        if (isRight&&isAudioOpen==1) {
            soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);  //③
        } else if(!isRight&&isAudioOpen==1) {
            soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);  //③
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            currentStyle = pre.getInt("CurrentStyle", 100);
            isAudioOpen = pre.getInt("IsAudioOpen", 100);
            initBackground();
        }
    }
}

