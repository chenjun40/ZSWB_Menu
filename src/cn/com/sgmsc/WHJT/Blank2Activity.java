package cn.com.sgmsc.WHJT;

import cn.com.sgmsc.ZSWB.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
/*
 * 该Activity的主要功能是显示好友的相册列表
 * 和MyAlbumActivity有所区别。
 */
public class Blank2Activity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank2);		//设置当前屏幕
	}
}