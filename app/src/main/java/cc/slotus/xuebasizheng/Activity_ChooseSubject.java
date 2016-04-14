package cc.slotus.xuebasizheng;

import java.io.IOException;

import cc.slotus.Util.DataBaseHelper;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.market.sdk.XiaomiUpdateAgent;

public class Activity_ChooseSubject extends AppCompatActivity implements OnClickListener {
    private ImageView iv_mao1;
    private ImageView iv_mao2;
    private ImageView iv_marx;
    private ImageView iv_thought;
    private ImageView iv_history;
    private TextView tv_history;
    private TextView tv_thought;
    private TextView tv_mao1;
    private TextView tv_mao2;
    private TextView tv_marx;
    private LinearLayout ll_history;
    private LinearLayout ll_thought;
    private LinearLayout ll_mao1;
    private LinearLayout ll_mao2;
    private LinearLayout ll_marx;
    SharedPreferences pre;
    SharedPreferences.Editor edi;
    private long exit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();

        setContentView(R.layout.activity_choosesubject);
        
        initToolBar();

        iv_mao1 = (ImageView) findViewById(R.id.iv_mao1);
        iv_mao2 = (ImageView) findViewById(R.id.iv_mao2);
        iv_marx = (ImageView) findViewById(R.id.iv_marx);
        iv_thought = (ImageView) findViewById(R.id.iv_thought);
        iv_history = (ImageView) findViewById(R.id.iv_history);
        tv_thought = (TextView) findViewById(R.id.tv_thought);
        tv_history = (TextView) findViewById(R.id.tv_history);
        tv_mao1 = (TextView) findViewById(R.id.tv_mao1);
        tv_mao2 = (TextView) findViewById(R.id.tv_mao2);
        tv_marx = (TextView) findViewById(R.id.tv_marx);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        ll_thought = (LinearLayout) findViewById(R.id.ll_thought);
        ll_mao1 = (LinearLayout) findViewById(R.id.ll_mao1);
        ll_mao2 = (LinearLayout) findViewById(R.id.ll_mao2);
        ll_marx = (LinearLayout) findViewById(R.id.ll_marx);

        iv_mao1.setOnClickListener(this);
        iv_mao2.setOnClickListener(this);
        iv_marx.setOnClickListener(this);
        iv_thought.setOnClickListener(this);
        iv_history.setOnClickListener(this);
        tv_thought.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        tv_mao1.setOnClickListener(this);
        tv_mao2.setOnClickListener(this);
        tv_marx.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_thought.setOnClickListener(this);
        ll_mao1.setOnClickListener(this);
        ll_mao2.setOnClickListener(this);
        ll_marx.setOnClickListener(this);

        if (isFirst()) initDb();

    }


    public boolean isFirst() {
        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        int versionCode = 0;
        int code = 0;
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pinfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pinfo.versionCode;
            code = pre.getInt("versionCode", 0);
        } catch (Exception e) {
        }

        edi = pre.edit();
        if (code == 0) {
            edi.putInt("versionCode", versionCode);
            edi.commit();
            return true;
        }

        if (versionCode > code) {
            edi.putInt("versionCode", versionCode);
            edi.commit();
            return true;
        }

        return false;
    }

    private void initDb() {
        DataBaseHelper dbh = new DataBaseHelper(this);
        try {
            dbh.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mao1:
                write("mao1");
                break;
            case R.id.tv_mao1:
                write("mao1");
                break;
            case R.id.ll_mao1:
                write("mao1");
                break;

            case R.id.iv_mao2:
                write("mao2");
                break;
            case R.id.tv_mao2:
                write("mao2");
                break;
            case R.id.ll_mao2:
                write("mao2");
                break;

            case R.id.iv_marx:
                write("marx");
                break;
            case R.id.tv_marx:
                write("marx");
                break;
            case R.id.ll_marx:
                write("marx");
                break;

            case R.id.iv_thought:
                write("thought");
                break;
            case R.id.tv_thought:
                write("thought");
                break;
            case R.id.ll_thought:
                write("thought");
                break;

            case R.id.iv_history:
                write("history");
                break;
            case R.id.tv_history:
                write("history");
                break;
            case R.id.ll_history:
                write("history");
                break;
            default:
                break;
        }
    }

    private void write(String string) {
        edi.putString("subject", string);
        edi.commit();
        Intent it = new Intent();
        it.setClass(Activity_ChooseSubject.this, Activity_ChooseFunction.class);
        startActivity(it);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exit) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exit = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("学霸思政");
        toolbar.setNavigationIcon(R.drawable.icon);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(Activity_ChooseSubject.this, Activiy_About.class);
                startActivity(it);
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_about:
                    startActivity(new Intent(Activity_ChooseSubject.this,Activiy_About.class));
                    break;
                case R.id.action_settings:
                    startActivity(new Intent(Activity_ChooseSubject.this,Activity_Settings.class));
                    break;
                default:
                    break;
            }


            return true;
        }
    };

}
