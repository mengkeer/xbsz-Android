package cc.slotus.Util;

import java.util.List;

import cc.slotus.xuebasizheng.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_ListView extends BaseAdapter{
	private List<List_View> myData;
	private Context con;
	
	public Adapter_ListView(List myData,Context con){
		this.con=con;
		this.myData=myData;
	}

	@Override
	public int getCount() {
		return myData.size();
	}

	@Override
	public Object getItem(int position) {
		return myData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=View.inflate(con, R.layout.listview_item, null);
		List_View lv=myData.get(position);
		
		TextView tv_listview=(TextView) view.findViewById(R.id.tv_listview);
		ImageView iv_listview=(ImageView) view.findViewById(R.id.iv_listview);
		
		tv_listview.setText(lv.getName());
		iv_listview.setImageResource(lv.getImage());
		
		return view;
	}

}
