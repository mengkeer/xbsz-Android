package cc.slotus.Util;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class Adapter_type extends PagerAdapter{
	private List<View> lv;
	
	public Adapter_type(List<View> lv){
		this.lv=lv;
	}

	@Override
	public int getCount() {
		return this.lv.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(lv.get(position));
		return lv.get(position);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(lv.get(position));
	}
	
}
