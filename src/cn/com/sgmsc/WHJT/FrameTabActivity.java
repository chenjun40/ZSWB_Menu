package cn.com.sgmsc.WHJT;

import cn.com.sgmsc.ZSWB.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class FrameTabActivity extends TabActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置这个Activity没有标题栏
		final TabHost tabHost = getTabHost();
		Intent itNormal = new Intent(this,OccupancyRateActivity.class);
		Intent itContact = new Intent(this,WeeklyAnalysisActivity.class);
		Intent itDiary = new Intent(this,Blank1Activity.class);
		Intent itAlbum = new Intent(this,Blank2Activity.class);
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator(getResources().getText(R.string.tabtitle1),
						getResources().getDrawable(R.drawable.publish))
				.setContent(itNormal)
				);
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator(getResources().getText(R.string.tabtitle2), 
						getResources().getDrawable(R.drawable.analysis_tab))
				.setContent(itContact)
				);
		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator(getResources().getText(R.string.tabtitle3),
						getResources().getDrawable(R.drawable.diary))
				.setContent(itDiary)
				);	
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator(getResources().getText(R.string.tabtitle4), 
						getResources().getDrawable(R.drawable.album))
				.setContent(itAlbum)
				);
	}
}