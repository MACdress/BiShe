package com.bishe.portal.service.utils;

import com.bishe.portal.model.mo.TbUsers;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class ExcelUtils {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;
    //构造方法
    public ExcelUtils(){}
    //获取总行数
    public int getTotalRows()  { return totalRows;}
    //获取总列数
    public int getTotalCells() {  return totalCells;}
    //获取错误信息
    public String getErrorInfo() { return errorMsg; }

    /**
     * 读EXCEL文件，获取信息集合
     * @param mFile
     * @return
     */
    public List<TbUsers> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();//获取文件名
        System.out.println("文件名"+fileName);
        List<TbUsers> stuList = null;
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            stuList = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stuList;
    }
    /**
     * 根据excel里面的内容读取客户信息
     * @param is 输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<TbUsers> createExcel(InputStream is, boolean isExcel2003) {
        List<TbUsers> stuList = null;
        try{
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            stuList = readExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stuList;
    }
    /**
     * 读取Excel里面客户的信息
     * @param wb
     * @return
     */
    private List<TbUsers> readExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        System.out.println("gaolei dayin============" +sheet);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        System.out.println("行数======="+this.totalRows);
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            System.out.println("总列数=========="+this.totalCells);
        }
        List<TbUsers> stuList = new ArrayList<>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            TbUsers tbUsers = new TbUsers();
            tbUsers.setName(row.getCell(0).getStringCellValue());
            tbUsers.setPwd(row.getCell(1).getStringCellValue());
            tbUsers.setAddress(row.getCell(2).getStringCellValue());
            tbUsers.setIdCard(row.getCell(3).getStringCellValue());
            tbUsers.setBirthDay(row.getCell(4).getStringCellValue());
            tbUsers.setBranch(row.getCell(5).getStringCellValue());
            tbUsers.setIdentity(Integer.valueOf(row.getCell(6).getStringCellValue()));
            tbUsers.setJob(row.getCell(7).getStringCellValue());
            tbUsers.setEmail(row.getCell(8).getStringCellValue());
            tbUsers.setFixedTel(row.getCell(9).getStringCellValue());
            tbUsers.setJoinPartyDate(row.getCell(10).getStringCellValue());
            tbUsers.setNationality(row.getCell(11).getStringCellValue());
            String stringCellValue1 = row.getCell(12).getStringCellValue();
            if ("管理员".equals(stringCellValue1)){
                tbUsers.setPermission(1);
            }else {
                tbUsers.setPermission(0);
            }
            String stringCellValue = row.getCell(13).getStringCellValue();
            if ("女".equals(stringCellValue)) {
                tbUsers.setSex(0);
            }else{
                tbUsers.setSex(1);
            }
            tbUsers.setTel(row.getCell(14).getStringCellValue());
            tbUsers.setTurnPositiveDate(row.getCell(15).getStringCellValue());
            tbUsers.setAccount(UUIDUtils.getUUID(8));
            tbUsers.setSale(Encryption.getSale());
            stuList.add(tbUsers);
        }
        return stuList;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }
    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}

