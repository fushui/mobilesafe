短信记录的备份

高级工具添加事件监听，实现点击textview启动服务：

新建实体类SmsInfo，存放短信的地址，日期，类型，内容
```
SmsInfo.java:

public class SmsInfo {


  private String address;
  private String date;
  private String type;
  private String body;
  public SmsInfo() {
  // TODO Auto-generated constructor stub
  }
  @Override
  public String toString() {
  return "SmsInfo [address=" + address + ", date=" + date + ", type="
  + type + ", body=" + body + "]" ;
  }
  public String getAddress() {
  return address ;
  }
  public void setAddress(String address) {
  this .address = address;
  }
  public String getDate() {
  return date ;
  }
  public void setDate(String date) {
  this .date = date;
  }
  public String getType() {
  return type ;
  }
  public void setType(String type) {
  this .type = type;
  }
  public String getBody() {
  return body ;
  }
  public void setBody(String body) {
  this .body = body;
  }

}

```

新建SmsInfoService类，用于获取短信记录，并记录写入xml文件中，从xml读取出短信记录

从系统短信数据库读取记录：
```
public List<SmsInfo> getSmsInfos() {

  // 访问系统提供者
  Uri uri = Uri. parse( "content://sms");

  Cursor c = context .getContentResolver().query(uri,
  new String[] { "address" , "date" , "type" , "body" }, null, null,
  null );
  SmsInfo info = new SmsInfo();

  while (c.moveToNext()) {
  String date = c.getString(c.getColumnIndex("date" ));
  String address = c.getString(c.getColumnIndex("address" ));
  String type = c.getString(c.getColumnIndex("type" ));
  String body = c.getString(c.getColumnIndex("body" ));

  info.setAddress(address);
  info.setBody(body);
  info.setType(type);
  info.setDate(date);


  infos.add(info);

  //info = null;
  }
  return infos ;
  }

// 把短信数据写入 xml中
  public void createXml(List<SmsInfo> infos) throws Exception {

  // 获取 xml解析器
  XmlSerializer serializer = Xml. newSerializer();

  // 获取输出流
  File file = new File("data/data/" + context .getPackageName()
  + "/smsbackup.xml" );

// File file = new File(Environment.getExternalStorageDirectory(),
// "smsbackup.xml");
  FileOutputStream os = new FileOutputStream(file);

  serializer.setOutput(os, "UTF-8" );
  serializer.startDocument( "UTF-8" , true );

  serializer.startTag( null , "smsInfos" );

  for (SmsInfo info : infos) {
  serializer.startTag( null , "smsInfo" );

  // address
  serializer.startTag( null , "address" );
  serializer.text(info.getAddress());
  serializer.endTag( null , "address" );

  // date
  serializer.startTag( null , "date" );
  serializer.text(info.getDate());
  serializer.endTag( null , "date" );

  // type

  serializer.startTag( null , "type" );
  serializer.text(info.getType());
  serializer.endTag( null , "type" );

  // body
  serializer.startTag( null , "body" );
  serializer.text(info.getBody());
  serializer.endTag( null , "body" );

  serializer.endTag( null , "smsInfo" );

  }

  serializer.endTag( null , "smsInfos" );

  serializer.endDocument();

  }
}
```




备份短信服务类BackupSmsService，完成备份后显示通知：



```
public class BackupSmsService extends Service {


  SmsInfoService infoService ;

  NotificationManager nm;
  public BackupSmsService() {
  // TODO Auto-generated constructor stub
  }


  @Override
  public void onCreate() {
  // TODO Auto-generated method stub
  super .onCreate();
  infoService = new SmsInfoService( this);

  //获取通知管理器
  nm = (NotificationManager)getSystemService(Context. NOTIFICATION_SERVICE);
  new Thread(){
  public void run() {
  //获取短信，然后生成 xml保存下来
  List<SmsInfo> infos = infoService .getSmsInfos();

  try {
  infoService .createXml(infos);


  //发送通知，告诉用户备份完成
  Notification notification = new Notification(R.drawable. ic_launcher , "短信备份完成" , System.currentTimeMillis()) ;

  //启动主界面
  Intent intent = new Intent(getApplicationContext(), MainActivity. class);
  PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, 0);

  notification. setLatestEventInfo(getApplicationContext(), "提示信息" , "短信备份完成" , contentIntent);

  //设置消息消失
  notification. flags = Notification.FLAG_AUTO_CANCEL ;

  //显示通知
  nm.notify(100,notification);

  } catch (Exception e) {
  // TODO Auto-generated catch block
  e.printStackTrace();

  //相当于handler,looper是一个消息泵，从消息队列(MessageQueue)获取消息,把消息交给handle
  Looper. prepare();
  T. makeTest(getApplicationContext(), "短信备份失败" );
  Looper. loop();


  }

  //停止服务
  stopSelf();

  };
  }.start();
  }
  @Override
  public IBinder onBind(Intent arg0) {
  // TODO Auto-generated method stub
  return null ;
  }

}

权限添加：

< uses-permission android:name ="android.permission.READ_SMS" />
  <uses-permission android:name ="android.permission.WRITE_SMS" />

  < service android:name = ".service.BackupSmsService" ></service >

```


