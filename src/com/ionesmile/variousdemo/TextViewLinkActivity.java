package com.ionesmile.variousdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ionesmile.variousdemo.view.OptDetailView;

public class TextViewLinkActivity extends Activity {
	
	private TextView tv;  
	private OptDetailView mOptDetailView;
	  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_text_view_link);
        
        initLink();  
        mOptDetailView = (OptDetailView) findViewById(R.id.optDetailView);
        String[] arrs = {
        	"若从灯灯泡颜色均跟随调节变化，分组成功，则点击“完成”；",	
        	"若未成功，可能是灯泡未全部点亮，请确保需分组的从灯已全部点亮，再点击重试；",	
        	"若三次尝试仍未成功，请前往“<a href=\""+OptDetailView.START_FEEDBACK_ACTIVITY_TAG+"\">反馈帮助</a>”进行解决。"
        };
        mOptDetailView.setData(arrs);
        String str = "<a href=\"startFeedbackActivityTAG\">反馈帮助</a>";
    }

	private void initLink() {
        tv = (TextView) findViewById(R.id.tv_link_test);
		tv.setClickable(false);  
  
        String htmlLinkText = "<a href=\"测试\">link</a> specified via an <a> tag.";   
        // 文字的样式（style）被覆盖，不能改变……  
        tv.setText(Html.fromHtml(htmlLinkText));  
        tv.setMovementMethod(LinkMovementMethod.getInstance());  
          
        CharSequence text = tv.getText();  
        if (text instanceof Spannable) {  
            int end = text.length();  
            Spannable sp = (Spannable) tv.getText();  
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);  
            SpannableStringBuilder style = new SpannableStringBuilder(text);  
            style.clearSpans();// should clear old spans  
            for (URLSpan url : urls) {  
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());  
                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);  
            }  
            tv.setText(style);  
        }
	}  
  
    private class MyURLSpan extends ClickableSpan {  
        private String mUrl;  
        MyURLSpan(String url) {  
            mUrl = url;  
        }  
  
        @Override  
        public void onClick(View widget) {  
            Toast.makeText(getApplicationContext(), mUrl, Toast.LENGTH_LONG).show();  
            widget.setBackgroundColor(Color.parseColor("#00000000"));  
  
            Intent intent = new Intent(getApplicationContext(), ColorSeekBarActivity.class);  
            startActivity(intent);  
        }  
    }  
}
