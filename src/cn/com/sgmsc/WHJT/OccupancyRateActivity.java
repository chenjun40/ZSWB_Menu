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
	String[] circleNumItems=null;  //线圈选项数组
	final int WRONG_DIALOG = 0; //错误对话框的id
	static int wrongNum = 0; //错误的类型
	final int LIST_DIALOG_MULTIPLE = 1;		//复选按钮对话框的id
	static boolean[] buttonPressedFlags=new boolean[]{false,false,false,false,false,false,false,false,false};  //复选框按钮选择情况
	static final int REQUEST_CODE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occupancyrate);			//设置当前屏幕
		
        circleNumItems = getResources().getStringArray(R.array.circle_num_items);	//获得XML文件中的字符串数组
        
        //线圈号多选框设置监听器
        editTextCircleNum = (EditText) this.findViewById(R.id.edittext_circle_num);
        editTextCircleNum.setOnTouchListener(new OnTouchListener() {
        	public boolean onTouch(View v, MotionEvent event) {
				showDialog(LIST_DIALOG_MULTIPLE);			//显示多选按钮对话框
				return false;
			}
		});

        //datePicker时间选择器
        editTextTime = (EditText) this.findViewById(R.id.edittext_time);
        editTextTime.setOnTouchListener(this);    
        
        //通过给背景甚至焦点来取消线圈复选框的焦点
        occupancyLayoutBackground = (LinearLayout)findViewById(R.id.occupancy_layout_background);
        occupancyLayoutBackground.setOnTouchListener(new OnTouchListener() { 
                 public boolean onTouch(View v, MotionEvent event) {
                	 occupancyLayoutBackground.setFocusable(true);
                	 occupancyLayoutBackground.setFocusableInTouchMode(true);
                	 occupancyLayoutBackground.requestFocus();
                     return false;
                 }
         });
        //给线圈按钮设置监听器
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
        //查询按钮设置监听器
        button = (Button)findViewById(R.id.button_search);
        button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				int i;
				checkBoxOccupancy = (CheckBox)findViewById(R.id.checkbox_occupancy_rate);
				checkBoxFlow = (CheckBox)findViewById(R.id.checkbox_flow);
				//根据用户的错误类型不同设置不同的错误号码，并输出
				//复选框单选，此时wrongNum不代表错误
				wrongNum = 0;
				if(checkBoxFlow.isChecked() && !checkBoxOccupancy.isChecked())
					wrongNum = 1;
				if(!checkBoxFlow.isChecked() && checkBoxOccupancy.isChecked())
					wrongNum = 2;
				if(checkBoxFlow.isChecked() && checkBoxOccupancy.isChecked())
					wrongNum = 3;
				//复选框为空
				if(!checkBoxFlow.isChecked() && !checkBoxOccupancy.isChecked())
					wrongNum = 4;
				//线圈未选择
				i = 0;
				while(!buttonPressedFlags[i] && i<8){
					i++;
				}
				if(i == 8)
					wrongNum = 5;
				//时间不合法
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

				//用户选择没问题，从服务器获得数据
				String circleNums = "";
				for(i = 0;i<buttonPressedFlags.length;i++){
					if(buttonPressedFlags[i])	//如果该选项被选中
						if(circleNums.isEmpty())
							circleNums = circleNums+circleNumItems[i];
						else
							circleNums = circleNums+"%20"+circleNumItems[i];
				}

		        String path ="http://192.168.1.153:8080/traffic/loop?function=2&id="+circleNums+"&date="+selectedTimeStr+"&fo="+wrongNum;
				//获得数据成功，唤醒另一个activity来显示数据
				Intent intent = new Intent(OccupancyRateActivity.this, OccupancyRatePictureShowActivity.class);
				intent.putExtra("PATH", path);
		        startActivity(intent);
			}
		});
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}
    
    //重写时间选择器的监听器
	@Override
	public boolean onTouch(View v, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//设置时间选择器datepicker的弹出对话框
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
				
				builder.setTitle("选取起始时间");
				builder.setPositiveButton("确 定",new DialogInterface.OnClickListener(){
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
    
	//重写线圈按钮监听器
	public void onClick(View v) {	  
      switch (v.getId()) {
	  case R.id.button_41751051:
		  if(buttonPressedFlags[0]){
			  buttonPressedFlags[0] = false;
			  v.setBackgroundResource(R.drawable.button_yellow);  //黄色按钮-未选中
			  }
		  else{
			  buttonPressedFlags[0] = true;
			  v.setBackgroundResource(R.drawable.button_blue);    //蓝色按钮-选中
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
	  //根据线圈按钮标志数组更新线圈复选框里显示的线圈号
	  String result = "您选择了：";
	  for(int i=0;i<buttonPressedFlags.length;i++){
		if(buttonPressedFlags[i])	//如果该选项被选中
				result = result+circleNumItems[i]+" ";
	  }
	  editTextCircleNum = (EditText) this.findViewById(R.id.edittext_circle_num);
	  editTextCircleNum.setText(result.substring(0,result.length()-1));//设置EditText显示的内容	  
	 }
	//重写onCreateDialog方法
     protected Dialog onCreateDialog(int id) {					
			Dialog dialog = null;									//声明一个Dialog对象用于返回
			Builder b = new AlertDialog.Builder(this);
			switch(id){		//对id进行判断
			case WRONG_DIALOG:
				b.setIcon(R.drawable.ic_header);		//设置对话框的图标
				b.setTitle("ERROR");			//设置对话框的标题
				switch(wrongNum)          //根据错误号码设置对话框的显示内容
				{ case 4:
					b.setMessage("未选择查询对象!");
					break;
				  case 5:
					b.setMessage("未选择线圈!");
					break;
				  case 6:
					b.setMessage("时间选择错误!");
					break;
				  default:
					b.setMessage("选择错误");	
					break;
				}
				b.setPositiveButton(				//添加按钮
					R.string.ok, 
					new OnClickListener() {			//为按钮添加监听器
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				dialog = b.create();		//生成Dialog对象
				break;
			case LIST_DIALOG_MULTIPLE:
				b.setIcon(R.drawable.ic_header);					//设置图标
				b.setTitle("请选择线圈");						//设置标题
				b.setMultiChoiceItems(							//设置复选选项
						R.array.circle_num_items,
						buttonPressedFlags,								//传入初始的选中状态
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which, boolean isChecked) {
								buttonPressedFlags[which] = isChecked;		//设置选中标志位
								//设置EditText显示的内容
								String result = "您选择了：";
								for(int i=0;i<buttonPressedFlags.length;i++){
									if(buttonPressedFlags[i]){		//如果该选项被选中
										result = result+circleNumItems[i]+" ";
									}
									
								}
								editTextCircleNum = (EditText)findViewById(R.id.edittext_circle_num);
								editTextCircleNum.setText(result.substring(0,result.length()-1));
								//更改被选中的线圈按钮的颜色
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
				b.setPositiveButton(					//添加确定按钮
						R.string.ok,
						new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								removeDialog(LIST_DIALOG_MULTIPLE);
							}
				});

				dialog = b.create();				//生成Dialog方法
				break;
		    default:
		        break;
			}
			return dialog;						//返回生成Dialog的对象
	}

    @Override
    public void onPrepareDialog(int id, Dialog dialog){//每次弹出对话框时更新对话框内容的方法
    	super.onPrepareDialog(id, dialog);
    }
}


