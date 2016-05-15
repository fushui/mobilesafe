package com.fushui.safe.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.fushui.safe.db.BlackNumberDBHelper;
import com.fushui.safe.db.BlackNumberDao;
import com.fushui.safe.domain.ContactInfo;
import com.fushui.safe.domain.SmsInfo;
import com.fushui.safe.engine.ContactInfoService;
import com.fushui.safe.engine.SmsInfoService;

public class ContactInfoServiceTest extends AndroidTestCase {

	
	public void testGetSms() throws Exception {
		SmsInfoService service = new SmsInfoService(getContext());
		List<SmsInfo> infos = service.getSmsInfos();
		service.createXml(infos);
			 
	}
	public void testGetContacts() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo> contacts = service.getContactInfo();
//		for(ContactInfo info:contacts){
//			Log.i("i", info.toString());
//		}
			 
	}
	public void testAdd(){
	      BlackNumberDao dao = new BlackNumberDao(getContext());
	      dao.add("123456");
	      Log.i("i", "" + dao.isBlackNumber("123456"));
	}
	public void testAddAll(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		for(int i = 0; i < 10; i ++){
			   dao.add("000" + i );
			   Log.i("i", "000" + i);
		}
	      
	
	}
	public void testdelete(){
	      BlackNumberDao dao = new BlackNumberDao(getContext());
	      dao.delete("0001");
	      Log.i("i", "0001");
	    
	}
	public void testupdate(){
	      BlackNumberDao dao = new BlackNumberDao(getContext());
	      dao.update(1,"0008");
	      //Log.i("i", "" + dao.isBlackNumber("123456"));
	}
	public void testFindAll(){
		Log.i("i",  "hi");
	      BlackNumberDao dao = new BlackNumberDao(getContext());
	      for(String str:dao.findAll()){
	    	  Log.i("i",  "hi");
		      Log.i("i",  str);
	      }
	    	 
	}
}
