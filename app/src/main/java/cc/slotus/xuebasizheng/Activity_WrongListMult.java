package cc.slotus.xuebasizheng;

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
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;


public class Activity_WrongListMult extends AppCompatActivity implements OnClickListener {
    int pis = 0;
    String course;
    int pid;
    int type;
    Intent intent;
    ArrayList<Model> list = new ArrayList<Model>();
    ArrayList<Model> wrong_list = new ArrayList<Model>();
    Model current;
    TextView tv;
    Button btn;
    Button remove_btn;
    LinearLayout ll;
    TextView tv_showPro;
    TextView tv_showWrong;
    TextView tv_showanswer;
    int wrongTimes = 0;
    boolean state = true;
    int count = 0;
    int time;

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


        setContentView(R.layout.activity_wrongmult);



        tv = (TextView) findViewById(R.id.WrongList_Muti_tv);
        tv_showPro = (TextView) findViewById(R.id.tv_leftnumber);
        tv_showWrong = (TextView) findViewById(R.id.tv_wrongnumber);
        tv_showanswer = (TextView) findViewById(R.id.tv_showanswer);
        ll = (LinearLayout) findViewById(R.id.WrongList_Muti_ll);
        btn = (Button) findViewById(R.id.btn_next);

        container= (LinearLayout) findViewById(R.id.container);
        currentStyle = pre.getInt("CurrentStyle", 100);
        isAudioOpen = pre.getInt("IsAudioOpen", 100);


        initData();
        initToolBar();
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

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 15;
        lp.bottomMargin = 15;
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

        state = true;
        time = 0;

        btn.setText("确认");
        btn.setBackground(getResources().getDrawable(R.drawable.shape2));
        GradientDrawable mmyGrad = (GradientDrawable) btn.getBackground();
        mmyGrad.setColor(0xff3e6372);

        tv_showanswer.setText(" ");
        current = list.get(pis++);
        tv.setText(current.getTitle());
        tv.setTextColor(Color.parseColor("#3e6372"));

        String option = current.getOption();
        ArrayList<String> optionList = anylse(option);
        ll.removeAllViews();
        for (int i = 0; i < optionList.size(); i++) {
            String str = optionList.get(i);
            final AppCompatCheckBox cb = new AppCompatCheckBox(this);
            cb.setText(str);
            cb.setChecked(false);
            cb.setId(i);
            cb.setLayoutParams(lp);
            cb.setPadding(20, 0, 0, 0);
//            cb.setButtonDrawable(getResources().getDrawable(R.drawable.checkbox_select));
            cb.setTextSize(16);
            cb.setTextColor(Color.parseColor("#3e6372"));
            ll.addView(cb);

            cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cb.isChecked()) {
                        time++;
                        btn.setBackground(getResources().getDrawable(R.drawable.shape2));
                        GradientDrawable myGrad = (GradientDrawable) btn.getBackground();
                        myGrad.setColor(0xffff6900);
                    } else {
                        time--;
                        if (time < 1) {
                            btn.setBackground(getResources().getDrawable(R.drawable.shape2));
                            GradientDrawable mmyGrad = (GradientDrawable) btn.getBackground();
                            mmyGrad.setColor(0xff3e6372);
                        }
                    }
                }
            });
        }
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
                .rawQuery("select * from " + course + " where flag = 1 and  pid = " + pid + " and type = 2", null);
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

//        if (v.getId() == R.id.btn_remove) {
//            RemoveWrong(list.get(pis - 1));
//            if (pis < list.size()) {
//                initOption();
//            } else {
//                Toast.makeText(this, "以上为该章节错题", Toast.LENGTH_SHORT).show();
//                Intent it = new Intent();
//                it.setClass(this, Activity_ChooseQuestionType.class);
//                startActivity(it);
//                finish();
//            }
//            return;
//        }

        if (state) {
            String str = getAns();
            if (str.equals("")) {
                Toast.makeText(this, "未选择", Toast.LENGTH_SHORT).show();
                return;
            }

            String ans = current.getAnswer().trim();
            if (!ans.equals(str) && pis < list.size()) {
                wrongTimes++;
                btn.setText("下一题");
                setEffect(false);
                setAllUnabled();
                tv_showanswer.setText("正确答案为：" + ans);
                state = false;
                return;
            } else if (ans.equals(str) && pis < list.size()) {
                setEffect(true);
                initOption();
                return;
            }
            if (!ans.equals(str)) {
                wrongTimes++;
                setEffect(false);
                setAllUnabled();
                btn.setText("退出");
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

        }
    }

    public void setAllUnabled(){
        for (int i = 0; i < ll.getChildCount(); i++) {
            AppCompatCheckBox cb = (AppCompatCheckBox) ll.getChildAt(i);
            cb.setClickable(false);
        }
    }

    public String getAns() {
        String str = "";
        for (int i = 0; i < ll.getChildCount(); i++) {
            AppCompatCheckBox cb = (AppCompatCheckBox) ll.getChildAt(i);
            if (cb.isChecked()) {
                str += (char) ('A' + i);
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

        LayoutInflater li = LayoutInflater.from(Activity_WrongListMult.this);
        View view = li.inflate(R.layout.new_dialog, null);

        Button btn_dialogconfirm = (Button) view.findViewById(R.id.btn_dialogconfirm);
        Button btn_dialogcancel = (Button) view.findViewById(R.id.btn_dialogcancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_WrongListMult.this);
        final AlertDialog ad = builder.create();
        ad.show();
        ad.getWindow().setContentView(view);
        ad.getWindow().setLayout(mwidth, mheight);

        btn_dialogconfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_WrongListMult.this.finish();
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
                        initOption();
                    } else {
                        Toast.makeText(Activity_WrongListMult.this, "以上为该章节错题", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent();
                        it.setClass(Activity_WrongListMult.this, Activity_ChooseQuestionType.class);
                        startActivity(it);
                        finish();
                    }
                    break;

                case R.id.action_settings:
                    startActivityForResult(new Intent(Activity_WrongListMult.this, Activity_Settings.class), 1);
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Activity_WrongListMult.this, Activiy_About.class));
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
