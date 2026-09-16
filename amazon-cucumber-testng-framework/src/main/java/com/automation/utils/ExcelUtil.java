package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ExcelUtil {

    private static final Object FILE_LOCK = new Object();

    private ExcelUtil() {
    }

    public static void setCellData(String filePath, String sheetName, int rowNum, int colNum, String value) {
        synchronized (FILE_LOCK) {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            Workbook workbook = null;
            try {
                if (!file.exists()) {
                    workbook = new XSSFWorkbook();
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        workbook = WorkbookFactory.create(fis);
                    }
                }

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sheetName);
                }

                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    row = sheet.createRow(rowNum);
                }

                Cell cell = row.getCell(colNum);
                if (cell == null) {
                    cell = row.createCell(colNum, CellType.STRING);
                }

                cell.setCellValue(value);
                sheet.autoSizeColumn(colNum);

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    fos.flush();
                }
                Log.info("Updated Excel [" + filePath + "] Sheet: " + sheetName + " Row: " + rowNum + " Col: " + colNum + " Value: " + value);
            } catch (IOException e) {
                Log.error("Failed to write to Excel file: " + filePath, e);
                throw new RuntimeException("Excel writing operation failed: " + e.getMessage(), e);
            } finally {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (IOException e) {
                        Log.error("Error closing workbook stream", e);
                    }
                }
            }
        }
    }

    public static String getCellData(String filePath, String sheetName, int rowNum, int colNum) {
        synchronized (FILE_LOCK) {
            File file = new File(filePath);
            if (!file.exists()) {
                Log.warn("Target Excel file does not exist: " + filePath);
                return "";
            }

            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    return "";
                }

                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    return "";
                }

                Cell cell = row.getCell(colNum);
                if (cell == null) {
                    return "";
                }

                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell).trim();
            } catch (IOException e) {
                Log.error("Failed to read from Excel file: " + filePath, e);
                throw new RuntimeException("Excel reading operation failed: " + e.getMessage(), e);
            }
        }
    }
}
