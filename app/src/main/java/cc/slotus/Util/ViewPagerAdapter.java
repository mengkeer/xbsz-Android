package cc.slotus.Util;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	private List<View> views;

	public ViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return this.views.size();
	}

	// ��ʼ��position����
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		
//		((ViewPager)container.get).addView(views.get(position));
//		return views.get(position);
		ViewGroup parent = (ViewGroup) views.get(position).getParent();
		if(parent!=null){
			parent.removeAllViews();
		}
		container.addView(views.get(position));
		return views.get(position);
	}

	// �ж��Ƿ��ж������ɽ���
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}
}
