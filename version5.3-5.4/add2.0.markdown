短信还原

先删除所有短信，在读取xml文件，写入短信数据库

在高级工具界面完成点击时间，开启还原线程。

SmsInfoService类添加解析xml的方法，返回短信列表

SmsInfoService.java


```
public List<SmsInfo> getSmsInfosFromXml() throws Exception {
  List<SmsInfo> infos = null;
  SmsInfo info = null ;
  // 获取解析器
  XmlPullParser parser = Xml. newPullParser();
  // 获取输入流
  File file = new File("data/data/" + context .getPackageName()
  + "/smsbackup.xml" );
// File file = new File(Environment.getExternalStorageDirectory(),
// "smsbackup.xml");
  FileInputStream is = new FileInputStream(file);

  parser.setInput(is, "UTF-8" );

  int eventType = parser.getEventType();

  //判断标签
  while (eventType != XmlPullParser. END_DOCUMENT) {

  switch (eventType) {
  case XmlPullParser. START_TAG:
  if ("smsInfos" .equals(parser.getName())) {
  infos = new ArrayList<SmsInfo>();
  } else if ("smsInfo" .equals(parser.getName())) {
  info = new SmsInfo();
  } else if ("address" .equals(parser.getName())) {
  info.setAddress(parser.nextText());
  } else if ("date" .equals(parser.getName())) {
  info.setDate(parser.nextText());
  } else if ("type" .equals(parser.getName())) {
  info.setType(parser.nextText());
  } else if ("body" .equals(parser.getName())) {
  info.setBody(parser.nextText());
  }
  break ;

  case XmlPullParser. END_TAG:
  if ("smsInfo" .equals(parser.getName())){
  infos.add(info);
  //info = null;
  }
  break ;
  }
  }

  return infos;

  }
```
点击事件监听：

```

public void onClick(View arg0) {
  T. makeTest(getApplicationContext(), "hi" );

  // 打开进度条对话框
  final ProgressDialog dialog = new ProgressDialog(
  HighToolActivity. this );
  // // 设置样式
  dialog.setProgressStyle(ProgressDialog. STYLE_HORIZONTAL );
  //
  // // 设置标题
  dialog.setTitle( "正在删除短信" );

  dialog.show();

  // 使用线程
  new Thread() {
  @Override
  public void run() {
  // TODO Auto-generated method stub
  try {

  Uri uri = Uri.parse( "content://sms");
  // //执行删除操作
  getContentResolver().delete(uri, null , null );

  dialog.setTitle( "正在还原短信" );

  // // 获取 xml中的短信
  infoService = new SmsInfoService(
  getApplicationContext());

                                    List<SmsInfo> infos = infoService
                                    .getSmsInfosFromXml();
  //List<SmsInfo> infos = infoService .getSmsInfos();

  dialog.setMax(infos.size());

  for (SmsInfo info : infos) {
  ContentValues values = new ContentValues();
  values.put( "address" , info.getAddress());
  values.put( "date" , info.getDate());
  values.put( "type" , info.getType());
  values.put( "body" , info.getBody());

  getContentResolver().insert(uri, values);

  // 进程休眠2秒
  SystemClock. sleep(2000);
  // 进度条加1
  dialog.incrementProgressBy(1);
  }

  dialog.dismiss();

  Looper. prepare();
  T. makeTest(getApplicationContext(), "还原成功");
  Looper. loop();

  } catch (Exception e) {
  Looper. prepare();
  T. makeTest(getApplicationContext(), "还原失败");
  Looper. loop();
  e.printStackTrace();
  dialog.dismiss();
  }

  }
  }.start();

  }
  });
```