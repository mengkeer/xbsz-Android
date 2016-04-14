package cc.slotus.xuebasizheng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xiaomi.market.sdk.XiaomiUpdateAgent;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XiaomiUpdateAgent.update(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Activity_ChooseSubject.class);
                MainActivity.this.startActivity(i);
                MainActivity.this.finish();
            }
        }, 2000);
    }
}
