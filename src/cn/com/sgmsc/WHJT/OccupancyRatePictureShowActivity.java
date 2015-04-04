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
	// ������������صõ���ͼƬ
	Bitmap bitmap;
	String resultUrl = "";//��õ�����(ͼƬ��URL�ַ���) 
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == 0x123)
				// ʹ��ImageView��ʾ��ͼƬ
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
			        // Post ������ʹ�û���   
			        conn.setUseCaches(false);
			        InputStream inStream = conn.getInputStream();
		            InputStreamReader in = new InputStreamReader(inStream);  
		            BufferedReader buffere = new BufferedReader(in);//Ϊ�������BufferedReader  
		            String line = null;
		            while((line = buffere.readLine()) != null){//ʹ��whileѭ����ȡ�û�ȡ������  
		                resultUrl += line;//����Ҫ��ÿһ�еĺ������һ����б��������  
		            }
		            in.close();//�ر�InputStreamReader  
		            conn.disconnect();//�ر�http����  
		            //
		            //
					
					// ������Ϣ��֪ͨUI�����ʾ�ý��
					handler.sendEmptyMessage(0x124);
					// ����һ��URL����
					URL urlPicture = new URL(resultUrl);
					// �򿪸�URL��Ӧ����Դ��������
					InputStream is = urlPicture.openStream();
					// ��InputStream�н�����ͼƬ
					bitmap = BitmapFactory.decodeStream(is);
					// ������Ϣ��֪ͨUI�����ʾ��ͼƬ
					handler.sendEmptyMessage(0x123);
					is.close();
					// �ٴδ�URL��Ӧ����Դ��������
					is = urlPicture.openStream();
					// ���ֻ��ļ���Ӧ�������
					OutputStream os = openFileOutput("crazyit.png"
						, MODE_WORLD_READABLE);
					byte[] buff = new byte[1024];
					int hasRead = 0;
					// ��URL��Ӧ����Դ���ص�����
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