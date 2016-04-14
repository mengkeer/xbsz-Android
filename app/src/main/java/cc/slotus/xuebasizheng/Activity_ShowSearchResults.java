package cc.slotus.xuebasizheng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import cc.slotus.Util.Adapter_ListView_Search;
import cc.slotus.Util.Adapter_ListView_SearchResults;
import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;

/**
 * Created by mengkeer on 2015/10/31.
 */
public class Activity_ShowSearchResults extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<Model> list = new ArrayList<>();

    String lastText;

    EditText et;
    Button btn;
    ListView lv;

    SharedPreferences pre;
    SharedPreferences.Editor edi;
    private Adapter_ListView_SearchResults adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsearchresults);

        pre = getSharedPreferences("local", MODE_WORLD_READABLE);
        edi = pre.edit();

        et = (EditText) findViewById(R.id.et_text);
        btn = (Button) findViewById(R.id.btn_startSearch);
        lv = (ListView) findViewById(R.id.lv_searchresults);

        Intent intent = getIntent();
        et.setText(intent.getStringExtra("text"));
        lastText = et.getText().toString();


        initData();
        adapter = new Adapter_ListView_SearchResults(list, this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = et.getText().toString();

                if(text==null||text.equals("")){
                    Toast.makeText(Activity_ShowSearchResults.this,"未输入",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(text.equals(lastText)){
                    Toast.makeText(Activity_ShowSearchResults.this,"亲，不要重复搜索，这样我很为难",Toast.LENGTH_SHORT).show();
                    return;
                }

                lastText = text;

                DataBaseHelper myDbHelper = new DataBaseHelper(Activity_ShowSearchResults.this);
                SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
                sqliteDatabase.execSQL("replace into search values ('" + text + "')");
                sqliteDatabase.close();
                myDbHelper.close();

                initData();
                adapter.setList(list);
                lv.setAdapter(adapter);
            }
        });




    }

    public  void initData() {
        list.clear();
        String course = pre.getString("subject", null);
        String text = et.getText().toString();
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();

        Cursor cursor = sqliteDatabase.rawQuery("select * from "+course+" where id != 0 and ( title like '%"+text+"%' or option like '%"+text+"%' )", null);

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
            if(list.size()>=21)     break;

        }

        sqliteDatabase.close();
        myDbHelper.close();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position>=20||list.size()==0){
            return;
        }

        Model temp = list.get(position);
        Intent intent = new Intent(this,Activity_show.class);
        intent.putExtra("title",temp.getTitle());
        intent.putExtra("option",temp.getOption());
        intent.putExtra("answer",temp.getAnswer());
        intent.putExtra("pid",temp.getPid());
        intent.putExtra("qid",temp.getQid());
        intent.putExtra("type",temp.getType());

        startActivity(intent);

    }
}
