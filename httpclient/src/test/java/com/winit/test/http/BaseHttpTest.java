/*
 * HttpRequestProxy.java Created on November 3, 2008, 9:53 AM
 */

package com.winit.test.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.compiere.Adempiere;
import org.compiere.model.MClassifyProduct;
import org.compiere.model.MOWMSExWareHouseProduct;
import org.compiere.model.MOWMSExWarehouse;
import org.compiere.model.MProduct;
import org.compiere.util.Env;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.winit.label.model.RequestMessage;
import com.winit.label.model.ResponseMessage;
import com.winit.label.model.RequestMessage.ClassifyProduct;
import com.winit.label.model.RequestMessage.Consignee;
import com.winit.label.model.RequestMessage.Product;

public class BaseHttpTest
{
	@BeforeClass
	public static void setup(){
		System.setProperty("org.compiere.report.path","D:/work/OWNS_branches/1.2.12.0.OWMS_0901/java/erp/install/Adempiere/reports");
		System.setProperty("PropertyFile","D:/software/eclipse-jee-juno-SR1-win32-x86_64/workspace/httpclient/Adempiere.properties");
		Adempiere.startup(true);
	}
	
	private static boolean		alwaysClose		= false;
	// 返回数据编码格式
	private String				encoding		= "UTF-8";

	private final HttpClient	client			= new HttpClient(new SimpleHttpConnectionManager(alwaysClose));

	protected ResponseMessage post(RequestMessage requestMessage) throws Exception{
		System.out.println(JSONObject.toJSONString(requestMessage,true));
		String requestStr = JSONObject.toJSONString(requestMessage);
		String url = "http://localhost:8180/label/getLabel.do";
//		String url = "http://172.16.2.60:8080/lastmile_label/getLabel.do";
		PostMethod postRequest = new PostMethod(url.trim());
		StringRequestEntity requestEntity = new StringRequestEntity(requestStr, "application/json", "UTF-8");
		postRequest.setRequestEntity(requestEntity);
		try
		{
			String responseString = this.executeMethod(postRequest, encoding);
			ResponseMessage responseMessage = JSONObject.parseObject(responseString, ResponseMessage.class);
			
			System.out.println(JSONObject.toJSONString(responseMessage,true));
			return responseMessage;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			postRequest.releaseConnection();
		}
	}
	
	public HttpClient getHttpClient()
	{
		return client;
	}

