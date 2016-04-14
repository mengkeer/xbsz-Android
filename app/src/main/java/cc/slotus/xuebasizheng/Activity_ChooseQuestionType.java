package cc.slotus.xuebasizheng;

import cc.slotus.Util.Adapter_ListView;
import cc.slotus.Util.Adapter_type;
import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.List_View;
import cc.slotus.Util.Model;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class Activity_ChooseQuestionType extends AppCompatActivity implements OnPageChangeListener, OnClickListener, OnItemClickListener {

    boolean IsEmpty = true;

    private ViewPager vp_choosetype;
    private List<View> lv;
    private Adapter_type adapter;
    private LinearLayout ll_singleormult;
    private Button[] points;
    private int current;
    private int[] images = {R.mipmap.katongxiaodongwu_04, R.mipmap.katongxiaodongwu_05, R.mipmap.katongxiaodongwu_09,
            R.mipmap.katongxiaodongwu_10, R.mipmap.katongxiaodongwu_13, R.mipmap.katongxiaodongwu_20, R.mipmap.katongxiaodongwu_21,
            R.mipmap.katongxiaodongwu_22};
    private ListView lv_type1;
    private ListView lv_type2;
    private Adapter_ListView adapter2;
    private Adapter_ListView adapter3;
    SharedPreferences pre;
    SharedPreferences.Editor edi;

    private String s;
    String func;
    private String[] str;
    private ArrayList<String> WrongSingalStr = new ArrayList<>();
    private ArrayList<String> WrongMutiStr = new ArrayList<>();
    boolean flag = true;            //表示是否是单多选


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();
        s = pre.getString("subject", null);
        func = pre.getString("function", null);

        setContentView(R.layout.activity_choosequestiontype);
        initToolBar();

        initView();
        initData();



        if(func.equals("wrong")&&IsEmpty&&WrongSingalStr.size()==0&&WrongMutiStr.size()==0){
            Toast.makeText(this, "当前无错题", Toast.LENGTH_SHORT).show();
            IsEmpty = false;
        }

    }

    private void initView() {
        vp_choosetype = (ViewPager) findViewById(R.id.vp_choosetype);
        ll_singleormult = (LinearLayout) findViewById(R.id.ll_singleormult);
        lv = new ArrayList<View>();
        adapter = new Adapter_type(lv);

        LayoutInflater li = getLayoutInflater();
        View view1 = li.inflate(R.layout.listview_type, null);
        lv_type1 = (ListView) view1.findViewById(R.id.lv_type);
        adapter2 = new Adapter_ListView(intiList(), this);
        lv_type1.setAdapter(adapter2);
        lv_type1.setOnItemClickListener(this);

        flag = false;
        View view2 = li.inflate(R.layout.listview_type, null);
        lv_type2 = (ListView) view2.findViewById(R.id.lv_type);
        adapter3 = new Adapter_ListView(intiList(), this);
        lv_type2.setAdapter(adapter3);
        lv_type2.setOnItemClickListener(this);
        flag = true;

        lv.add(lv_type1);
        lv.add(lv_type2);
    }

    private void initData() {
        initPoint();
        vp_choosetype.setAdapter(adapter);
        vp_choosetype.setOnPageChangeListener(this);
    }


    private List intiList() {

        DataBaseHelper dbh = new DataBaseHelper(this);
        SQLiteDatabase sd = dbh.getReadableDatabase();
        Cursor cursor = sd.rawQuery("select * from " + s + " where id = 0", null);
        Model temp = new Model();
        while (cursor.moveToNext()) {
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
        }


        List<List_View> list = new ArrayList<List_View>();
        str = temp.getOption().split(",");

        if (pre.getString("function", null).equals("wrong")) {
            for (int i = 0; i < str.length; i++) {
                List_View lvlv = new List_View();
                String name = "";
//                edi.putString("unit", str[i]);
//                edi.commit();
                boolean flag0 = charge(str[i]);
                if (flag0) {
                    if (str[i].equals("0")) {
                        name = "序章";
                    } else {
                        name = "章节" + str[i];
                    }
                    lvlv.setName(name);
                    lvlv.setImage(images[i]);
                    list.add(lvlv);
                    if (flag) WrongSingalStr.add(str[i]);
                    else {
                        WrongMutiStr.add(str[i]);
                    }
                    Log.e("mess", "将错题章节添加" + str[i]);
                }

            }
        } else {
            String name = "";
            for (int i = 0; i < str.length; i++) {
                List_View lvlv = new List_View();
                if (str[i].equals("0")) {
                    name = "序章";
                } else {
                    name = "章节" + str[i];
                }
                lvlv.setName(name);
                lvlv.setImage(images[i]);
                list.add(lvlv);
            }
        }

        return list;
    }

    private void initPoint() {
        points = new Button[2];
        for (int i = 0; i < ll_singleormult.getChildCount(); i++) {
            points[i] = (Button) ll_singleormult.getChildAt(i);
            points[i].setBackgroundColor(Color.parseColor("#3e6372"));
            points[i].setTag(i);
            points[i].setOnClickListener(this);
        }
        current = 0;
        points[current].setBackgroundColor(Color.parseColor("#ff6900"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case "0":
                int i = (int) v.getTag();
                vp_choosetype.setCurrentItem(i);
                flag = true;
                break;

            case "1":
                int j = (int) v.getTag();
                vp_choosetype.setCurrentItem(j);
                flag = false;
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {

        setPoint(position);
    }

    private void setPoint(int position) {
        for (int i = 0; i < ll_singleormult.getChildCount(); i++) {
            points[i] = (Button) ll_singleormult.getChildAt(i);
            points[i].setBackgroundColor(Color.parseColor("#3e6372"));
        }
        points[position].setBackgroundColor(Color.parseColor("#ff6900"));
        if (position == 0) {
            flag = true;
        } else {
            flag = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        edi.putString("unit", str[position]);
        edi.commit();
        Intent it = new Intent();

        if (flag == true) {
            switch (func) {
                case "exercise":
                    it.setClass(this, Activity_ExerciseSingle.class);
                    startActivity(it);
                    break;

                case "recite":
                    edi.putInt("type", 1);
                    edi.commit();
                    it.setClass(this, Activity_Recite.class);
                    startActivity(it);
                    break;

                case "wrong":

                    Log.e("mess", "postion位置" + position);
                    Log.e("mess", "WrongStr长度" + WrongSingalStr.size());
                    Log.e("mess", WrongSingalStr.get(position));
                    Log.e("mess", "haha");
                    edi.putString("unit", WrongSingalStr.get(position));
                    edi.commit();

                    it.setClass(this, Activity_WrongListSingle.class);
                    startActivity(it);
                    finish();

                    break;

                default:
                    break;
            }
        } else {
            switch (func) {
                case "exercise":
                    it.setClass(this, Activity_ExerciseMult.class);
                    startActivity(it);
                    break;

                case "recite":
                    edi.putInt("type", 2);
                    edi.commit();
                    it.setClass(this, Activity_Recite.class);
                    startActivity(it);
                    break;

                case "wrong":
                    Log.e("mess", "postion位置" + position);
                    Log.e("mess", "WrongStr长度" + WrongMutiStr.size());
                    edi.putString("unit", WrongMutiStr.get(position));
                    edi.commit();
                    it.setClass(this, Activity_WrongListMult.class);
                    startActivity(it);
                    finish();
                    break;

                default:
                    break;
            }
        }
    }

    private boolean charge(String str) {
        String course = pre.getString("subject", null);
        int pid = Integer.parseInt(str);

        DataBaseHelper dbh = new DataBaseHelper(this);
        SQLiteDatabase sd = dbh.getReadableDatabase();


        String sql = "";
        if (flag) {
            sql = "select * from " + course + " where id != 0 and  pid = " + pid + " and ( type = 1 or type = 3 ) and flag = 1";
        } else {
            sql = "select * from " + course + " where id != 0 and  pid = " + pid + " and type = 2 and flag = 1";
        }

        Cursor cursor = sd.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            return true;
        }
        return false;
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
        getMenuInflater().inflate(R.menu.menu_two, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    startActivity(new Intent(Activity_ChooseQuestionType.this, Activity_Search.class));
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Activity_ChooseQuestionType.this,Activiy_About.class));
                    break;
                case R.id.action_settings:
                    startActivity(new Intent(Activity_ChooseQuestionType.this,Activity_Settings.class));
                    break;
                default:
                    break;
            }


            return true;
        }
    };





}
