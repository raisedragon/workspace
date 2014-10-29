package com.winit.label.manager.impl.en.ups;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.compiere.model.MClassifyProduct;
import org.compiere.model.MOWMSExWareHouseProduct;
import org.compiere.model.MOWMSExWarehouse;
import org.compiere.model.MProduct;
import org.compiere.util.Env;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.winit.label.model.RequestMessage;
import com.winit.label.model.RequestMessage.ClassifyProduct;
import com.winit.label.model.RequestMessage.Consignee;
import com.winit.label.model.RequestMessage.Product;
import com.winit.label.model.ResponseMessage;
import com.winit.test.http.BaseHttpTest;


public class UpsLabelHandlerTest extends BaseHttpTest
{
	

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testHandle() throws Exception
	{
		RequestMessage requestMessage = getRequestMessage("1438741","UPSFS");
		
		ResponseMessage responseMessage = post(requestMessage);
		assertEquals(responseMessage.getStatusCode(), 0);
		System.out.println(responseMessage.getFilePath());
//		Desktop.getDesktop().open(file);
	}
	

}
