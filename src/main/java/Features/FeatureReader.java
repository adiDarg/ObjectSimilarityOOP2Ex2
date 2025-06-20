package Features;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FeatureReader {
    private static final String COMMA_DELIMITER = ",";
    private String path;
    List<List<String>> records;
    public FeatureReader() {
        records = new ArrayList<>();
        path = "";
    }
    public void chooseFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setCurrentDirectory(new File(path));
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getAbsolutePath();
        }
    }
    public void read() throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception("No File Inputted");
        }
        if (FilenameUtils.getExtension(path).equals("xlsx")){
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            String csvFileName = "ConvertedCSVFiles/" + FilenameUtils.getBaseName(path) + ".csv";
            try (
                    BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)
            ) {
                for (Row row : sheet) {
                    String[] cells = new String[row.getLastCellNum()];
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        cells[i] = (cell == null) ? "" : getCellValue(cell);
                    }
                    csvPrinter.printRecord((Object[]) cells);
                }
            }
            workbook.close();
            path = csvFileName;
        }
        else if (!FilenameUtils.getExtension(path).equals("csv")){
            throw new Exception("File type not supported - Input CSV or XLSX");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<FeaturesObject> convertToFeatures() throws Exception {
        List<FeaturesObject> features = new ArrayList<>();
        FeaturesObject current = null;
        for (List<String> record : records) {
            //Ignore empty rows
            if (record.isEmpty()) {
                continue;
            }
            //New FeaturesObject being made
            if (record.size() == 1) {
                //Save previous FeaturesObject
                if (current != null) {
                    features.add(current);
                }
                current = new FeaturesObject(record.get(0));
                continue;
            }
            /*If row wasn't empty and had more than one entry -
             consider as attempt to enter attribute without declaring FeaturesObject*/
            if (current == null){
                throw new Exception("File must start with name of first object");
            }
            //Get name of Feature - *name means important feature, !name means less important feature
            String name = record.get(0);
            boolean isImportant = false;
            boolean isLessImportant = false;
            if (name.charAt(0) == '*') {
                isImportant = true;
                name = name.substring(1);
            }
            else if (name.charAt(0) == '!') {
                isLessImportant = true;
                name = name.substring(1);
            }

            //Try to extract value as double while considering number format exception
            double value = 0;
            try {
                value = Double.parseDouble(record.get(1));
            }
            catch (NumberFormatException e) {
                throw new Exception("Value of every attribute must be a number");
            }

            //Create the correct type of Feature
            if (isImportant) {
                current.addFeature(new ImportantFeature(name, value));
            }
            else if (isLessImportant) {
                current.addFeature(new LessImportantFeature(name, value));
            }
            else {
                current.addFeature(new Feature(name, value));
            }
        }
        features.add(current);
        return features;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                // Safely evaluate formulas
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    yield switch (evaluatedValue.getCellType()) {
                        case STRING -> evaluatedValue.getStringValue();
                        case NUMERIC -> String.valueOf(evaluatedValue.getNumberValue());
                        case BOOLEAN -> String.valueOf(evaluatedValue.getBooleanValue());
                        default -> "";
                    };
                } catch (Exception e) {
                    yield cell.getCellFormula(); // fallback
                }
            }
            case BLANK -> "";
            default -> "";
        };
    }

}
