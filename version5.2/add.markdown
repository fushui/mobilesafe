黑名单来电拦截（未测试）：

设置中心添加选择框，和逻辑代码：


settingcenter.xml:
```

< LinearLayout
  android:layout_width ="match_parent"
  android:layout_height ="wrap_content"
  android:orientation ="horizontal" >

  < TextView
  android:layout_width = "wrap_content"
  android:layout_height ="wrap_content"
  android:layout_weight ="1"
  android:text = "来电拦截服务"
  android:textSize ="18sp" />

  < CheckBox
  android:id ="@+id/id_cb_is_abort_number"
  android:layout_width ="wrap_content"
  android:layout_height ="wrap_content" />
  </LinearLayout >

```


SettingCenterActivity.java:


```
//是否开启黑名单来电拦截
  ck_abort_number .setOnClickListener( new View.OnClickListener() {

  // 判断 checkbox选中状态，并存放到sharePreferences中
  @Override
  public void onClick(View arg0) {
  // TODO Auto-generated method stub
  if (ck_abort_number .isChecked()) {
  ck_abort_number .setChecked( true);
  SavePreferences. save( TAG, Constant.IS_ABORT_NUMBER , true);
  } else {
  ck_abort_number .setChecked( false);
  SavePreferences. save( TAG, Constant.IS_ABORT_NUMBER , false);

  }
  }

  });
```
check()方法添加：


```
boolean is_abort_number = SavePreferences.getBoolean(getApplicationContext(), Constant.IS_ABORT_NUMBER );
  if (is_abort_number){
  ck_abort_number .setChecked( true);

  //开启拦截服务
  Intent intent = new Intent(this , BlackNumberService.class );
  startService(intent);
  } else {
  ck_abort_number .setChecked( false);
  }
```

拦截服务类BlackNumberService用于监听手机电话状态，判断是否黑名单，然后挂断电话。


```


public class BlackNumberService extends Service {

  private TelephonyManager tm;

  private MyPhoneStateListener listener;

  private BlackNumberDao dao;

  boolean is_open_abort_number ;

  @Override
  public void onCreate() {
  // TODO Auto-generated method stub
  super .onCreate();
  // 判断是否开启拦截服务
  is_open_abort_number = SavePreferences.getBoolean( this,
  Constant. IS_ABORT_NUMBER );


  dao = new BlackNumberDao( this);
  tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE );

  // 实例化监听器
  listener = new MyPhoneStateListener();

  // 设置监听事件
  tm.listen( listener, PhoneStateListener.LISTEN_CALL_STATE );



  }

  // 监听电话的呼叫状态
  private class MyPhoneStateListener extends PhoneStateListener {
  @Override
  public void onCallStateChanged( int state, String incomingNumber) {
  // TODO Auto-generated method stub
  super .onCallStateChanged(state, incomingNumber);


  switch (state) {
  case TelephonyManager. CALL_STATE_RINGING:
  // 正在接听

  if (is_open_abort_number ) {
  boolean is_black_number = dao .isBlackNumber(incomingNumber);
  if (is_black_number){
  endcall(incomingNumber);
  }
  }
  // 判断是否黑名单
  break ;

  case TelephonyManager. CALL_STATE_IDLE:
  break ;
  case TelephonyManager. CALL_STATE_OFFHOOK:
  break ;
  }
  }


  }
  private void endcall(String incomingNumber) {

  try {

  //通过反射技术
  Class clazz = Class.forName( "android.os.ServiceManager");
  Method method = clazz.getMethod( "getService", String. class );
  IBinder ibinder = (IBinder) method.invoke(null , Context. TELEPHONY_SERVICE);
  ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
  iTelephony.endCall();


  //相当于访问系统数据库
  //删除通话记录 通话记录的保存是一个异步的操作，需要使用ContentObserver技术来实现
  Uri uri = Calls. CONTENT_URI ;
  getContentResolver().registerContentObserver(uri, true , new MyContentObserver( new Handler(),incomingNumber));
//
  } catch (Exception e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
  }
  }


  private final class MyContentObserver extends ContentObserver{

  private String incomingNumber ;
  public MyContentObserver(Handler handler, String incomingNumber) {
  super (handler);
  // TODO Auto-generated constructor stub
  this .incomingNumber = incomingNumber;
  }

  @Override
  public void onChange( boolean selfChange) {
  // TODO Auto-generated method stub
  super .onChange(selfChange);
  Uri uri = Calls. CONTENT_URI ;
  String where = Calls. NUMBER + " = ?" ;
  String[] selectionArgs = new String[]{incomingNumber };
  getContentResolver().delete(uri, where, selectionArgs);

  //解除监听
  getContentResolver().unregisterContentObserver( this );
  }
  }
  // 用于挂断黑名单号码的电话
  public BlackNumberService() {
  // TODO Auto-generated constructor stub
  }

  @Override
  public IBinder onBind(Intent arg0) {
  // TODO Auto-generated method stub
  return null ;
  }

}
```

添加两个包：放置NeighboringCellInfo.aidl和ITelephony.aidl两个文件，用于反射技术获得系统服务

权限添加：
```
 <uses-permission android:name ="android.permission.CALL_PHONE" />
  <uses-permission android:name ="android.permission.WRITE_CONTACTS" />
  <uses-permission android:name ="android.permission.READ_PHONE_STATE" />


```
 