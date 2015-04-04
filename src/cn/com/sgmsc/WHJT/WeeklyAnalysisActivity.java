package cn.com.sgmsc.WHJT;

import java.util.Calendar;

import cn.com.sgmsc.ZSWB.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class WeeklyAnalysisActivity extends Activity implements View.OnTouchListener,View.OnClickListener{
    private Button circleButton;
    private Spinner circleNumSpinner;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private int circleNumFlag = 0; //选中的线圈序号
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weeklyanalysis);
		
        //circleNumSpinner线圈单选框设置
        circleNumSpinner=(Spinner)findViewById(R.id.spinner_circle_num);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(
               	this,
               	R.array.circle_num_items,
               	android.R.layout.simple_spinner_item);
        //设置circleNumSpinner的下拉列表显示样式 
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        //将adapter添加到circleNumSpinner中 
        circleNumSpinner.setAdapter(adapter); 
        //设置circleNumSpinner的一些属性
        circleNumSpinner.setSelection(0, true);
        //添加circleNumSpinner事件监听 
        circleNumSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
            @Override 
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) { 
                //设置显示当前选择的项 
                arg0.setVisibility(View.VISIBLE); 
				Button bton;
				//将之前选中的线圈按钮设置为黄色
				switch(circleNumFlag)
				{
				 case 0:
				  bton = (Button)findViewById(R.id.button_41751051);
				  break;
				 case 1:
				  bton = (Button)findViewById(R.id.button_41751052);
				  break;
				 case 2:
				  bton = (Button)findViewById(R.id.button_41951011);
				  break;
				 case 3:
				  bton = (Button)findViewById(R.id.button_41951012);
				  break;
				 case 4:
				  bton = (Button)findViewById(R.id.button_43151051);
				  break;
				 case 5:
				  bton = (Button)findViewById(R.id.button_43151052);
				  break;
				 case 6:
				  bton = (Button)findViewById(R.id.button_41951031);
				  break;
				 case 7:
				  bton = (Button)findViewById(R.id.button_41951032);
				  break;
				 default:
				  bton = (Button)findViewById(R.id.button_41751051);
				  break;
				}
				bton.setBackgroundResource(R.drawable.button_yellow);
				//更新线圈号标志
				circleNumFlag = arg2;
				//将现在选中的线圈按钮设置为蓝色
				switch(arg2)
				{
				 case 0:
				  bton = (Button)findViewById(R.id.button_41751051);
				  break;
				 case 1:
				  bton = (Button)findViewById(R.id.button_41751052);
				  break;
				 case 2:
				  bton = (Button)findViewById(R.id.button_41951011);
				  break;
				 case 3:
				  bton = (Button)findViewById(R.id.button_41951012);
				  break;
				 case 4:
				  bton = (Button)findViewById(R.id.button_43151051);
				  break;
				 case 5:
				  bton = (Button)findViewById(R.id.button_43151052);
				  break;
				 case 6:
				  bton = (Button)findViewById(R.id.button_41951031);
				  break;
				 case 7:
				  bton = (Button)findViewById(R.id.button_41951032);
				  break;
				 default:
				  bton = (Button)findViewById(R.id.button_41751051);
				  break;
				}
			    bton.setBackgroundResource(R.drawable.button_blue);   
				} 
				
				@Override 
				public void onNothingSelected(AdapterView<?> arg0) { 
				} 
        });
        
        //设置时间选择器datePicker的监听器
        editTextStartTime = (EditText) this.findViewById(R.id.edittext_start_time);   
        editTextStartTime.setOnTouchListener(this);       
        editTextEndTime = (EditText) this.findViewById(R.id.edittext_end_time);   
        editTextEndTime.setOnTouchListener(this);    
		//设置线圈按钮的监听器
        circleButton = (Button)findViewById(R.id.button_41751051);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_41751052);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_41951011);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_41951012);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_43151051);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_43151052);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_41951031);
        circleButton.setOnClickListener(this);
        circleButton = (Button)findViewById(R.id.button_41951032);
        circleButton.setOnClickListener(this);
	}
	//重写时间选择器datepicker的监听器
	@Override
	public boolean onTouch(View v, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//设置弹出的时间选择器的布局
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.date_time_dialog,null);
			final DatePicker datePicker = (DatePicker)view.findViewById(R.id.date_picker);
			builder.setView(view);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
			//根据选中的是起始时间框还是结束时间框设置对应编辑框的内容
			if(v.getId() == R.id.edittext_start_time){
				final int inType = editTextStartTime.getInputType();
				editTextStartTime.setInputType(InputType.TYPE_NULL);
				editTextStartTime.onTouchEvent(event);
				editTextStartTime.setInputType(inType);
				editTextStartTime.setSelection(editTextStartTime.getText().length());
				
				builder.setTitle("选取起始时间");
				builder.setPositiveButton("确 定",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
                        StringBuffer sb = new StringBuffer(); 
                        sb.append(String.format("%d-%02d-%02d",  
                                datePicker.getYear(),  
                                datePicker.getMonth() + 1, 
                                datePicker.getDayOfMonth())); 
                        editTextStartTime.setText(sb); 
                        dialog.cancel(); 
					}
					
				});
				
			}else{
				final int inType = editTextEndTime.getInputType();
				editTextEndTime.setInputType(InputType.TYPE_NULL);
				editTextEndTime.onTouchEvent(event);
				editTextEndTime.setInputType(inType);
				editTextEndTime.setSelection(editTextEndTime.getText().length());
				
				builder.setTitle("选取时间");
				builder.setPositiveButton("确 定",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
                        StringBuffer sb = new StringBuffer(); 
                        sb.append(String.format("%d-%02d-%02d",  
                                datePicker.getYear(),  
                                datePicker.getMonth() + 1, 
                                datePicker.getDayOfMonth())); 
                        editTextEndTime.setText(sb); 
                        dialog.cancel(); 
					}
					
				});
				
			}
            Dialog dialog = builder.create(); 
            dialog.show();
		}
		return true;
	}
    //重写线圈按钮的监听器
	public void onClick(View v) {
		  // TODO Auto-generated method stub
		  Button bton;
		  //取消之前被选中的按钮，将其设置为黄色
          switch(circleNumFlag)
          {
          case 0:
        	  bton = (Button)findViewById(R.id.button_41751051);
        	  break;
          case 1:
        	  bton = (Button)findViewById(R.id.button_41751052);
        	  break;
          case 2:
        	  bton = (Button)findViewById(R.id.button_41951011);
        	  break;
          case 3:
        	  bton = (Button)findViewById(R.id.button_41951012);
        	  break;
          case 4:
        	  bton = (Button)findViewById(R.id.button_43151051);
        	  break;
          case 5:
        	  bton = (Button)findViewById(R.id.button_43151052);
        	  break;
          case 6:
        	  bton = (Button)findViewById(R.id.button_41951031);
        	  break;
          case 7:
        	  bton = (Button)findViewById(R.id.button_41951032);
        	  break;
          default:
        	  bton = (Button)findViewById(R.id.button_41751051);
        	  break;
          }
          bton.setBackgroundResource(R.drawable.button_yellow);
          //将目前选中的按钮设置为蓝色
          v.setBackgroundResource(R.drawable.button_blue);
          //根据选中按钮的id设置circleNumSpinner线圈号单选框
		  switch (v.getId()) {
		  case R.id.button_41751051:
			  circleNumSpinner.setSelection(0,true);
			  circleNumFlag = 0;
			  break;
		  case R.id.button_41751052:
			  circleNumSpinner.setSelection(1,true);
			  circleNumFlag = 1;
			  break;
		  case R.id.button_41951011:
			  circleNumSpinner.setSelection(2,true);
			  circleNumFlag = 2;
			  break;
		  case R.id.button_41951012:
			  circleNumSpinner.setSelection(3,true);
			  circleNumFlag = 3;
			  break;
		  case R.id.button_43151051:
			  circleNumSpinner.setSelection(4,true);
			  circleNumFlag = 4;
			  break;
		  case R.id.button_43151052:
			  circleNumSpinner.setSelection(5,true);
			  circleNumFlag = 5;
			  break;
		  case R.id.button_41951031:
			  circleNumSpinner.setSelection(6,true);
			  circleNumFlag = 6;
			  break;
		  case R.id.button_41951032:
			  circleNumSpinner.setSelection(7,true);
			  circleNumFlag = 7;
			  break;
		  default:
			  circleNumFlag = 0;
			  break;
		  }
		 }
}