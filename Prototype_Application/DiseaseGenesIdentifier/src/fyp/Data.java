package fyp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Data {
	
	private LinkedHashMap<String, Double> diseaseGenes;
	
	public Data(){
		diseaseGenes = new LinkedHashMap<>();
		readData();
		
	}
	
	public void setDiseaseGenes(String gene, double score){
		this.diseaseGenes.put(gene, score);
	}

	public LinkedHashMap<String, Double> getDiseaseGenes(){
		
		return diseaseGenes;
	}
	
	public void readData(){
		String filePath = "D:\\IIT\\l\\Final_Year_Project\\DNA_Inherit_Disease_Indentiyer\\data\\disgenet\\all_gene_disease_associations\\Filterd_Disease_Genes.xls";
		double score = 0.D;
		String gene = "";
		
		try {
            
            FileInputStream file = new FileInputStream(new File(filePath));
             
            //Get the workbook instance for XLS file 
            HSSFWorkbook workbook = new HSSFWorkbook(file);
 
            //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);
             
            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                 
                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                     
                    Cell cell = cellIterator.next();
                     
                    switch(cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            score = cell.getNumericCellValue();
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            gene = cell.getStringCellValue();
                            break;
                    }
                }
                setDiseaseGenes(gene, score);
                System.out.println("");
            }
            file.close();
            /*FileOutputStream out = 
                new FileOutputStream(new File("C:\\test.xls"));
            workbook.write(out);
            out.close();*/
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
