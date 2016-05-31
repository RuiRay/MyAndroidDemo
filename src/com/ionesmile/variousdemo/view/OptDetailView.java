package com.ionesmile.variousdemo.view;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ionesmile.variousdemo.ColorSeekBarActivity;
import com.ionesmile.variousdemo.R;
import com.ionesmile.variousdemo.utils.MyBaseAdapter;

/**
 * @author iOnesmile
 */
public class OptDetailView extends FrameLayout {

	public static final String START_FEEDBACK_ACTIVITY_TAG = "startFeedbackActivityTAG";
	public static final String NUMBER_SYMBOL_STRING = "①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯";
	public static final char[] NUMBER_SYMBOLS = NUMBER_SYMBOL_STRING.toCharArray();
	private OptDetailAdapter mOptDetailAdapter;

	public OptDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.view_opt_detail, this);
		ListView listView = (ListView) findViewById(R.id.lv_optdetailview);
		mOptDetailAdapter = new OptDetailAdapter(context, null);
		listView.setAdapter(mOptDetailAdapter);
	}
	
	public void setData(String[] arrs) {
		List<String> list = Arrays.asList(arrs);
		mOptDetailAdapter.setDataAndNotify(list);
	}
	
	private class OptDetailAdapter extends MyBaseAdapter<String> {

		public OptDetailAdapter(Context context, List<String> data) {
			super(context, data);
		}
		
		@Override
		public int getCount() {
			int count = super.getCount();
			return count > NUMBER_SYMBOLS.length ? NUMBER_SYMBOLS.length : count;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View contentView = inflater.inflate(R.layout.item_view_opt_detail, null);
			TextView number = (TextView) contentView.findViewById(R.id.tv_number);
			TextView detail = (TextView) contentView.findViewById(R.id.tv_detail);
			number.setText(String.valueOf(NUMBER_SYMBOLS[position]));
			String text = getItem(position);
			// 如果包括超链接TAG时，创建超链接
			if (text.contains(START_FEEDBACK_ACTIVITY_TAG)) {
				createLink(text, detail);
			} else {
				detail.setText(text);
			}
			return contentView;
		}
	}
	
	// 创建“反馈帮助”的超链接
    public void createLink(String htmlLinkText, TextView textView) {  
    	textView.setClickable(false);  
        // 文字的样式（style）被覆盖，不能改变……  
    	textView.setText(Html.fromHtml(htmlLinkText));  
    	textView.setMovementMethod(LinkMovementMethod.getInstance());  
          
        CharSequence text = textView.getText();  
        if (text instanceof Spannable) {  
            int end = text.length();  
            Spannable sp = (Spannable) textView.getText();  
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);  
            SpannableStringBuilder style = new SpannableStringBuilder(text);  
            style.clearSpans();// should clear old spans  
            for (URLSpan url : urls) {  
                MyURLSpan myURLSpan = new MyURLSpan();  
                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);  
            }  
            textView.setText(style);  
        }
    }  
  
    private class MyURLSpan extends ClickableSpan {  
        @Override  
        public void onClick(View widget) {  
            Intent intent = new Intent(getContext(), ColorSeekBarActivity.class);  
            getContext().startActivity(intent);  
        }
    }  

}
