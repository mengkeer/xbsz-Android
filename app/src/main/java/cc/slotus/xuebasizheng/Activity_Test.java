package cc.slotus.xuebasizheng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cc.slotus.Util.DataBaseHelper;
import cc.slotus.Util.Model;

public class Activity_Test extends AppCompatActivity implements OnClickListener {

	// 初始化全局信息
	int pis = 0;
	String course;
	Intent intent;
	ArrayList<Model> list = new ArrayList<Model>();
	Model current;
	int score = 0;
	int STATUS = 0;		//用来标志该Activity是否摧毁过
	// 初始化单选组件
	TextView other_tv;
	RadioGroup rg;
	Button other_btn;
	TextView other_tv_showPro;

	LinearLayout container;
	int currentStyle;

	// 初始化多选组件
	TextView muti_tv;
	Button muti_btn;
	LinearLayout ll;
	LinearLayout ll0;
	TextView muti_tv_showPro;
	int count = 0;

	SharedPreferences pre;
	SharedPreferences.Editor edi;
	Boolean flag = false;
	Boolean mode = true;

	TextView showTime;
	SimpleDateFormat format = new SimpleDateFormat("mm:ss");
	Date date = new Date();
	long currentTime = 0;

	 CountDownTimer timer = new CountDownTimer(1201000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			currentTime = millisUntilFinished;
			if(STATUS==1){
				timer.cancel();
			}
			date.setTime(millisUntilFinished);
			showTime.setText("时间:"+format.format(millisUntilFinished) + "");
		}

		@Override
		public void onFinish() {
			testfinish();
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pre = getSharedPreferences("local", MODE_WORLD_READABLE);
		edi = pre.edit();


		setContentView(R.layout.activity_testsingle);

		// 初始化单选组件
		other_tv = (TextView) findViewById(R.id.other_tv);
		other_tv_showPro = (TextView) findViewById(R.id.tv_leftnumber);
		rg = (RadioGroup) findViewById(R.id.other_rg);
		other_btn = (Button) findViewById(R.id.btn_next);
		ll = (LinearLayout) findViewById(R.id.other_ll);
		showTime = (TextView) findViewById(R.id.tv_showtime);

		showTime.setText("时间:20:00");

		container= (LinearLayout) findViewById(R.id.container);
		currentStyle = pre.getInt("CurrentStyle", 100);

		// 获取所有题库数据
		initData();
		// 初始化单选信息
		initSingalOption();
		//初始化菜单信息
		initToolBar();
		initBackground();

		other_btn.setOnClickListener(this);
		timer.start();
	}

	void initBackground(){

		if(currentStyle==4||currentStyle==100){
			container.setBackgroundColor(Color.parseColor("#f4f4f4"));
		}else if(currentStyle==1){
			container.setBackground(getResources().getDrawable(R.drawable.background1));
		}else if(currentStyle==2){
			container.setBackground(getResources().getDrawable(R.drawable.background2));
		}else if(currentStyle==3){
			container.setBackground(getResources().getDrawable(R.drawable.background3));
		}

	}

	private void initSingalOption() {

		RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.topMargin = 15;
		lp.bottomMargin= 15;
		lp.rightMargin = 5;
		lp.leftMargin = 5;
		lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

		current = list.get(pis++);
		other_tv.setText(current.getTitle());
		rg.removeAllViews();
		rg.clearCheck();

		other_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
		GradientDrawable myGrad = (GradientDrawable) other_btn.getBackground();
		myGrad.setColor(0xff3e6372);

		String option = current.getOption();

		if (option != null && !option.trim().equals("")) {

			ArrayList<String> optionList = anylse(option);
			for (int i = 0; i < optionList.size(); i++) {
				String str = optionList.get(i);
				AppCompatRadioButton radio = new AppCompatRadioButton(this);
				radio.setText(str);
				radio.setLayoutParams(lp);
				radio.setTextSize(16);
				radio.setChecked(false);
				radio.setId(i);

				radio.setPadding(20, 0, 0, 0);
//				radio.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));

				radio.setTextColor(Color.parseColor("#3e6372"));

				radio.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						other_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
						GradientDrawable myGrad = (GradientDrawable) other_btn.getBackground();
						myGrad.setColor(0xffff6900);
					}
				});
				rg.addView(radio);
			}
		} else {

			AppCompatRadioButton radio1 = new AppCompatRadioButton(this);
			radio1.setText("A. 对");
			radio1.setChecked(false);
			radio1.setId(0);
			radio1.setLayoutParams(lp);
			radio1.setTextSize(16);
			radio1.setPadding(20, 0, 0, 0);
//			radio1.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
			radio1.setTextColor(Color.parseColor("#3e6372"));

			radio1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					other_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
					GradientDrawable myGrad = (GradientDrawable) other_btn.getBackground();
					myGrad.setColor(0xffff6900);
				}
			});
			rg.addView(radio1);

			AppCompatRadioButton radio2 = new AppCompatRadioButton(this);
			radio2.setText("B. 错");
			radio2.setChecked(false);
			radio2.setLayoutParams(lp);
			radio2.setPadding(20, 0, 0, 0);
