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
public class Adapter_ListView_Search extends BaseAdapter implements Filterable {

    int len = 0;

    private PersonFilter filter;

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    private ArrayList<String> list;
    private Context con;


    public Adapter_ListView_Search(ArrayList<String> list, Context con) {
        this.con = con;
        this.list = list;
        len = list.size();
    }


    @Override
    public int getCount() {
        if(list.size()==0){
            return 0;
        }
        if(list.size()<len){
            return list.size();
        }
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if(position==list.size()){
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


        if(position==list.size()&&position!=0&&len==list.size()){
            View view = View.inflate(con, R.layout.listview_search_buttom, null);
            return view;
        }

        View view = View.inflate(con, R.layout.listview_search_item, null);
        String text = list.get(position);



        TextView tv_listview = (TextView) view.findViewById(R.id.tv_search);

        tv_listview.setText(text);

        return view;


    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PersonFilter(list);
        }
        return filter;
    }


    private class PersonFilter extends Filter {

        private ArrayList<String> original;

        public PersonFilter(ArrayList<String> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = original;
                results.count = original.size();
            } else {
                ArrayList mList = new ArrayList();
                for (String p : original) {

                    if (p.contains(constraint)) {
                        mList.add(p);
                    }

                }
                results.values = mList;
                results.count = mList.size();
//                Log.e("结果个数为",results.count+"");
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }


}
