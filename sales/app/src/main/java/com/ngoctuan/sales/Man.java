package com.ngoctuan.sales;

import com.microsoft.schemas.office.visio.x2012.main.CellType;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Man {
    public static void main(String[] args) throws IOException {
        FileInputStream fis ;
        try {
            fis = new FileInputStream("C:\\Users\\DELL\\Downloads\\DATA BỔ SUNG 1.xlsx)");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//creating workbook instance that refers to .xls file
        HSSFWorkbook wb = new HSSFWorkbook(fis);
//creating a Sheet object to retrieve the object
        HSSFSheet sheet = wb.getSheetAt(0);
//evaluating cell type
        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        for (Row row : sheet)     //iteration over row using for each loop
        {
            for (Cell cell : row)    //iteration over cell using for each loop
            {
                switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                    case NUMERIC://field that represents numeric cell type
//getting the value of the cell as a number
                        System.out.print(cell.getNumericCellValue() + "\t\t");
                        break;
                    case STRING:    //field that represents string cell type
//getting the value of the cell as a string
                        System.out.print(cell.getStringCellValue() + "\t\t");
                        break;
                }
            }
            System.out.println();
        }
    }
}