//			radio2.setButtonDrawable(getResources().getDrawable(R.drawable.radio_select));
			radio2.setTextSize(16);
			radio2.setId(1);

			radio2.setTextColor(Color.parseColor("#3e6372"));

			radio2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					other_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
					GradientDrawable myGrad = (GradientDrawable) other_btn.getBackground();
					myGrad.setColor(0xffff6900);
				}
			});
			rg.addView(radio2);
		}

		other_tv_showPro.setText("进度:"+pis + "/" + list.size());
	}

	private void initMutiOption() {

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.topMargin = 15;
		lp.bottomMargin =15;
		lp.rightMargin = 5;
		lp.leftMargin = 5;
		lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;


		count = 0;
		current = list.get(pis++);
		muti_tv.setText(current.getTitle());
		String option = current.getOption();
		ArrayList<String> optionList = anylse(option);
		ll.removeAllViews();

		muti_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
		GradientDrawable myGrad = (GradientDrawable) muti_btn.getBackground();
		myGrad.setColor(0xff3e6372);

		for (int i = 0; i < optionList.size(); i++) {
			String str = optionList.get(i);
			final AppCompatCheckBox cb = new AppCompatCheckBox(this);
			cb.setText(str);
			cb.setChecked(false);
			cb.setId(i);
			cb.setLayoutParams(lp);
			cb.setTextSize(16);
			cb.setTextColor(Color.parseColor("#3e6372"));

			cb.setPadding(20, 0, 0, 0);
//			cb.setButtonDrawable(getResources().getDrawable(R.drawable.checkbox_select));

			ll.addView(cb);

			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (cb.isChecked()) {
						count++;
						muti_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
						GradientDrawable myGrad = (GradientDrawable) muti_btn.getBackground();
						myGrad.setColor(0xffff6900);

					} else {
						count--;
						if (count < 1) {
							muti_btn.setBackground(getResources().getDrawable(R.drawable.shape2));
							GradientDrawable myGrad = (GradientDrawable) muti_btn.getBackground();
							myGrad.setColor(0xff3e6372);
						}
					}
				}
			});
		}
		muti_tv_showPro.setText(pis + "/" + list.size());

	}

	private ArrayList<String> anylse(String str) {
		ArrayList<String> list = new ArrayList<String>();
		String temp = "";
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (ch <= 'H' && ch >= 'A') {
				temp = ch + "";
				ch = str.charAt(++i);
				while (ch < 'A' || ch > 'H') {
					temp += ch + "";
					if (i >= str.length() - 1)
						break;
					ch = str.charAt(++i);
				}
				list.add(temp);
				--i;
			}

		}
		return list;
	}

	private void initData() {
		course = pre.getString("subject", null);

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase
				.rawQuery("select * from " + course + " where type = 1 or type = 3 order by  random() limit 60", null);
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

		}

		cursor = sqliteDatabase.rawQuery("select * from " + course + " where type = 2 order by  random() limit 20",
				null);
		while (cursor.moveToNext()) {
			Model temp = new Model();
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
			list.add(temp);

		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_next && mode == true) {
			setSignalClick();
		} else {
			setMutiClick();
		}
	}

	private void setMutiClick() {

		String str = getMutiAns();
		if (str.equals("")) {
			Toast.makeText(this, "没有选项被选中", Toast.LENGTH_SHORT).show();
			return;
		}

		String ans = current.getAnswer().trim();

		if (pis < 80) {
			if (!ans.equals(str)) {
				setWrong(list.get(pis - 1));
			} else {
				score += 2;
			}
			initMutiOption();
			return;
		}
		if (pis == 80) {

			if (!ans.equals(str)) {
				setWrong(list.get(pis - 1));
			} else {
				score += 2;
			}
			testfinish();

		}

	}

	private void testfinish() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mwidth = (int)(dm.widthPixels*0.8);
		int mheight = (int)(mwidth*0.6);

		LayoutInflater li = LayoutInflater.from(Activity_Test.this);
		View view = li.inflate(R.layout.new_dialog_test, null);

		Button btn_dialogconfirm = (Button) view.findViewById(R.id.btn_dialogconfirm);
		btn_dialogconfirm.setWidth((int)(mwidth*0.7));
		TextView tv_score = (TextView) view.findViewById(R.id.tv_score);
		TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
		if (score < 30) {
			tv_message.setText("学海无涯，回头是岸");
		} else if (score < 60) {
			tv_message.setText("再接再厉");
		} else if (score < 80) {
			tv_message.setText("力争上游");
		} else {
			tv_message.setText("做得不错");
		}

		tv_score.setText("得分：" + score);

		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Test.this);
		builder.setCancelable(false);
		final AlertDialog ad = builder.create();
		ad.show();
		ad.getWindow().setContentView(view);
		ad.getWindow().setLayout(mwidth, mheight);

		btn_dialogconfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void setSignalClick() {

		String str = getSingalAns();
		if (str.equals("")) {
			Toast.makeText(this, "没有选项被选中", Toast.LENGTH_SHORT).show();
			return;
		}

		String ans = current.getAnswer().trim();
		if (ans.equals("对"))
			ans = "A";
		if (ans.equals("错"))
			ans = "B";

		if (pis < 60) {
			if (!ans.equals(str)) {
				setWrong(list.get(pis - 1));
			} else {
				score++;
			}
			initSingalOption();
			return;
		}
		if (pis == 60) {

			if (!ans.equals(str)) {
				setWrong(list.get(pis - 1));
			} else {
				score++;
			}

			// 初始化多选组件
			mode = false;
			setContentView(R.layout.activity_testmuti);
			initToolBar();

			muti_tv = (TextView) findViewById(R.id.muti_tv);
			muti_btn = (Button) findViewById(R.id.btn_next);
			ll = (LinearLayout) findViewById(R.id.muti_ll);
			ll0 = (LinearLayout) findViewById(R.id.mult_ll0);
			muti_tv_showPro = (TextView) findViewById(R.id.tv_leftnumber);
			showTime = (TextView) findViewById(R.id.tv_showtime);
			container= (LinearLayout) findViewById(R.id.container);
			initBackground();

			date.setTime(currentTime);
			showTime.setText("时间:"+format.format(date) + "");



			muti_btn.setOnClickListener(this);
			initMutiOption();
		}

	}

	public void setWrong(Model temp) {
		if (temp.getFlag() == 1)
			return;
		int id = temp.getId();
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		SQLiteDatabase sqliteDatabase = myDbHelper.getReadableDatabase();
		String sql = "update " + course + " set flag = 1 where id = " + id;
		sqliteDatabase.execSQL(sql);
		sqliteDatabase.close();
		myDbHelper.close();
	}

	public String getSingalAns() {
		String str = "";
		for (int i = 0; i < rg.getChildCount(); i++) {
			AppCompatRadioButton rb = (AppCompatRadioButton) rg.getChildAt(i);
			if (rb.isChecked()) {
				str = (char) ('A' + i) + "";
			}
		}
		return str;
	}

	public String getMutiAns() {
		String str = "";
		for (int i = 0; i < ll.getChildCount(); i++) {
			AppCompatCheckBox cb = (AppCompatCheckBox) ll.getChildAt(i);
			if (cb.isChecked()) {
				str += (char) ('A' + i);
			}
		}
		return str;
	}

	@Override
	public void onBackPressed() { // press back
		setBack();
	}


	public void setBack(){

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mwidth = (int)(dm.widthPixels*0.8);
		int mheight = (int)(mwidth*0.6);

		LayoutInflater li = LayoutInflater.from(Activity_Test.this);
		View view = li.inflate(R.layout.new_dialog, null);

		Button btn_dialogconfirm = (Button) view.findViewById(R.id.btn_dialogconfirm);
		Button btn_dialogcancel = (Button) view.findViewById(R.id.btn_dialogcancel);

		AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Test.this);
		final AlertDialog ad = builder.create();
		ad.show();
		ad.getWindow().setContentView(view);
		ad.getWindow().setLayout(mwidth, mheight);

		btn_dialogconfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity_Test.this.finish();
			}
		});

		btn_dialogcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ad.cancel();
			}
		});
	}

	@Override
	protected void onDestroy() {
		STATUS = 1;
		super.onDestroy();
	}

	@Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

	@Override
    protected void onRestart() {
        super.onRestart();
        timer = new CountDownTimer(currentTime, 1000) {
    		@Override
    		public void onTick(long millisUntilFinished) {
    			currentTime = millisUntilFinished;
    			if(STATUS==1){
    				timer.cancel();
    			}
    			date.setTime(millisUntilFinished);
    			showTime.setText("时间:"+format.format(millisUntilFinished) + "");
    		}

    		@Override
    		public void onFinish() {
    			testfinish();
    		}
    	};
        timer.start();

    }


	private void initToolBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
		toolbar.setTitle("学霸思政");
		toolbar.setNavigationIcon(R.drawable.back);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setBack();
			}
		});
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}


	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {

			switch (menuItem.getItemId()) {
				case R.id.action_about:
					startActivity(new Intent(Activity_Test.this,Activiy_About.class));
					break;
				case R.id.action_settings:
					startActivityForResult(new Intent(Activity_Test.this, Activity_Settings.class), 1);
					break;
				default:
					break;
			}


			return true;
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			currentStyle = pre.getInt("CurrentStyle", 100);
			initBackground();
		}
	}

}
