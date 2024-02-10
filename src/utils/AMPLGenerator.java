package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AMPLGenerator {

    public static void Translate(String input, String output){
        try
        {
            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter title = new PrintWriter(bw);
            FileInputStream ficheroXlsx = new FileInputStream(new File(input));
            Workbook ficheroWb = new HSSFWorkbook(ficheroXlsx);
            Sheet sheet = ficheroWb.getSheetAt(0);
            //for the items
            Row row = sheet.getRow(1);
            /****************************************
             * Print model
             ***************************************/
            title = printModel(title);
            int i = 0;
            title.println("data;");
            title.println();
            String itemsSet = "set J := ";
            List<String> varItem = new ArrayList<>();
            while(row != null && row.getCell(2) != null){
                itemsSet = itemsSet+(i+1)+" ";
                String tmp = "";
                if(i == 0){
                    tmp = "param w:=     "+(i+1)+"   "+row.getCell(2).getNumericCellValue();
                }else{
                    tmp = "              "+(i+1)+"   "+row.getCell(2).getNumericCellValue();
                }
                varItem.add(tmp);
                i++;
                row = sheet.getRow(i+1);
            }
            varItem.add(";");
            itemsSet = itemsSet+";";

            //for the bins
            row = sheet.getRow(1);
            int j = 0;
            String binSet = "set I := ";
            List<String> binsCost = new ArrayList<>();
            List<String> binsCap = new ArrayList<>();
            while(row != null && row.getCell(0) != null){
                String tmpCost;
                String tmpCap;
                binSet = binSet+(j+1)+" ";
                if(j == 0){
                    tmpCost = "param f:=     "+(j+1)+"   "+ row.getCell(1).getNumericCellValue();
                    tmpCap = "param b:=     "+(j+1)+"   "+ row.getCell(0).getNumericCellValue();
                }else{
                    tmpCost = "              "+(j+1)+"   "+ row.getCell(1).getNumericCellValue();
                    tmpCap = "              "+(j+1)+"   "+ row.getCell(0).getNumericCellValue();
                }
                binsCost.add(tmpCost);
                binsCap.add(tmpCap);
                j++;
                row = sheet.getRow(j+1);
            }
            binsCap.add(";");
            binsCost.add(";");
            binSet = binSet+";";

            title.print(binSet);
            title.println();
            title.print(itemsSet);
            title.println();
            title.println();
            for (String aBinsCap : binsCap) {
                title.print(aBinsCap);
                title.println();
            }
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

    private static PrintWriter printModel(PrintWriter p)
    {
        p.println("set I;");
        p.println("/* Bins set*/");
        p.println();
        p.println("set J;");
        p.println("/* Items set*/");
        p.println();
        p.println("param b{i in I};");
        p.println("/* Bin capacity */");
        p.println();
        p.println("param f{i in I};");
        p.println("/* Bin cost */");
        p.println();
        p.println("param w{j in J};");
        p.println("/* Item weight */");
        p.println();
        p.println("var y{i in I}, binary;");
        p.println("/* Use bin i to pack n Items */");
        p.println();
        p.println("var x{i in I, j in J}, binary;");
        p.println("/* Pack Item j in Bin i */");
        p.println();
        p.println("minimize cost: sum{i in I}f[i]*y[i];");
        p.println("/*Total cost of used bins*/");
        p.println();
        p.println("s.t. assignment{j in J}: sum{i in I}x[i,j] = 1;");
        p.println("/* All items must be pack */");
        p.println();
        p.println("s.t. capacity{i in I}: sum{j in J}w[j]*x[i,j]<= b[i]*y[i];");
        p.println("/* Items weight must not exceed selected Bin capacity */");
        p.println();
        return p;
    }
}
