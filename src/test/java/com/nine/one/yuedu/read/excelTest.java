package com.nine.one.yuedu.read;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:李王柱
 * 2020/8/14
 */
public class excelTest {
    public static void main(String[] args) throws IOException, BiffException {
        //创建工作簿并载入excel文件流
        jxl.Workbook wb =null;
        InputStream is = new FileInputStream("D:\\JXTC\\test1.xls");
        wb = Workbook.getWorkbook(is);

        int sheetSize = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheet(0);
        int row_total = sheet.getRows();//获取总行数
        for (int j = 2; j <= 3; j++) {
                Cell[] cells = sheet.getRow(j);//获取这一行的数据

                System.out.println(cells[0].getContents());
                System.out.println(cells[1].getContents());
                System.out.println(cells[2].getContents());
                System.out.println(cells[3].getContents());
                //System.out.println(cells[4].getContents());
        }


    }
}
