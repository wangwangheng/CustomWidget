package wang.cn.customwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * MainActivity
 *
 * @author wangheng
 */
public class MainActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.roundRectLayout).setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roundRectLayout:
                startActivity(new Intent(MainActivity.this,RoundRectLayoutActivity.class));
                break;
            default:

                break;
        }
    }
}