	/**
	 * 用法： HttpRequestProxy hrp = new HttpRequestProxy();
	 * hrp.doRequest("http://www.163.com",null,null,"gbk");
	 * 
	 * @param url 请求的资源ＵＲＬ
	 * @param postData POST请求时form表单封装的数据 没有时传null
	 * @param header request请求时附带的头信息(header) 没有时传null
	 * @param encoding response返回的信息编码格式 没有时传null
	 * @return response返回的文本数据
	 * @throws Exception
	 */
	private String doRequest(String url, Map postData, Map header, String encoding) throws Exception
	{
		String responseString = null;
		// 头部请求信息
		Header[] headers = null;
		if (header != null)
		{
			Set entrySet = header.entrySet();
			int dataLength = entrySet.size();
			headers = new Header[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();)
			{
				Map.Entry entry = (Map.Entry) itor.next();
				headers[i++] = new Header(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		// post方式
		if (postData != null)
		{
			PostMethod postRequest = new PostMethod(url.trim());
			if (headers != null)
			{
				for (int i = 0; i < headers.length; i++)
				{
					postRequest.setRequestHeader(headers[i]);
				}
			}
			Set entrySet = postData.entrySet();
			int dataLength = entrySet.size();
			NameValuePair[] params = new NameValuePair[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();)
			{
				Map.Entry entry = (Map.Entry) itor.next();
				params[i++] = new NameValuePair(entry.getKey().toString(), entry.getValue().toString());
			}
			postRequest.setRequestBody(params);
			try
			{
				responseString = this.executeMethod(postRequest, encoding);
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				postRequest.releaseConnection();
			}
		}
		// get方式
		if (postData == null)
		{
			GetMethod getRequest = new GetMethod(url.trim());
			if (headers != null)
			{
				for (int i = 0; i < headers.length; i++)
				{
					getRequest.setRequestHeader(headers[i]);
				}
			}
			try
			{
				responseString = this.executeMethod(getRequest, encoding);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			finally
			{
				getRequest.releaseConnection();
			}
		}

		return responseString;
	}

	protected String executeMethod(HttpMethod request, String encoding) throws Exception
	{
		String responseContent = null;
		InputStream responseStream = null;
		BufferedReader rd = null;
		try
		{
			this.getHttpClient().executeMethod(request);
			if (encoding != null)
			{
				responseStream = request.getResponseBodyAsStream();
				rd = new BufferedReader(new InputStreamReader(responseStream, encoding));
				String tempLine = rd.readLine();
				StringBuffer tempStr = new StringBuffer();
				String crlf = System.getProperty("line.separator");
				while (tempLine != null)
				{
					tempStr.append(tempLine);
					tempStr.append(crlf);
					tempLine = rd.readLine();
				}
				responseContent = tempStr.toString();
			}
			else
				responseContent = request.getResponseBodyAsString();

			Header locationHeader = request.getResponseHeader("location");
			// 返回代码为302,301时，表示页面己经重定向，则重新请求location的url，这在
			// 一些登录授权取cookie时很重要
			if (locationHeader != null)
			{
				String redirectUrl = locationHeader.getValue();
				this.doRequest(redirectUrl, null, null, null);
			}
		}
		catch (HttpException e)
		{
			throw new Exception(e.getMessage());
		}
		catch (IOException e)
		{
			throw new Exception(e.getMessage());

		}
		finally
		{
			if (rd != null)
				try
				{
					rd.close();
				}
				catch (IOException e)
				{
					throw new Exception(e.getMessage());
				}
			if (responseStream != null)
				try
				{
					responseStream.close();
				}
				catch (IOException e)
				{
					throw new Exception(e.getMessage());

				}
		}
		return responseContent;
	}

	

	protected RequestMessage getRequestMessage(String documentNo,String logisticCode){
		
		MOWMSExWarehouse exWarehouse = MOWMSExWarehouse.getbyDocumentNO(Env.getCtx(), documentNo, null);
		Map<String,Object> parMap = exWarehouse.checkLogisticsType();
		
		RequestMessage requestMessage = new RequestMessage();
		requestMessage.setDocumentNo(documentNo);
		
		if(parMap.containsKey("lenght")){
			requestMessage.setLength((Double)parMap.get("lenght"));
			requestMessage.setWidth((Double)parMap.get("width"));
			requestMessage.setHeight((Double)parMap.get("height"));
		}
		requestMessage.setWeight(exWarehouse.getWeight().doubleValue());
		requestMessage.setRequiredNew(false);
		
		Consignee consignee = new Consignee();
		consignee.setName(exWarehouse.getName());
		consignee.setAddress1(exWarehouse.getAddress1());
		consignee.setAddress2(exWarehouse.getAddress2());
		consignee.setAddress3(exWarehouse.getAddress3());
		consignee.setCity(exWarehouse.getCity());
		consignee.setState(exWarehouse.getRegionName());
		consignee.setCountryCode(exWarehouse.getCountryName());
		consignee.setPostcode(exWarehouse.getPostal());
		consignee.setEmail(exWarehouse.getEMail());
		consignee.setPhone(exWarehouse.getPhone());
		requestMessage.setConsignee(consignee);
		
		
		List<MOWMSExWareHouseProduct> products = exWarehouse.getChildren(MOWMSExWareHouseProduct.Table_Name, "", "");
		for(MOWMSExWareHouseProduct exWareHouseProduct:products){
			MProduct mProduct = (MProduct) exWareHouseProduct.getM_Product();
			Product product = new Product();
			product.setName(mProduct.getEName());
			product.setLength(mProduct.getLength().doubleValue());
			product.setWidth(mProduct.getWidth().doubleValue());
			product.setHeight(mProduct.getHeight().doubleValue());
			product.setWidth(mProduct.getWeight().doubleValue());
			product.setQty(exWareHouseProduct.getQty().intValue());
			product.setSku(mProduct.getSKU());
			product.setWeight(mProduct.getWeight().doubleValue());
			MClassifyProduct[] classifyProducts =  mProduct.getClassifications(null, null);
			for(MClassifyProduct mClassifyProduct : classifyProducts){
				ClassifyProduct classifyProduct = new ClassifyProduct();
				classifyProduct.setCountryCode(mClassifyProduct.getC_Country().getCountryCode());
				classifyProduct.setPriceImports(classifyProduct.getPriceImports());
				product.getClassifyProducts().add(classifyProduct);
			}
			requestMessage.getProducts().add(product);
		}
		
		requestMessage.setLogisticsCode(logisticCode);
		
		return requestMessage;
	}


}
