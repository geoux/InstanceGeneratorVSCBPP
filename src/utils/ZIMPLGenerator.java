package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ZIMPLGenerator {

    public static void CreateModel(String input, String output){
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
            int i = 0;
            title.println();
            List<String> varItem = new ArrayList<>();
            while(row != null && row.getCell(2) != null){
                String tmp;
                if(i == 0){
                    tmp = "param weigth[Items]:= <"+(i+1)+"> "+row.getCell(2).getNumericCellValue()+",";
                }else{
                    String lineEnding;
                    Row tmpRowItem = sheet.getRow(i + 2);
                    if(tmpRowItem != null && tmpRowItem.getCell(2) != null){
                        lineEnding = ",";
                    }else{
                        lineEnding = ";";
                    }
                    tmp = "                      <"+(i+1)+"> "+row.getCell(2).getNumericCellValue()+lineEnding;
                }
                varItem.add(tmp);
                i++;
                row = sheet.getRow(i+1);
            }

            //for the bins
            row = sheet.getRow(1);
            int j = 0;
            List<String> binsCost = new ArrayList<>();
            List<String> binsCap = new ArrayList<>();
            while(row != null && row.getCell(0) != null){
                String tmpCost;
                String tmpCap;
                if(j == 0){
                    tmpCost = "param cost[Bins]:= <"+(j+1)+"> "+ row.getCell(1).getNumericCellValue()+",";
                    tmpCap = "param capacity[Bins]:= <"+(j+1)+"> "+ row.getCell(0).getNumericCellValue()+",";
                }else{
                    String lineEnding;
                    Row tmpRowBin = sheet.getRow(j + 2);
                    if(tmpRowBin != null && tmpRowBin.getCell(0) != null){
                        lineEnding = ",";
                    }else{
                        lineEnding = ";";
                    }
                    tmpCost = "                   <"+(j+1)+"> "+ row.getCell(1).getNumericCellValue()+lineEnding;
                    tmpCap = "                       <"+(j+1)+"> "+ row.getCell(0).getNumericCellValue()+lineEnding;
                }
                binsCost.add(tmpCost);
                binsCap.add(tmpCap);
                j++;
                row = sheet.getRow(j+1);
            }

            title = printModelHeader(title,j,i);
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
            for (String aVarItem : varItem) {
                title.print(aVarItem);
                title.println();
            }
            title.println();
            printModelFooter(title);
            title.println();
            title.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private static PrintWriter printModelHeader(PrintWriter p, int binsAmount, int itemsAmount)
    {
        p.println("set Bins := { 1 .. "+binsAmount+" };");
        p.println("#Bins set");
        p.println();
        p.println("set Items := { 1 .. "+itemsAmount+" };");
        p.println("#Items set");
        p.println();
        p.println("set Assigment := Bins * Items;");
        p.println();
        return p;
    }

    private static void printModelFooter(PrintWriter p)
    {
        p.println("var y[Bins] binary;");
        p.println("#Is the bin used?");
        p.println();
        p.println("var x[Assigment] binary; ");
        p.println("#Is the item packed in bin?");
        p.println();
        p.println("minimize cost: sum <i> in Bins : cost[i]*y[i];");
        p.println("#Minimize total cost of used bins");
        p.println();
        p.println("#Each item j is packed in exactly one bin");
        p.println("subto assign :");
        p.println("       forall <j> in Items do");
        p.println("              sum <i> in Bins : x[i,j] == 1;");
        p.println();
        p.println("#Every bin should pack no more items than its capacity if it's used");
        p.println("subto limit :");
        p.println("       forall <i> in Bins do ");
        p.println("              sum <j> in Items : weigth[j] * x[i,j]  <= capacity[i] * y[i];");
    }
}
