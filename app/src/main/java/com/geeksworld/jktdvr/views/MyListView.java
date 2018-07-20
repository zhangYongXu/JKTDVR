package com.geeksworld.jktdvr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**ListView显示不完整 处理
 * 用处:比如在ListView里面嵌套ListView
 * @author a
 *com.tzl.lunengtaishan.view.MyListView
 */
public class MyListView extends ListView {  

	public MyListView(Context context) {  
		super(context);  
	}  

	public MyListView(Context context, AttributeSet attrs) {  
		super(context, attrs);  
	}  

	public MyListView(Context context, AttributeSet attrs, int defStyle) {  
		super(context, attrs, defStyle);  
	}  

	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}  
} 