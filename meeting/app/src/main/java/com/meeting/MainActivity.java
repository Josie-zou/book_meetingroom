package com.meeting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private String baseURL = "http://192.168.1.109:8080/meeting_server/servlet/meetingrooms";
    private  String  bookURL = "http://192.168.1.109:8080/meeting_server/servlet/book";
    private HttpURLConnection connection;
    public String responseString;
    public List<String> list;
    public String result;

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
                        rooms = new String[list.size()];
                        list.toArray(rooms);
//                        Toast.makeText(getApplicationContext(), list.toString(),Toast.LENGTH_SHORT).show();
                        showrooms();
                        break;
                    case  2:
                        Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        };
//        loadrooms();
    }

    private void loadrooms() {

        Thread thread = new Thread(runnable);
        thread.start();
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
                    connection.setConnectTimeout(3000);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    type=getrooms
                    StringBuffer stringbuffer = new StringBuffer();
                    stringbuffer.append("type").append("=").append(URLEncoder.encode("getrooms", "utf-8"));
                    byte[] data3 = stringbuffer.toString().getBytes();
                    OutputStream outputstream = connection.getOutputStream();
                    outputstream.write(data3);
                    outputstream.close();



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
                        list = new ArrayList<String>();
                        JSONArray jsonArray = new JSONObject(responseString).getJSONArray("rooms");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            list.add(jsonObject.getString("roomid"));
                        }
                        inputStream.close();

                        Message message = new Message();
                        message.what = 1;
                        handle.sendMessage(message);

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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





        final Runnable runnable1 = new Runnable() {
            @Override
            public void run() {

                try {
                    URL url1 = new URL(bookURL);
                    HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                    connection1.setRequestMethod("POST");
                    connection1.setDoOutput(true);
                    connection1.setDoInput(true);
                    connection1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    connection1.setRequestProperty("Content-Type", "text/html;charset=utf-8");
                    connection1.setConnectTimeout(3000);

                    String requeststring = getalldata().toString().trim();
                    byte[] data1 = requeststring.getBytes();
                    OutputStream outputStream = connection1.getOutputStream();
                    outputStream.write(data1);
                    outputStream.close();
                    int responsecode1 = connection1.getResponseCode();
                    if (responsecode1 == HttpURLConnection.HTTP_OK)
                    {
                        InputStream inputStream = connection1.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] data3 = new byte[1024];
                        int length = 0;
                        while ((length = inputStream.read(data3)) != -1)
                        {
                            byteArrayOutputStream.write(data3, 0, length);
                        }
                        String  response =new String(byteArrayOutputStream.toByteArray());
                        list = new ArrayList<String>();
                        JSONObject jsonObject = new JSONObject(response);
                        result = jsonObject.getString("result");
//                        for (int i = 0; i < jsonArray.length(); i++)
//                        {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            list.add(jsonObject.getString("roomid"));
//                        }
                        inputStream.close();
                        Message message = new Message();
                        message.what = 2;
                        handle.sendMessage(message);

                    }



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Thread thread = new Thread(runnable1);
              thread.start();

            }
        });
    }

    private String getalldata() {

        StringBuffer stringBuffer = new StringBuffer();
        try {
//            stringBuffer.append("typename=bookroom");
            stringBuffer.append("typename=").append(URLEncoder.encode("bookroom", "utf-8"));
            stringBuffer.append("&").append("room=").append(URLEncoder.encode(ET_meetings.getText().toString().trim(), "utf-8")).append("&");
            stringBuffer.append("datetime=").append(URLEncoder.encode(ET_date.getText().toString().trim(), "utf-8")).append("&");
            stringBuffer.append("daytime=").append(URLEncoder.encode(ET_daytime.getText().toString().trim(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("va",stringBuffer.toString());
        return  stringBuffer.toString();


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
