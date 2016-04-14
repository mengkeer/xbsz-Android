package cc.slotus.xuebasizheng;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cc.slotus.Util.Adapter_ListView_Search;
import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;

/**
 * Created by mengkeer on 2015/10/30.
 */
public class Activity_Search extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private  long lastTime = 0;

    private SearchView sv;
    private ListView lv;
    private Adapter_ListView_Search adapter;
    ArrayList<String> list = new ArrayList<>();

//    private final String[]  strs = {"aaaa","abbb","accc","bba","bbc","aaaa","abbb","accc","bba","bbc","ccd","asd","sdsg","asd","fhg","eryty","rrtty"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        sv = (SearchView) findViewById(R.id.sv_search);
        lv = (ListView) findViewById(R.id.lv_search);

        initListView();

        lv.setOnItemClickListener(this);
        lv.setTextFilterEnabled(true);
        sv.setOnQueryTextListener(this);

    }

    private void initListView(){
        list.clear();
        initData();
        adapter = new Adapter_ListView_Search(list, this);
        lv.setAdapter(adapter);
    }


    private void initData() {                                                //initData

        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.rawQuery("select * from  search", null);

        while (cursor.moveToNext()) {

            String text = cursor.getString(cursor.getColumnIndex("text"));
            list.add(text);

        }
        Collections.reverse(list);
        sqliteDatabase.close();
        myDbHelper.close();

    }



    @Override
    public boolean onQueryTextSubmit(String text) {

        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
        sqliteDatabase.execSQL("replace into search values ('" + text + "')");
        sqliteDatabase.close();
        myDbHelper.close();

        Intent intent = new Intent(Activity_Search.this,Activity_ShowSearchResults.class);
        intent.putExtra("text",text);
        startActivity(intent);
        finish();

        return false;

    }

    @Override
    public boolean onQueryTextChange(String text) {
        if(TextUtils.isEmpty(text)){
            adapter.getFilter().filter(text);
        }else{
            adapter.getFilter().filter(text);
        }
        return  true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if(position==list.size()){
            DataBaseHelper myDbHelper = new DataBaseHelper(this);
            SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
            sqliteDatabase.execSQL("delete from search");
            sqliteDatabase.close();
            myDbHelper.close();

            initListView();


        }else{

            TextView tv = (TextView) view.findViewById(R.id.tv_search);
            Intent intent = new Intent(Activity_Search.this,Activity_ShowSearchResults.class);
            intent.putExtra("text",tv.getText());
            startActivity(intent);
            finish();

        }


    }
}
