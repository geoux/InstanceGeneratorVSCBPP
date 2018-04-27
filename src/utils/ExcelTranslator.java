/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 *
 * @author g-ux
 */
public class ExcelTranslator {
    
    public static void Translate(String input, String output){
        try 
        {
            FileWriter fw = new FileWriter(output);// Objeto para que establece origen de los datos
            BufferedWriter bw = new BufferedWriter(fw);// buffer para el manejo de los datos
            PrintWriter title = new PrintWriter(bw);
            FileInputStream ficheroXlsx = new FileInputStream(new File(input));
            Workbook ficheroWb = new HSSFWorkbook(ficheroXlsx);
            Sheet sheet = ficheroWb.getSheetAt(0);
            //for the items
            Row row = sheet.getRow(3);
            int i = 1;
            title.print("data;");
            title.println();
            String itemsSet = "set J := ";
            int reqCapacity = 0;
            List<String> varItem = new ArrayList<>();
            while(row != null && row.getCell(2) != null){
                itemsSet = itemsSet+i+" ";
                int weight = (int)row.getCell(2).getNumericCellValue();
                reqCapacity += weight;
                String tmp = "";
                if(i == 1){
                    tmp = "param w:=     "+i+"   "+weight;
                }else{
                    tmp = "              "+i+"   "+weight;
                }
                varItem.add(tmp);
                i++;
                row = sheet.getRow(i+2);
            }
            varItem.add(";");
            itemsSet = itemsSet+";";
            
             //for the bins
            row = sheet.getRow(3);
            int j = 1;
            String binSet = "set I := ";
            List<String> binsCost = new ArrayList<>();
            List<String> binsCap = new ArrayList<>();
            while(row != null && row.getCell(0) != null){
                String tmpCost = "";
                String tmpCap = "";
                binSet = binSet+j+" ";
                if(j == 1){
                    tmpCost = "param f:=     "+j+"   "+ row.getCell(1).getNumericCellValue();
                    tmpCap = "param b:=     "+j+"   "+ row.getCell(0).getNumericCellValue();
                }else{
                    tmpCost = "              "+j+"   "+ row.getCell(1).getNumericCellValue();
                    tmpCap = "              "+j+"   "+ row.getCell(0).getNumericCellValue();
                }
                binsCost.add(tmpCost);
                binsCap.add(tmpCap);
                j++;
                row = sheet.getRow(j+2);
            }
            binsCap.add(";");
            binsCost.add(";");
            binSet = binSet+";";
            
            title.print(binSet);
            title.println();
            title.println();
            title.print(itemsSet);
            title.println();
            title.println();

            title.println();
            for (String aBinsCap : binsCap) {
                title.print(aBinsCap);
                title.println();
            }
            title.println();
            title.println();
            for (String aBinsCost : binsCost) {
                title.print(aBinsCost);
                title.println();
            }
            title.println();
            title.println();
            for (String aVarItem : varItem) {
                title.print(aVarItem);
                title.println();
            }
            title.print("end;");
            title.println();
            title.close();            
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }        
    }    
}
