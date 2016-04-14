package cc.slotus.xuebasizheng;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;
import cc.slotus.Util.ViewPagerAdapter;

public class Activity_Recite extends AppCompatActivity implements OnPageChangeListener, OnClickListener {
    int pis = 0;
    String course;
    String pid;
    int type;
    Intent intent;
    Button btn_last;
    Button btn_next;
    ViewPager vp;
    ViewPagerAdapter adapter;
    ArrayList<Model> list = new ArrayList<Model>();
    List<View> views = new ArrayList<View>();
    EditText et_jumpnumber;         //设置要跳转的可编辑文本框     //要跳转的页面值
    Model current;
    TextView tv_leftnumber;

    SharedPreferences pre;
    SharedPreferences.Editor edi;

    LinearLayout container;
    int currentStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();


        setContentView(R.layout.activity_recite);


        btn_last = (Button) findViewById(R.id.btn_last);
        btn_next = (Button) findViewById(R.id.btn_next);
        vp = (ViewPager) findViewById(R.id.vp_one);
        tv_leftnumber = (TextView) findViewById(R.id.tv_leftnumber);
        container= (LinearLayout) findViewById(R.id.container);
        currentStyle = pre.getInt("CurrentStyle", 100);

        initData();
        initToolBar();

        if (type == 1)
            initSingalView();
        else if (type == 2) {
            initMutiView();
        }
        adapter = new ViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(this);
        btn_last.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        tv_leftnumber.setText("1/" + list.size());
        initBackground();
        initProgress();
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


    private void initMutiView() {

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp2.topMargin = 30;
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp3.topMargin = 15;
        lp3.bottomMargin = 15;
        lp3.rightMargin = 5;
        lp3.leftMargin = 5;
        lp3.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp4.setMargins(5,5,5,5);


        for (Model temp : list) {

            String title = temp.getTitle();
            String option = temp.getOption();
            ArrayList<String> optionList = anylse(option);
            String ans = temp.getAnswer().trim();


            ScrollView sv = new ScrollView(this);
            sv.setLayoutParams(lp);


            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(lp);
            ll.setOrientation(LinearLayout.VERTICAL);

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
                cb.setId(i);
                cb.setLayoutParams(lp3);
                cb.setPadding(20, 0, 0, 0);
//                cb.setButtonDrawable(getResources().getDrawable(R.drawable.checkbox_select));
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

            sv.addView(ll);


            views.add(sv);

        }

    }

    private void initSingalView() {

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp2.topMargin = 30;
        RadioGroup.LayoutParams lp3 = new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp3.topMargin = 15;
        lp3.bottomMargin = 15;
        lp3.rightMargin = 5;
        lp3.leftMargin = 5;
        lp3.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp4.setMargins(5,5,5,5);


        for (Model temp : list) {

            String title = temp.getTitle();
            String option = temp.getOption();
            ArrayList<String> optionList = anylse(option);
            String ans = temp.getAnswer().trim();
            if (ans.equals("对")) ans = "A";
            if (ans.equals("错")) ans = "B";
            int answer = ans.trim().charAt(0) - 'A';

            ScrollView sv = new ScrollView(this);
            sv.setLayoutParams(lp);


            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(lp);
            ll.setOrientation(LinearLayout.VERTICAL);

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
                    radio.setId(i);
                    radio.setTextSize(16);
                    radio.setClickable(false);
                    radio.setPadding(20, 0, 0, 0);
//                    radio.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
                    radio.setTextColor(Color.parseColor("#3e6372"));
                    radio.setLayoutParams(lp3);
                    rg.addView(radio);
                }


            } else {

                AppCompatRadioButton radio1 = new AppCompatRadioButton(this);
                radio1.setText("A. 对");
                radio1.setChecked(false);
                radio1.setId(0);
                radio1.setTextSize(16);
                radio1.setClickable(false);
                radio1.setPadding(20, 0, 0, 0);
//                radio1.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
                radio1.setTextColor(Color.parseColor("#3e6372"));
                radio1.setLayoutParams(lp3);
                rg.addView(radio1);

                AppCompatRadioButton radio2 = new AppCompatRadioButton(this);
                radio2.setText("B. 错");
                radio2.setChecked(false);
                radio2.setId(1);
                radio2.setPadding(20, 0, 0, 0);
//                radio2.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
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

            sv.addView(ll);

            views.add(sv);

        }

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
        pid = pre.getString("unit", null);
        type = pre.getInt("type", 0);
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();

        String sql = "";
        if (type == 1) {
            sql = "select * from " + course + " where id != 0 and  pid = " + pid + " and ( type = 1 or type = 3 )";
        } else if (type == 2) {
            sql = "select * from " + course + " where id != 0 and  pid = " + pid + " and type = " + type;
        }

