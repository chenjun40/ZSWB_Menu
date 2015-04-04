package cn.com.sgmsc.WHJT;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import cn.com.sgmsc.ZSWB.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;


public class OccupancyRateActivity extends Activity implements View.OnTouchListener,View.OnClickListener{

	private CheckBox checkBoxOccupancy;
	private CheckBox checkBoxFlow;
    private EditText editTextTime;
    private EditText editTextCircleNum;
    private LinearLayout occupancyLayoutBackground = null;
    private Button button = null;
	String[] circleNumItems=null;  //��Ȧѡ������
	final int WRONG_DIALOG = 0; //����Ի����id
	static int wrongNum = 0; //���������
	final int LIST_DIALOG_MULTIPLE = 1;		//��ѡ��ť�Ի����id
	static boolean[] buttonPressedFlags=new boolean[]{false,false,false,false,false,false,false,false,false};  //��ѡ��ťѡ�����
	static final int REQUEST_CODE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occupancyrate);			//���õ�ǰ��Ļ
		
        circleNumItems = getResources().getStringArray(R.array.circle_num_items);	//���XML�ļ��е��ַ�������
        
        //��Ȧ�Ŷ�ѡ�����ü�����
        editTextCircleNum = (EditText) this.findViewById(R.id.edittext_circle_num);
        editTextCircleNum.setOnTouchListener(new OnTouchListener() {
        	public boolean onTouch(View v, MotionEvent event) {
				showDialog(LIST_DIALOG_MULTIPLE);			//��ʾ��ѡ��ť�Ի���
				return false;
			}
		});

        //datePickerʱ��ѡ����
        editTextTime = (EditText) this.findViewById(R.id.edittext_time);
        editTextTime.setOnTouchListener(this);    
        
        //ͨ������������������ȡ����Ȧ��ѡ��Ľ���
        occupancyLayoutBackground = (LinearLayout)findViewById(R.id.occupancy_layout_background);
        occupancyLayoutBackground.setOnTouchListener(new OnTouchListener() { 
                 public boolean onTouch(View v, MotionEvent event) {
                	 occupancyLayoutBackground.setFocusable(true);
                	 occupancyLayoutBackground.setFocusableInTouchMode(true);
                	 occupancyLayoutBackground.requestFocus();
                     return false;
                 }
         });
        //����Ȧ��ť���ü�����
        button = (Button)findViewById(R.id.button_41751051);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_41751052);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_41951011);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_41951012);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_43151051);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_43151052);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_41951031);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.button_41951032);
        button.setOnClickListener(this);
        //��ѯ��ť���ü�����
        button = (Button)findViewById(R.id.button_search);
        button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				int i;
				checkBoxOccupancy = (CheckBox)findViewById(R.id.checkbox_occupancy_rate);
				checkBoxFlow = (CheckBox)findViewById(R.id.checkbox_flow);
				//�����û��Ĵ������Ͳ�ͬ���ò�ͬ�Ĵ�����룬�����
				//��ѡ��ѡ����ʱwrongNum���������
				wrongNum = 0;
				if(checkBoxFlow.isChecked() && !checkBoxOccupancy.isChecked())
					wrongNum = 1;
				if(!checkBoxFlow.isChecked() && checkBoxOccupancy.isChecked())
					wrongNum = 2;
				if(checkBoxFlow.isChecked() && checkBoxOccupancy.isChecked())
					wrongNum = 3;
				//��ѡ��Ϊ��
				if(!checkBoxFlow.isChecked() && !checkBoxOccupancy.isChecked())
					wrongNum = 4;
				//��Ȧδѡ��
				i = 0;
				while(!buttonPressedFlags[i] && i<8){
					i++;
				}
				if(i == 8)
					wrongNum = 5;
				//ʱ�䲻�Ϸ�
				editTextTime = (EditText)findViewById(R.id.edittext_time);
				String selectedTimeStr = "",currentTimeStr;
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				currentTimeStr = String.format("%d/%02d/%02d",
						cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH));
				selectedTimeStr = editTextTime.getText().toString();
				if(selectedTimeStr.compareTo("2014/11/10\0")<0 || selectedTimeStr.compareTo("2015/03/08\0")>0)
					wrongNum = 6;
					//int compareResult = selectedTimeStr.compareTo(currentTimeStr);
					//if(compareResult>0)
					

				if(wrongNum >= 4){
					showDialog(WRONG_DIALOG);
					return;
				}

				//�û�ѡ��û���⣬�ӷ������������
				String circleNums = "";
				for(i = 0;i<buttonPressedFlags.length;i++){
					if(buttonPressedFlags[i])	//�����ѡ�ѡ��
						if(circleNums.isEmpty())
							circleNums = circleNums+circleNumItems[i];
						else
							circleNums = circleNums+"%20"+circleNumItems[i];
				}

		        String path ="http://192.168.1.153:8080/traffic/loop?function=2&id="+circleNums+"&date="+selectedTimeStr+"&fo="+wrongNum;
				//������ݳɹ���������һ��activity����ʾ����
				Intent intent = new Intent(OccupancyRateActivity.this, OccupancyRatePictureShowActivity.class);
				intent.putExtra("PATH", path);
		        startActivity(intent);
			}
		});
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}
    
    //��дʱ��ѡ�����ļ�����
	@Override
	public boolean onTouch(View v, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//����ʱ��ѡ����datepicker�ĵ����Ի���
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.date_time_dialog,null);
			final DatePicker datePicker = (DatePicker)view.findViewById(R.id.date_picker);
			builder.setView(view);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
			
			if(v.getId() == R.id.edittext_time){
				editTextTime = (EditText) this.findViewById(R.id.edittext_time);
				final int inType = editTextTime.getInputType();
				editTextTime.setInputType(InputType.TYPE_NULL);
				editTextTime.onTouchEvent(event);
				editTextTime.setInputType(inType);
				editTextTime.setSelection(editTextTime.getText().length());
				
				builder.setTitle("ѡȡ��ʼʱ��");
				builder.setPositiveButton("ȷ ��",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
                        StringBuffer sb = new StringBuffer(); 
                        sb.append(String.format("%d/%02d/%02d",  
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth())); 
                        editTextTime.setText(sb);
                        dialog.cancel(); 
					}
					
				});
			}
               
            Dialog dialog = builder.create(); 
            dialog.show();
		}
		return true;
	}
    
	//��д��Ȧ��ť������
	public void onClick(View v) {	  
      switch (v.getId()) {
	  case R.id.button_41751051:
		  if(buttonPressedFlags[0]){
			  buttonPressedFlags[0] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);  //��ɫ��ť-δѡ��
			  }
		  else{
			  buttonPressedFlags[0] = true;
			  v.setBackgroundResource(R.drawable.button_blue);    //��ɫ��ť-ѡ��
			  }
		  break;
	  case R.id.button_41751052:
		  if(buttonPressedFlags[1]){
			  buttonPressedFlags[1] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[1] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  case R.id.button_41951011:
		  if(buttonPressedFlags[2]){
			  buttonPressedFlags[2] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[2] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  case R.id.button_41951012:
		  if(buttonPressedFlags[3]){
			  buttonPressedFlags[3] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[3] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  case R.id.button_43151051:
		  if(buttonPressedFlags[4]){
			  buttonPressedFlags[4] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[4] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  case R.id.button_43151052:
		  if(buttonPressedFlags[5]){
			  buttonPressedFlags[5] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[5] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  case R.id.button_41951031:
		  if(buttonPressedFlags[6]){
			  buttonPressedFlags[6] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[6] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  case R.id.button_41951032:
		  if(buttonPressedFlags[7]){
			  buttonPressedFlags[7] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);
			  }
		  else{
			  buttonPressedFlags[7] = true;
			  v.setBackgroundResource(R.drawable.button_blue);
			  }
		  break;
	  default:
	          break;
	  };
	  //������Ȧ��ť��־���������Ȧ��ѡ������ʾ����Ȧ��
	  String result = "��ѡ���ˣ�";
	  for(int i=0;i<buttonPressedFlags.length;i++){
		if(buttonPressedFlags[i])	//�����ѡ�ѡ��
				result = result+circleNumItems[i]+" ";
	  }
	  editTextCircleNum = (EditText) this.findViewById(R.id.edittext_circle_num);
	  editTextCircleNum.setText(result.substring(0,result.length()-1));//����EditText��ʾ������	  
	 }
	//��дonCreateDialog����
     protected Dialog onCreateDialog(int id) {					
			Dialog dialog = null;									//����һ��Dialog�������ڷ���
			Builder b = new AlertDialog.Builder(this);
			switch(id){		//��id�����ж�
			case WRONG_DIALOG:
				b.setIcon(R.drawable.ic_header);		//���öԻ����ͼ��
				b.setTitle("ERROR");			//���öԻ���ı���
				switch(wrongNum)          //���ݴ���������öԻ������ʾ����
				{ case 4:
					b.setMessage("δѡ���ѯ����!");
					break;
				  case 5:
					b.setMessage("δѡ����Ȧ!");
					break;
				  case 6:
					b.setMessage("ʱ��ѡ�����!");
					break;
				  default:
					b.setMessage("ѡ�����");	
					break;
				}
				b.setPositiveButton(				//��Ӱ�ť
					R.string.ok, 
					new OnClickListener() {			//Ϊ��ť��Ӽ�����
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				dialog = b.create();		//����Dialog����
				break;
			case LIST_DIALOG_MULTIPLE:
				b.setIcon(R.drawable.ic_header);					//����ͼ��
				b.setTitle("��ѡ����Ȧ");						//���ñ���
				b.setMultiChoiceItems(							//���ø�ѡѡ��
						R.array.circle_num_items,
						buttonPressedFlags,								//�����ʼ��ѡ��״̬
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which, boolean isChecked) {
								buttonPressedFlags[which] = isChecked;		//����ѡ�б�־λ
								//����EditText��ʾ������
								String result = "��ѡ���ˣ�";
								for(int i=0;i<buttonPressedFlags.length;i++){
									if(buttonPressedFlags[i]){		//�����ѡ�ѡ��
										result = result+circleNumItems[i]+" ";
									}
									
								}
								editTextCircleNum = (EditText)findViewById(R.id.edittext_circle_num);
								editTextCircleNum.setText(result.substring(0,result.length()-1));
								//���ı�ѡ�е���Ȧ��ť����ɫ
								switch(which)
								{   case 0:button =(Button)findViewById(R.id.button_41751051);break;
									case 1:button =(Button)findViewById(R.id.button_41751052);break;
									case 2:button =(Button)findViewById(R.id.button_41951011);break;
									case 3:button =(Button)findViewById(R.id.button_41951012);break;
									case 4:button =(Button)findViewById(R.id.button_43151051);break;
									case 5:button =(Button)findViewById(R.id.button_43151052);break;
									case 6:button =(Button)findViewById(R.id.button_41951031);break;
									case 7:button =(Button)findViewById(R.id.button_41951032);break;
									default:button =(Button)findViewById(R.id.button_41751051);break;
								}
							    if(isChecked == false)
							    	button.setBackgroundResource(R.drawable.button_yellow);
							    else
							    	button.setBackgroundResource(R.drawable.button_blue);
							}
						});
				b.setPositiveButton(					//���ȷ����ť
						R.string.ok,
						new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								removeDialog(LIST_DIALOG_MULTIPLE);
							}
				});

				dialog = b.create();				//����Dialog����
				break;
		    default:
		        break;
			}
			return dialog;						//��������Dialog�Ķ���
	}

    @Override
    public void onPrepareDialog(int id, Dialog dialog){//ÿ�ε����Ի���ʱ���¶Ի������ݵķ���
    	super.onPrepareDialog(id, dialog);
    }
}


