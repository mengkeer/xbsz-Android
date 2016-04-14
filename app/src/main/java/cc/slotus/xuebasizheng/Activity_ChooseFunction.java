package cc.slotus.xuebasizheng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Activity_ChooseFunction extends AppCompatActivity implements OnClickListener {
    private Button btn_recite;
    private Button btn_exercise;
    private Button btn_test;
    private Button btn_wronganswers;

    SharedPreferences pre;
    SharedPreferences.Editor edi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();

        setContentView(R.layout.activity_choosefunction);
        initToolBar();


        btn_recite = (Button) findViewById(R.id.btn_recite);
        btn_exercise = (Button) findViewById(R.id.btn_exercise);
        btn_test = (Button) findViewById(R.id.btn_test);
        btn_wronganswers = (Button) findViewById(R.id.btn_wronganswers);

        btn_recite.setOnClickListener(this);
        btn_exercise.setOnClickListener(this);
        btn_test.setOnClickListener(this);
        btn_wronganswers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recite:
                edi.putString("function", "recite");
                edi.commit();
                Intent it = new Intent();
                it.setClass(Activity_ChooseFunction.this, Activity_ChooseQuestionType.class);
                startActivity(it);
                break;

            case R.id.btn_exercise:
                edi.putString("function", "exercise");
                edi.commit();
                Intent it0 = new Intent();
                it0.setClass(Activity_ChooseFunction.this, Activity_ChooseQuestionType.class);
                startActivity(it0);
                break;

            case R.id.btn_test:
                edi.putString("function", "test");
                edi.commit();
                Intent it1 = new Intent();
                it1.setClass(Activity_ChooseFunction.this, Activity_Test.class);
                startActivity(it1);
                break;

            case R.id.btn_wronganswers:
                edi.putString("function", "wrong");
                edi.commit();
                Intent it2 = new Intent();
                it2.setClass(Activity_ChooseFunction.this, Activity_ChooseQuestionType.class);
                startActivity(it2);
                break;
            default:
                break;
        }
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
                    startActivity(new Intent(Activity_ChooseFunction.this, Activity_Search.class));
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Activity_ChooseFunction.this,Activiy_About.class));
                    break;
                case R.id.action_settings:
                    startActivity(new Intent(Activity_ChooseFunction.this,Activity_Settings.class));
                    break;
                default:
                    break;
            }


            return true;
        }
    };



}
