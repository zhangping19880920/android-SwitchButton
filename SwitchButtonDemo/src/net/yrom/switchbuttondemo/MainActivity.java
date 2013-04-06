
package net.yrom.switchbuttondemo;

import net.yrom.widget.SwitchButton;
import net.yrom.widget.SwitchButton.OnSwitchStateChangedListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButton sb = (SwitchButton) findViewById(R.id.sb);
        final TextView tv = (TextView) findViewById(R.id.tv);
        boolean curState = sb.isSwitchOn();
        tv.setText(curState?"current switch on!":"current switch off!");
        sb.setOnSwitchStateChangedListener(new OnSwitchStateChangedListener() {
            
            @Override
            public void onSwitchStateChanged(boolean state) {
                tv.setText(state?"current switch on!":"current switch off!");
            }
        });
    }


}
