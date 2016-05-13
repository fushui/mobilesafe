
实现长按item弹出菜单，选择修改和删除黑名单功能，已经拦截黑名单短信

1.数据库业务逻辑代码中添加根据号码，查询id代码，以便实现修改
```

// 根据号码返回回id
  public int queryId(String number) {
  int _id = 0;
  SQLiteDatabase db = helper .getReadableDatabase();
  if (db.isOpen()) {
  Cursor c = db.query(Constant. DB_TABLE_NAME , new String[] { "_id" },
  "number = ?" , new String[] { number }, null , null , null);
  if (c.moveToFirst()) {
  _id = c.getInt(0);
  }

  c.close();
  }
    db.close();
   return _id;
  }
```

2.弹出菜单
```
// 给一个控件注册上下文菜单
  registerForContextMenu( lv_black_number_lists );

// 弹出菜单
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
  ContextMenuInfo menuInfo) {
  // TODO Auto-generated method stub
  super .onCreateContextMenu(menu, v, menuInfo);
  menu.add(0, MENU_UPDATE_ID , 0, "更新黑名单号码" );
  menu.add(0, MENU_DELETE_ID , 0, "删除黑名单号码" );

  }



//菜单事件监听
  @Override
  public boolean onContextItemSelected(MenuItem item) {
  // TODO Auto-generated method stub
  int id = item.getItemId();
  switch (id) {
  case MENU_UPDATE_ID :
  // 获得要更改的索引
  AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
  .getMenuInfo();
  int position = acmi.position ;
  updateItem(position);
  break ;

  case MENU_DELETE_ID :
  // 获得要更改的索引
  AdapterContextMenuInfo acmi1 = (AdapterContextMenuInfo) item
  .getMenuInfo();
  int position1 = acmi1.position ;
  deleteItem(position1);
  break ;
  }

  return super .onContextItemSelected(item );

  }

```
借用添加黑名单对话框的界面，通过设置相关组件的不可见和文本内容，来显示新的对话框，
省去重新编写xml代码的麻烦
3.更新黑名单的代码：
```
private void updateItem( int postion) {
  // TODO Auto-generated method stub
  AlertDialog.Builder builder = new AlertDialog.Builder(TAG );

  final int pos = dao .queryId((String)adapter .getItem(postion));
  // 对话框视图
  View view = LayoutInflater. from( TAG).inflate(
  R.layout. add_blacknumber_dialog , null );

  //修改标题
  TextView tv_title = (TextView)view.findViewById(R.id.id_tv_add_blacknumber );
  tv_title.setText( "更新黑名单号码" );

  builder.setView(view);



  final Dialog d = builder.create();
  d.show();

  Button bt_confirm = (Button) view
  .findViewById(R.id. id_bt_confirm_add_black );
  Button bt_cancel = (Button) view
  .findViewById(R.id. id_bt_cancel_add_black );


  final EditText et_number = (EditText) view
  .findViewById(R.id. id_et_add_black );
  bt_confirm.setOnClickListener( new View.OnClickListener() {

  @Override
  public void onClick(View arg0) {
  // TODO Auto-generated method stub
  // 获取输入的黑名单号码
  String input_number = et_number.getText().toString();
  if (!TextUtils.isEmpty(input_number)) {

  // 判断是否已存在该黑名单
  boolean is_exist = dao .isBlackNumber(input_number);
  if (is_exist) {
  T. makeTest( TAG, "输入号码已存在" );
  } else {

  T. makeTest( TAG, "修改成功");
  dao.update(pos, input_number);
  d.dismiss();

  // 刷新数据
  black_number_lists = dao .findAll();
  adapter.setBlack_lists( black_number_lists );
  adapter.notifyDataSetChanged();

  }
  } else {
  T. makeTest( TAG, "输入号码为空" );
  }

  }
  });

  bt_cancel.setOnClickListener( new View.OnClickListener() {

  @Override
  public void onClick(View arg0) {
  // TODO Auto-generated method stub
  d.dismiss();
  }
  });

  }
```

4.删除黑名单的代码
```
private void deleteItem( int position) {
  // TODO Auto-generated method stub
  AlertDialog.Builder builder = new AlertDialog.Builder(TAG );

  final int pos = position;
  // 对话框视图
  View view = LayoutInflater. from( TAG).inflate(
  R.layout. add_blacknumber_dialog , null );

  //修改标题
  TextView tv_title = (TextView)view.findViewById(R.id.id_tv_add_blacknumber );
  tv_title.setText( "删除黑名单号码" );

  builder.setView(view);

  final Dialog d = builder.create();
  d.show();

  Button bt_confirm = (Button) view
  .findViewById(R.id. id_bt_confirm_add_black );
  Button bt_cancel = (Button) view
  .findViewById(R.id. id_bt_cancel_add_black );
  final EditText et_number = (EditText) view
  .findViewById(R.id. id_et_add_black );

  //删除不需要用到编辑框，设置为不可见
  et_number.setVisibility(View. GONE );
  bt_confirm.setOnClickListener( new View.OnClickListener() {

  @Override
  public void onClick(View arg0) {
  // TODO Auto-generated method stub

  String number = (String) adapter .getItem(pos);
  T. makeTest( TAG, "删除成功");

  dao.delete(number);
  d.dismiss();

  // 刷新数据
  black_number_lists = dao .findAll();
  adapter.setBlack_lists( black_number_lists );
  adapter.notifyDataSetChanged();

  }
  });

  bt_cancel.setOnClickListener( new View.OnClickListener() {

  @Override
  public void onClick(View arg0) {
  // TODO Auto-generated method stub
  d.dismiss();
  }
  });
  }

```
5.黑名单短信的拦截：

SmsReceiver.java中的receice方法添加：

```
  BlackNumberDao dao;
  dao = new BlackNumberDao(context);

  //拦截接受的短信号码
  String number = smsMessage.getDisplayOriginatingAddress ();

//查询数据库，判断是否黑名单
  boolean is_black_number = dao .isBlackNumber(number);
  if (is_black_number){
  abortBroadcast();
  }
```