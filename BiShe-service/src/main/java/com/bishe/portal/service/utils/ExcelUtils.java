package com.bishe.portal.service.utils;

import com.bishe.portal.model.mo.TbUsers;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
public class ExcelUtils {
    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel

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
            tbUsers.setName((String)getCellValue(row.getCell(0)));
            String stringCellValue = (String)getCellValue(row.getCell(1));
            if ("女".equals(stringCellValue)) {
                tbUsers.setSex(0);
            }else{
                tbUsers.setSex(1);
            }
            tbUsers.setBirthDay((String)getCellValue(row.getCell(2)));
            tbUsers.setIdCard((String)getCellValue(row.getCell(3)));
            String stringCellValue1 = (String)getCellValue(row.getCell(4));
            if ("管理员".equals(stringCellValue1)){
                tbUsers.setPermission(1);
            }else {
                tbUsers.setPermission(0);
            }
            if("预备党员".equals(getCellValue(row.getCell(5)).toString())){
                tbUsers.setIdentity(0);
            }else{
                tbUsers.setIdentity(1);
            }
            tbUsers.setEmail((String)getCellValue(row.getCell(6)));
            tbUsers.setNationality(row.getCell(7).getStringCellValue());
            tbUsers.setBranch(row.getCell(8).getStringCellValue());
            tbUsers.setFixedTel((String)getCellValue(row.getCell(9)));
            tbUsers.setAddress(row.getCell(10).getStringCellValue());
            tbUsers.setJob(row.getCell(11).getStringCellValue());
            tbUsers.setJoinPartyDate(row.getCell(12).getStringCellValue());
            tbUsers.setTurnPositiveDate(row.getCell(13).getStringCellValue());
            tbUsers.setTel((String)getCellValue(row.getCell(14)));
            tbUsers.setPwd((String)getCellValue(row.getCell(15)));
            tbUsers.setAccount(UUIDUtils.getUUID(8));
            tbUsers.setPwd(Encryption.getPwd(tbUsers.getPwd(),tbUsers.getSale()));
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

    public static HSSFWorkbook outPutUserInfoExcel (List<TbUsers> tbUsers){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("用户信息");
        sheet.setDefaultColumnWidth(15);
        HSSFRow row1=sheet.createRow(0);
        row1.setHeightInPoints(50);
        int columnNum  = 16;
        HSSFCell cellTittle=row1.createCell(0);
        HSSFCellStyle columnTopStyle = getColumnTopStyle(wb);
        HSSFCellStyle style = getStyle(wb);
        cellTittle.setCellValue("用户信息一览表");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnNum-1));
        cellTittle.setCellStyle(columnTopStyle);
        HSSFRow row2=sheet.createRow(2);
        row2.setRowStyle(style);
        //创建单元格并设置单元格内容
        row2.createCell(0).setCellValue("编号");
        row2.createCell(1).setCellValue("姓名");
        row2.createCell(2).setCellValue("性别");
        row2.createCell(3).setCellValue("生日");
        row2.createCell(4).setCellValue("身份证");
        row2.createCell(5).setCellValue("权限");
        row2.createCell(6).setCellValue("身份");
        row2.createCell(7).setCellValue("邮箱");
        row2.createCell(8).setCellValue("民族");
        row2.createCell(9).setCellValue("所在党支部");
        row2.createCell(10).setCellValue("固定电话");
        row2.createCell(11).setCellValue("地址");
        row2.createCell(12).setCellValue("工作岗位");
        row2.createCell(13).setCellValue("入党日期");
        row2.createCell(14).setCellValue("转正日期");
        row2.createCell(15).setCellValue("电话");
        row2.setRowStyle(columnTopStyle);
        getUserInfoPassage(sheet,tbUsers,style);
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if(colNum == 0){
                sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
            }else{
                sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
        }
        return wb;
    }

    private static HSSFCellStyle getStyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = wb.createCellStyle();
        //设置底边框;
        style.setBorderBottom(ExtendedFormatRecord.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(ExtendedFormatRecord.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(ExtendedFormatRecord.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(ExtendedFormatRecord.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(ExtendedFormatRecord.CENTER_SELECTION);
        //设置垂直对齐的样式为居中对齐;
            style.setVerticalAlignment(ExtendedFormatRecord.VERTICAL_CENTER);

        return style;
    }

    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)11);
        //字体加粗
        font.setBoldweight((short)16);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = wb.createCellStyle();
        //设置底边框;
        style.setBorderBottom(ExtendedFormatRecord.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(ExtendedFormatRecord.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(ExtendedFormatRecord.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(ExtendedFormatRecord.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(ExtendedFormatRecord.CENTER_SELECTION);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(ExtendedFormatRecord.VERTICAL_CENTER);

        return style;
    }

    private static void getUserInfoPassage(HSSFSheet sheet, List<TbUsers> tbUsersList,HSSFCellStyle style){
        int i = 3;
        for (TbUsers tbUsers:tbUsersList){
            HSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(tbUsers.getAccount());
            row.createCell(1).setCellValue(tbUsers.getName());
            String sex = tbUsers.getSex()==1?"男":"女";
            row.createCell(2).setCellValue(sex);
            row.createCell(3).setCellValue(tbUsers.getBirthDay());
            row.createCell(4).setCellValue(tbUsers.getIdCard());
            String permission = tbUsers.getPermission() == 1?"管理员":"普通用户";
            row.createCell(5).setCellValue(permission);
            String identity = tbUsers.getIdentity()==1?"正式党员":"预备党员";
            row.createCell(6).setCellValue(identity);
            row.createCell(7).setCellValue(tbUsers.getEmail());
            row.createCell(8).setCellValue(tbUsers.getNationality());
            row.createCell(9).setCellValue(tbUsers.getBranch());
            row.createCell(10).setCellValue(tbUsers.getFixedTel());
            row.createCell(11).setCellValue(tbUsers.getAddress());
            row.createCell(12).setCellValue(tbUsers.getJob());
            row.createCell(13).setCellValue(tbUsers.getJoinPartyDate());
            row.createCell(14).setCellValue(tbUsers.getTurnPositiveDate());
            row.createCell(15).setCellValue(tbUsers.getTel());
            row.setRowStyle(style);
            i++;
        }
    }
    public static  Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化字符类型的数字
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }
    public static  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
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