        Cursor cursor = sqliteDatabase
                .rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Model temp = new Model();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int pid = cursor.getInt(cursor.getColumnIndex("pid"));
            int qid = cursor.getInt(cursor.getColumnIndex("qid"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String option = cursor.getString(cursor.getColumnIndex("option"));
            String answer = cursor.getString(cursor.getColumnIndex("answer"));

            temp.setId(id);
            temp.setPid(pid);
            temp.setQid(qid);
            temp.setType(type);
            temp.setTitle(title);
            temp.setOption(option);
            temp.setAnswer(answer);
            list.add(temp);

        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int pos) {
        // Toast.makeText(this, "第" + pos + "页", Toast.LENGTH_SHORT).show();
        if (pos == list.size() - 1) {
            btn_next.setText("退出");
        } else {
            btn_next.setText("下一题");
        }
        tv_leftnumber.setText(vp.getCurrentItem() + 1 + "/" + list.size());
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_last) {
            int pos = vp.getCurrentItem();
            if (pos > 0)
                vp.setCurrentItem(--pos);
            if (pos == 0) {
                Toast.makeText(this, "当前已经是第一页了哦", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.btn_next) {

            int pos = vp.getCurrentItem();
            if (pos < list.size() - 1) {
                vp.setCurrentItem(++pos);
            } else {
                setCurrentProgress(course,Integer.parseInt(pid),type,0);
                finish();
            }

        }

    }

    @Override
    public void onBackPressed() {
        setBack();
    }


    public void setBack(){

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth = (int)(dm.widthPixels*0.8);
        int mheight = (int)(mwidth*0.6);

        LayoutInflater li = LayoutInflater.from(Activity_Recite.this);
        View view = li.inflate(R.layout.new_dialog, null);

        Button btn_dialogconfirm = (Button) view.findViewById(R.id.btn_dialogconfirm);
        Button btn_dialogcancel = (Button) view.findViewById(R.id.btn_dialogcancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Recite.this);
        final AlertDialog ad = builder.create();
        ad.show();
        ad.getWindow().setContentView(view);
        ad.getWindow().setLayout(mwidth, mheight);

        btn_dialogconfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                course = pre.getString("subject", null);
                pid = pre.getString("unit", null);
                type = pre.getInt("type", 0);
                setCurrentProgress(course,Integer.parseInt(pid),type,vp.getCurrentItem()+1);
                Activity_Recite.this.finish();
            }
        });

        btn_dialogcancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });
    }

    private void setCurrentProgress(String course, int pid, int type, int value) {

        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        String sql = "replace into progress values ('"+course+"' ,'"+pid+"','"+type+"','"+value+"' )";
        sqliteDatabase.execSQL(sql);
        sqliteDatabase.close();
        myDbHelper.close();

    }

    private void initProgress() {
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        String sql = "select * from progress where course = '"+course+"' and pid = '"+pid+"' and type = '"+type+"' ";
        Cursor cursor = sqliteDatabase.rawQuery(sql, null);
        int value = -1;
        while (cursor.moveToNext()) {
            value = cursor.getInt(cursor.getColumnIndex("value"));
//            Log.e("mess",value+"");
            break;
        }
        if(value!=-1&&value!=0&&value<=list.size()){
            vp.setCurrentItem(value-1);
        }
        sqliteDatabase.close();
        myDbHelper.close();
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
        getMenuInflater().inflate(R.menu.menu_one, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_skip:
                    setJumpAction();
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Activity_Recite.this, Activiy_About.class));
                    break;
                case R.id.action_settings:
                    startActivityForResult(new Intent(Activity_Recite.this, Activity_Settings.class), 1);
                    break;
                default:
                    break;
            }


            return true;
        }
    };


    public void setJumpAction() {


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mwidth = (int)(dm.widthPixels*0.8);
        int mheight = (int)(mwidth*0.6);


        final LayoutInflater li = LayoutInflater.from(Activity_Recite.this);
        View view = li.inflate(R.layout.new_dialog_recite, null);
        Button btn_junmpconfirm = (Button) view.findViewById(R.id.btn_junmpconfirm);
        et_jumpnumber = (EditText) view.findViewById(R.id.et_jumpnumber);
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Recite.this);

        final AlertDialog dialog  = builder.create();
        dialog .setView(new EditText(this));
        dialog .show();
        dialog .getWindow().setContentView(view);
        dialog .getWindow().setLayout(mwidth, mheight);
        btn_junmpconfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                String text = et_jumpnumber.getText().toString();
                if(text==null||text.equals("")){
                    Toast.makeText(Activity_Recite.this,"未输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                int  number = Integer.parseInt(text);
                Log.e("mess", "跳转页面为" + number);
                Log.e("mess", "总页面数为" + list.size());
                if(number==0||number>list.size()){
                    Toast.makeText(Activity_Recite.this,"不存在此页",Toast.LENGTH_SHORT).show();
                }else{
                    vp.setCurrentItem(number - 1);
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            currentStyle = pre.getInt("CurrentStyle", 100);
            initBackground();
        }
    }

}
