package cc.slotus.Util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.slotus.xuebasizheng.R;

/**
 * Created by mengkeer on 2015/10/30.
 */
public class Adapter_ListView_SearchResults extends BaseAdapter {

    public ArrayList<Model> getList() {
        return list;
    }

    public void setList(ArrayList<Model> list) {
        this.list = list;
    }

    private ArrayList<Model> list;
    private Context con;


    public Adapter_ListView_SearchResults(ArrayList<Model> list, Context con) {
        this.con = con;
        this.list = list;
    }


    @Override
    public int getCount() {

        if (list.size()==0){
            return  list.size()+1;
        }

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if(list.size()==0){
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position==0&&list.size()==0){
            View view = View.inflate(con, R.layout.listview_sr_buttom, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_search_item);
            tv.setText("一个都没搜索到");
            return view;

        }

        if(position==20){
            View view = View.inflate(con, R.layout.listview_sr_buttom, null);
            return view;
        }


        View view = View.inflate(con, R.layout.listview_sr_item, null);


        String text = list.get(position).getTitle()+list.get(position).getOption();

        TextView tv_listview = (TextView) view.findViewById(R.id.tv_sr);
        TextView tv_order = (TextView) view.findViewById(R.id.tv_order);

        int type = list.get(position).getType();
        String str = "";
        if(type==1){
            str = "单选";
        }else  if(type==2){
            str= "多选";
        }else if(type==3){
            str = "判断";
        }
        tv_order.setText("结果"+(position+1)+"\t"+str);
        tv_listview.setText(text);

        return view;


    }


}
