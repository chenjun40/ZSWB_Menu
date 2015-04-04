package cn.com.sgmsc.WHJT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.com.sgmsc.ZSWB.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OccupancyRatePictureShowActivity extends Activity {

	ImageView show;
	TextView textViewPictureURL;
	// 代表从网络下载得到的图片
	Bitmap bitmap;
	String resultUrl = "";//获得的数据(图片的URL字符串) 
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == 0x123)
				// 使用ImageView显示该图片
				show.setImageBitmap(bitmap);
			else if(msg.what == 0x124)
				textViewPictureURL.setText(resultUrl);
		}
	};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occupancyratepictureshow);
        
        Intent intent=getIntent();
        final String path = intent.getStringExtra("PATH");
        TextView textViewGetURL = (TextView)findViewById(R.id.textview_get_url);
        textViewGetURL.setText(path);
		show = (ImageView) findViewById(R.id.show);
		textViewPictureURL = (TextView)findViewById(R.id.textview_picture_url);
		new Thread()
		{
			public void run()
			{
				try
				{
			         
		        	URL urlSever =new URL(path);
		        	HttpURLConnection conn = (HttpURLConnection)urlSever.openConnection();
			        conn.setConnectTimeout(5*10000);
			        conn.setRequestMethod("GET");
			        // Post 请求不能使用缓存   
			        conn.setUseCaches(false);
			        InputStream inStream = conn.getInputStream();
		            InputStreamReader in = new InputStreamReader(inStream);  
		            BufferedReader buffere = new BufferedReader(in);//为输出创建BufferedReader  
		            String line = null;
		            while((line = buffere.readLine()) != null){//使用while循环来取得获取的数据  
		                resultUrl += line;//我们要在每一行的后面加上一个反斜杠来换行  
		            }
		            in.close();//关闭InputStreamReader  
		            conn.disconnect();//关闭http连接  
		            //
		            //
					
					// 发送消息、通知UI组件显示该结果
					handler.sendEmptyMessage(0x124);
					// 定义一个URL对象
					URL urlPicture = new URL(resultUrl);
					// 打开该URL对应的资源的输入流
					InputStream is = urlPicture.openStream();
					// 从InputStream中解析出图片
					bitmap = BitmapFactory.decodeStream(is);
					// 发送消息、通知UI组件显示该图片
					handler.sendEmptyMessage(0x123);
					is.close();
					// 再次打开URL对应的资源的输入流
					is = urlPicture.openStream();
					// 打开手机文件对应的输出流
					OutputStream os = openFileOutput("crazyit.png"
						, MODE_WORLD_READABLE);
					byte[] buff = new byte[1024];
					int hasRead = 0;
					// 将URL对应的资源下载到本地
					while((hasRead = is.read(buff)) > 0)
					{
						os.write(buff, 0 , hasRead);
					}
					is.close();
					os.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
        
        
		Button button;
		button = (Button) findViewById(R.id.button_back);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				finish();
			}
		});
    }
}