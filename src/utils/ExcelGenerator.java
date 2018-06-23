/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author g-ux
 */
public class ExcelGenerator {

    public static void writeInExcel(ArrayList<Double> capacity, ArrayList<Double> cost, ArrayList<Double> items, String baseExcel){
        try{
            FileOutputStream fileout = new FileOutputStream(new File(baseExcel+".xls"));
            Workbook ficheroWb = new HSSFWorkbook();
            Sheet sheet = ficheroWb.createSheet("VCSBPP");

            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Bin Size");
            row.createCell(1).setCellValue("Bin Cost");
            row.createCell(2).setCellValue("Item Weight");
            int maxIterations;
            if(capacity.size() > items.size()){
                maxIterations = capacity.size();
            }else {
                maxIterations = items.size();
            }

            for (int x = 0; x < maxIterations; x++){
                row = sheet.createRow(x+1);
                // Writing Bins
                if (x < capacity.size()){
                    row.createCell(0).setCellValue(capacity.get(x));
                    row.createCell(1).setCellValue(cost.get(x));
                }
                // Writing items
                if (x < items.size()){
                    row.createCell(2).setCellValue(items.get(x));
                }
            }
            ficheroWb.write(fileout);
            fileout.flush();
        }catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
