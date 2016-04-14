package cc.slotus.xuebasizheng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;


public class Activity_WrongListSingle extends AppCompatActivity implements OnClickListener {
    int pis = 0;
    String course;
    int pid;
    int type;
    Intent intent;
    ArrayList<Model> list = new ArrayList<Model>();
    Model current;
    TextView tv;
    RadioGroup rg;
    Button btn;
    TextView tv_showPro;
    TextView tv_showWrong;
    TextView tv_showanswer;
    int wrongTimes = 0;
    static  boolean state = true;       //true表示按钮一次都没选中


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


        setContentView(R.layout.activity_wrongsingle);



        tv = (TextView) findViewById(R.id.WrongList_Singal_tv);
        tv_showPro = (TextView) findViewById(R.id.tv_leftnumber);
        tv_showWrong = (TextView) findViewById(R.id.tv_wrongnumber);
        rg = (RadioGroup) findViewById(R.id.WrongList_Singal_rg);
        btn = (Button) findViewById(R.id.btn_next);
        tv_showanswer = (TextView) findViewById(R.id.tv_showanswer);

        container= (LinearLayout) findViewById(R.id.container);
        currentStyle = pre.getInt("CurrentStyle", 100);
        isAudioOpen = pre.getInt("IsAudioOpen", 100);

        initToolBar();
        initData();
        initEffect();
        initOption();
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


    private void initOption() {

        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 15;
        lp.bottomMargin= 15;
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

        tv_showanswer.setText(" ");
        current = list.get(pis++);
        tv.setText(current.getTitle());


        btn.setBackground(getResources().getDrawable(R.drawable.shape2));
        GradientDrawable mmyGrad = (GradientDrawable) btn.getBackground();
        mmyGrad.setColor(0xff3e6372);

        rg.removeAllViews();
        rg.clearCheck();

        String option = current.getOption();

        if (option != null && !option.trim().equals("")) {

            ArrayList<String> optionList = anylse(option);
            for (int i = 0; i < optionList.size(); i++) {
                String str = optionList.get(i);
                AppCompatRadioButton radio = new AppCompatRadioButton(this);
                radio.setText(str);
                radio.setChecked(false);
                radio.setId(i);
                radio.setLayoutParams(lp);
                radio.setTextSize(16);


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
            radio1.setLayoutParams(lp);
            radio1.setTextSize(16);
            radio1.setPadding(20, 0, 0, 0);
//            radio1.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
            radio1.setTextColor(Color.parseColor("#3e6372"));
            rg.addView(radio1);

            AppCompatRadioButton radio2 = new AppCompatRadioButton(this);
            radio2.setText("B. 错");
            radio2.setLayoutParams(lp);
            radio2.setTextSize(16);
            radio2.setChecked(false);
            radio2.setPadding(20, 0, 0, 0);
//            radio2.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
            radio2.setId(1);
            radio2.setTextColor(Color.parseColor("#3e6372"));
            rg.addView(radio2);
        }

        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btn.setBackground(getResources().getDrawable(R.drawable.shape2));
                GradientDrawable myGrad = (GradientDrawable) btn.getBackground();
                myGrad.setColor(0xffff6900);
                if(state==false){
                    state = true;
                    btn.setBackground(getResources().getDrawable(R.drawable.shape2));
                    GradientDrawable mmyGrad = (GradientDrawable) btn.getBackground();
                    mmyGrad.setColor(0xff3e6372);
                    btn.setText("确认");
                }

            }
        });

        tv_showPro.setText("进度:"+pis + "/" + list.size());
        tv_showWrong.setText("错误:"+wrongTimes + "/" + list.size());


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
        course = pre.getString("subject", null);
        String p = pre.getString("unit", null);
        pid = Integer.parseInt(p);
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase
                .rawQuery("select * from " + course + " where flag = 1 and  pid = " + pid + " and ( type = 1 or type = 3 )", null);
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
        sqliteDatabase.close();
        myDbHelper.close();
    }

    @Override
    public void onClick(View v) {


        if (state) {
            String str = getAns();
            if (str.equals("")) {
                Toast.makeText(this, "未选中", Toast.LENGTH_SHORT).show();
                return;
            }

            String ans = current.getAnswer().trim();
            if (ans.equals("对")) ans = "A";
            if (ans.equals("错")) ans = "B";
            if (!ans.equals(str) && pis < list.size()) {
                wrongTimes++;
                btn.setText("下一题");
                tv_showanswer.setText("正确答案为：" + ans);
                setAllUnabled();
               setEffect(false);
                state = false;
                return;
            } else if (ans.equals(str) && pis < list.size()) {
                state = false;
                setEffect(true);
                initOption();
                return;
            }
            if (!ans.equals(str)) {
                wrongTimes++;
                btn.setText("退出");
                setAllUnabled();
               setEffect(false);
                tv_showanswer.setText("正确答案为：" + ans);
                state = false;
                return;
            }else{
                setEffect(true);
            }

        } else {
            if (pis < list.size()) {
                initOption();
                return;
            }
        }

        if (pis >= list.size()) {

            Toast.makeText(this, "以上为该章节错题", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    public void setAllUnabled(){
        for (int i = 0; i < rg.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(i);
            rb.setClickable(false);
        }
    }

    public String getAns() {
        String str = "";
        for (int i = 0; i < rg.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(i);
            if (rb.isChecked()) {
                str = (char) ('A' + i) + "";
            }
        }
        return str;
    }

    public void RemoveWrong(Model temp) {
        if (temp.getFlag() == 0) return;
        int id = temp.getId();
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        String sql = "update " + course + " set flag = 0 where id = " + id;
        sqliteDatabase.execSQL(sql);
        sqliteDatabase.close();
        myDbHelper.close();
    }

    @Override
    public void onBackPressed() {                                         //press back
      setBack();
    }


    public void setBack(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth = (int)(dm.widthPixels*0.8);
        int mheight = (int)(mwidth*0.6);

        LayoutInflater li = LayoutInflater.from(Activity_WrongListSingle.this);
        View view = li.inflate(R.layout.new_dialog, null);

        Button btn_dialogconfirm = (Button) view.findViewById(R.id.btn_dialogconfirm);
        Button btn_dialogcancel = (Button) view.findViewById(R.id.btn_dialogcancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_WrongListSingle.this);
        final AlertDialog ad = builder.create();
        ad.show();
        ad.getWindow().setContentView(view);
        ad.getWindow().setLayout(mwidth, mheight);

        btn_dialogconfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_WrongListSingle.this.finish();
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
        getMenuInflater().inflate(R.menu.menu_three, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    RemoveWrong(list.get(pis - 1));
                    if (pis < list.size()) {
                        Activity_WrongListSingle.state = false;
                        initOption();
                    } else {
                        Toast.makeText(Activity_WrongListSingle.this, "以上为该章节错题", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent();
                        it.setClass(Activity_WrongListSingle.this, Activity_ChooseQuestionType.class);
                        startActivity(it);
                        finish();
                    }
                    break;

                case R.id.action_settings:
                    startActivityForResult(new Intent(Activity_WrongListSingle.this, Activity_Settings.class), 1);
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Activity_WrongListSingle.this, Activiy_About.class));
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
