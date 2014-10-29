package com.winit.label.manager.impl.us.usps.intl;

import static org.junit.Assert.*;

import java.util.Map.Entry;
import java.util.Set;

import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.SecureEngine;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.winit.label.model.RequestMessage;
import com.winit.label.model.ResponseMessage;
import com.winit.test.http.BaseHttpTest;

public class UspsIntlLabelHandlerTest extends BaseHttpTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Test
	public void testHandleRequestMessageDeliveryWay() throws Exception
	{
		{
			
			 Set<Entry<Object, Object>> entrs = Ini.getProperties().entrySet();
			 for(Entry<Object,Object> e:entrs){
				 String st = e.getKey()+"="+ SecureEngine.decrypt(e.getValue());
				 System.out.println(st);
			 }
			
			RequestMessage requestMessage = getRequestMessage("1460709","USPS0004");
			
			ResponseMessage responseMessage = post(requestMessage);
			assertEquals(responseMessage.getStatusCode(), 0);
			System.out.println(responseMessage.getFilePath());
		}
		{
			RequestMessage requestMessage = getRequestMessage("1460712","USPS0005");
			
			ResponseMessage responseMessage = post(requestMessage);
			
			assertEquals(responseMessage.getStatusCode(), 0);
			System.out.println(responseMessage.getFilePath());
		}
//		Desktop.getDesktop().open(file);
	}

	@Test
	public void testHandleRequestMessageDeliveryWayBoolean()
	{
		fail("Not yet implemented");
	}

}
