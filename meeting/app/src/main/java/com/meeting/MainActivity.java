package com.meeting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;


public class MainActivity extends Activity {
    private RelativeLayout layout;
    private EditText ET_meetings;
    private EditText ET_date;
    private EditText ET_daytime;
    private Calendar calendar;//获得系统时间
    private Button sure;
    private String [] rooms;
    public Handler handle;
    private  boolean[] selected;
    private int roomselected;
    private  int daytimeselected;
    private String[] daytime = new String[]{"Morning", "Afternoon"};
    private String baseURL = "http://172.20.0.17:8080/meeting_server//servlet/meetingrooms";
    private HttpURLConnection connection;
    public String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initview();
        loadview();
        handle = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(getApplicationContext(), responseString.toString(),Toast.LENGTH_SHORT).show();
//                        showrooms();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void showrooms() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("请选择会议室！");
        alert.setSingleChoiceItems(rooms, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomselected = which;
                switch (which){
                    case  0:
                        Toast.makeText(getApplication(), "你选择了" + rooms[0], Toast.LENGTH_SHORT).show();
                        break;
                    case  1:
                        Toast.makeText(getApplication(), "你选择了" + rooms[1], Toast.LENGTH_SHORT).show();
                        break;
                    case  2:
                        Toast.makeText(getApplication(), "你选择了" + rooms[2], Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ET_meetings.setText(rooms[roomselected].toString().trim());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }


    private void initview() {
        ET_meetings = (EditText)findViewById(R.id.ED_meeting);
        ET_date = (EditText)findViewById(R.id.ET_date);
        ET_daytime = (EditText)findViewById(R.id.ET_daytime);
        sure = (Button)findViewById(R.id.sure);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //TODO
            //联网获取会议室的列表
            if (rooms == null)
            {
                try {
                    URL url = new URL(baseURL);
                    connection =(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    int responsecode = connection.getResponseCode();
                    if (responsecode == HttpURLConnection.HTTP_OK)
                    {
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] data2 = new byte[1024];
                        int length = 0;
                        while ((length = inputStream.read(data2)) != -1)
                        {
                            byteArrayOutputStream.write(data2, 0, length);
                        }
                        responseString =new String(byteArrayOutputStream.toByteArray());
                        Message message = new Message();
                        message.what = 1;
                        handle.sendMessage(message);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    };

    private void loadview() {


        ET_meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });



        ET_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                //设置日期简略显示 否则详细显示 包括:星期周
                datePicker.setCalendarViewShown(false);
                //初始化当前日期
                calendar = Calendar.getInstance();
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
                //设置date布局
                builder.setView(view);
                builder.setTitle("设置日期信息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth()));
                        ET_date.setText(sb);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
                builder.create().show();
            }
        });


        ET_daytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder datimeAlert = new AlertDialog.Builder(MainActivity.this);
                datimeAlert.setTitle("请选择时间");
                datimeAlert.setSingleChoiceItems(daytime, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                               daytimeselected = which;
                                break;
                            case  1:
                                daytimeselected = which;
                                break;
                            default:
                                break;

                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ET_daytime.setText(daytime[daytimeselected].toString().trim());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();


            }
        });


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
