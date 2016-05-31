package com.ionesmile.variousdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CodeLayoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_code_layout);
		
		
		LinearLayout tabsContainer = new LinearLayout(this);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        setContentView(tabsContainer);
        
        tabsContainer.removeAllViews();

        String[] arrs = new String[]{"AAAAAAAAAAAA", "BBB", "CCCCCC"};
        int[] colors = new int[]{0xFFff0000, 0xFF00ff00, 0xFF0000ff};
        
        for (int i = 0; i < 3; i++) {

        	TextView tab = new TextView(this);
            tab.setText(arrs[i]);
            tab.setBackgroundColor(colors[i]);
            tab.setGravity(Gravity.CENTER);
            tab.setSingleLine();
            
            final String msg = arrs[i];
            tab.setFocusable(true);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CodeLayoutActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });

            tab.setPadding(20, 0, 20, 0);
            tabsContainer.addView(tab, i, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
        }
        
        
        
        
	}

}
