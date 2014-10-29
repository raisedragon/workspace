package com.raise.pio;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class WriteExcel
{
	
	public static void main(String[] args) throws IOException
	{
		WriteExcel excel = new WriteExcel();
		excel.write();
		
	}

	public void write() throws IOException
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		createHeader(sheet);
		
		FileOutputStream fos = new FileOutputStream("D:\\temp\\template.xls");
		wb.write(fos);
		// sheet.createRow(rownum);
		fos.close();

	}
	
	public void read() throws IOException
	{
		
	}

	protected HSSFSheet createHeader(HSSFSheet sheet)
	{
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0006= row0.createCell(6);
		HSSFRow row1 = sheet.createRow(1);
		HSSFCell cell0116= row1.createCell(16);
		HSSFRow row2 = sheet.createRow(2);
		HSSFCell cell0223= row2.createCell(23);
		HSSFRow row3 = sheet.createRow(3);
		
		HSSFCell cell0300= row3.createCell(0);
		HSSFCell cell0301= row3.createCell(1);
		HSSFCell cell0302= row3.createCell(2);
		HSSFCell cell0303= row3.createCell(3);
		HSSFCell cell0304= row3.createCell(4);
		HSSFCell cell0305= row3.createCell(5);
		HSSFCell cell0306= row3.createCell(6);
		HSSFCell cell0307= row3.createCell(7);
		HSSFCell cell0308= row3.createCell(8);
		HSSFCell cell0309= row3.createCell(9);
		HSSFCell cell0310= row3.createCell(10);
		HSSFCell cell0311= row3.createCell(11);
		HSSFCell cell0312= row3.createCell(12);
		HSSFCell cell0313= row3.createCell(13);
		HSSFCell cell0314= row3.createCell(14);
		HSSFCell cell0315= row3.createCell(15);
		HSSFCell cell0316= row3.createCell(16);
		HSSFCell cell0317= row3.createCell(17);
		HSSFCell cell0318= row3.createCell(18);
		HSSFCell cell0319= row3.createCell(19);
		HSSFCell cell0320= row3.createCell(20);
		HSSFCell cell0321= row3.createCell(21);
		HSSFCell cell0322= row3.createCell(22);
		HSSFCell cell0323= row3.createCell(23);
		HSSFCell cell0324= row3.createCell(24);
		
		
		
		cell0006.setCellValue("consignee");
		cell0116.setCellValue("products");
		cell0223.setCellValue("classifyProducts");
		
		cell0300.setCellValue("documentNo");
		cell0301.setCellValue("logisticsCode");
		cell0302.setCellValue("length");
		cell0303.setCellValue("width");
		cell0304.setCellValue("height");
		cell0305.setCellValue("weight");
		cell0306.setCellValue("name");
		cell0307.setCellValue("address1");
		cell0308.setCellValue("address2");
		cell0309.setCellValue("address3");
		cell0310.setCellValue("email");
		cell0311.setCellValue("phone");
		cell0312.setCellValue("countryCode");
		cell0313.setCellValue("state");
		cell0314.setCellValue("city");
		cell0315.setCellValue("postcode");
		cell0316.setCellValue("name");
		cell0317.setCellValue("length");
		cell0318.setCellValue("width");
		cell0319.setCellValue("height");
		cell0320.setCellValue("weight");
		cell0321.setCellValue("qty");
		cell0322.setCellValue("sku");
		cell0323.setCellValue("countryCode");
		cell0324.setCellValue("priceImports");
		
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 24));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 15));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 24));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 15));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 16, 22));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 23, 24));
		
		return sheet;
	}

}
