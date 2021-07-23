/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package edp.davinci.service.excel;

import com.alibaba.druid.util.StringUtils;
import edp.core.enums.SqlTypeEnum;
import edp.core.model.QueryColumn;
import edp.core.utils.CollectionUtils;
import edp.davinci.core.enums.NumericUnitEnum;
import edp.davinci.core.model.ExcelHeader;
import edp.davinci.core.model.FieldCurrency;
import edp.davinci.core.model.FieldNumeric;
import edp.davinci.core.utils.ExcelUtils;
import edp.davinci.dto.poiDto.PoiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.*;
import java.util.stream.Collectors;

import static edp.core.consts.Consts.EMPTY;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author daemon
 * @Date 19/5/28 18:24
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public abstract class AbstractSheetWriter {

    private CellStyle header;

    private CellStyle myDefault;

    private CellStyle general;

    private DataFormat format;

    private int nextRowNum = 0;

    //用于记录表头对应数据格式
    Map<String, CellStyle> headerFormatMap = new HashMap();
    //用于标记标记数字格式单位
    Map<String, NumericUnitEnum> dataUnitMap = new HashMap();
    //记录列最大字符数
    Map<String, Integer> columnWidthMap = new HashMap();


    protected void init(SheetContext context) throws Exception {
        format = context.getWorkbook().createDataFormat();
        //默认格式
        myDefault = context.getWorkbook().createCellStyle();
        //常规格式
        general = context.getWorkbook().createCellStyle();
        general.setDataFormat(format.getFormat("General"));
        //表头格式 粗体居中
        header = context.getWorkbook().createCellStyle();
        Font font = context.getWorkbook().createFont();
        font.setFontName("黑体");
        font.setBold(true);
        header.setFont(font);
        header.setDataFormat(format.getFormat("@"));
        header.setAlignment(HorizontalAlignment.CENTER);
        header.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    protected void writeHeader(SheetContext context) throws Exception {
        if (context.getIsTable() && !CollectionUtils.isEmpty(context.getExcelHeaders())) {
            int rownum = 0;
            int colnum = 0;
            Map<String, QueryColumn> columnMap = context.getQueryColumns().stream().collect(Collectors.toMap(x -> x.getName(), x -> x, (v1, v2) -> v1));
            List<QueryColumn> queryColumns = new ArrayList<>();
            for (ExcelHeader excelHeader : context.getExcelHeaders()) {
                //计算多级表头行
                if (excelHeader.getRow() + excelHeader.getRowspan() > rownum) {
                    rownum = excelHeader.getRow() + excelHeader.getRowspan();
                }
                //计算多级表头列
                if (excelHeader.getCol() + excelHeader.getColspan() > colnum) {
                    colnum = excelHeader.getCol() + excelHeader.getColspan();
                }
                if (columnMap.containsKey(excelHeader.getKey())) {
                    QueryColumn queryColumn = columnMap.get(excelHeader.getKey());
                    queryColumns.add(queryColumn);
                    queryColumn.setType(excelHeader.getType());
                    //设置列的最大长度
                    columnWidthMap.put(queryColumn.getName(), Math.max(queryColumn.getName().getBytes().length, queryColumn.getType().getBytes().length));
                }
                //获取对应数据格式
                if (null != excelHeader.getFormat()) {
                    Object o = excelHeader.getFormat();
                    //设置列货币数值的单位
                    if (o instanceof FieldNumeric || o instanceof FieldCurrency) {
                        FieldNumeric fieldNumeric = (FieldNumeric) o;
                        if (null != fieldNumeric.getUnit()) {
                            dataUnitMap.put(excelHeader.getKey(), fieldNumeric.getUnit());
                        }
                    }
                    //设置列数据格式
                    String dataFormat = ExcelUtils.getDataFormat(excelHeader.getFormat());
                    if (!StringUtils.isEmpty(dataFormat)) {
                        CellStyle dataStyle = context.getWorkbook().createCellStyle();
                        DataFormat xssfDataFormat = context.getWorkbook().createDataFormat();
                        dataStyle.setDataFormat(xssfDataFormat.getFormat(dataFormat));
                        headerFormatMap.put(excelHeader.getKey(), dataStyle);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(queryColumns)) {
                context.setQueryColumns(queryColumns);
            }
            //画出表头
            for (int i = 0; i < rownum; i++) {
                Row headerRow = context.getSheet().createRow(i);
                nextRowNum++;
                for (int j = 0; j < colnum; j++) {
                    headerRow.createCell(j);
                }
            }
            for (ExcelHeader excelHeader : context.getExcelHeaders()) {
                //合并单元格
                if (excelHeader.isMerged() && null != excelHeader.getRange() && excelHeader.getRange().length == 4) {
                    int[] range = excelHeader.getRange();
                    if (!(range[0] == range[1] && range[2] == range[3])) {
                        CellRangeAddress cellRangeAddress = new CellRangeAddress(range[0], range[1], range[2], range[3]);
                        context.getSheet().addMergedRegion(cellRangeAddress);
                    }
                }
                Cell cell = context.getSheet().getRow(excelHeader.getRow()).getCell(excelHeader.getCol());
                cell.setCellStyle(header);
                cell.setCellValue(StringUtils.isEmpty(excelHeader.getAlias()) ? excelHeader.getKey() : excelHeader.getAlias());
            }
        } else {
            Row row = context.getSheet().createRow(nextRowNum++);
            for (int i = 0; i < context.getQueryColumns().size(); i++) {
                QueryColumn queryColumn = context.getQueryColumns().get(i);
                columnWidthMap.put(queryColumn.getName(), Math.max(queryColumn.getName().getBytes().length, queryColumn.getType().getBytes().length));
                Cell cell = row.createCell(i);
                cell.setCellStyle(header);
                cell.setCellValue(queryColumn.getName());
            }
        }
        //添加数据类型行
        if (context.getContain()) {
            Row row = context.getSheet().createRow(nextRowNum++);
            for (int i = 0; i < context.getQueryColumns().size(); i++) {
                String type = context.getQueryColumns().get(i).getType();
                if (context.getIsTable()) {
                    type = SqlTypeEnum.VARCHAR.getName();
                }
                row.createCell(i).setCellValue(type);
            }
        }
    }

    protected void writeLine(SheetContext context, Map<String, Object> dataMap) {
        Row row = context.getSheet().createRow(nextRowNum++);
        for (int j = 0; j < context.getQueryColumns().size(); j++) {
            QueryColumn queryColumn = context.getQueryColumns().get(j);
            myDefault.setDataFormat(format.getFormat("@"));
            Object value = dataMap.get(queryColumn.getName());
            Cell cell = row.createCell(j);
            if (null != value) {
                if (value instanceof Number || queryColumn.getType().equals("value")) {

                    Double v = formatNumber(value, dataUnitMap.get(queryColumn.getName()));

                    if (v == null) {
                        cell.setCellValue(String.valueOf(value));
                    } else {
                        cell.setCellValue(v);
                    }

                    if (null != headerFormatMap && headerFormatMap.containsKey(queryColumn.getName())) {
                        cell.setCellStyle(headerFormatMap.get(queryColumn.getName()));
                    } else {
                        cell.setCellStyle(general);
                    }
                } else {
                    cell.setCellValue(String.valueOf(value));
                }

                if (columnWidthMap.containsKey(queryColumn.getName())) {
                    if (String.valueOf(value).getBytes().length > columnWidthMap.get(queryColumn.getName())) {
                        columnWidthMap.put(queryColumn.getName(), String.valueOf(value).getBytes().length);
                    }
                }
            } else {
                cell.setCellValue(EMPTY);
                cell.setCellStyle(myDefault);
            }
        }
    }

<<<<<<< HEAD
=======
    /**
     * @param queryColumns 标题集合 queryColumns的长度应该与list中的model的属性个数一致
     * @param maps 内容集合
     * @param mergeIndex 合并单元格的列
     */
    public Boolean createSheet(Sheet sheet,List<QueryColumn> queryColumns, Map<String, List<Map<String, Object>>> maps, int[] mergeIndex){
        if (queryColumns.size()==0){
            return false;
        }
        int n = 0;
        for(Map.Entry<String, List<Map<String, Object>>> entry : maps.entrySet()){
            /*初始化head，填值标题行（第一行）*/
            Row row0 = sheet.createRow(0);
            for(int i = 0; i<queryColumns.size(); i++){
                /*创建单元格，指定类型*/
                Cell cell_1 = row0.createCell(i, Cell.CELL_TYPE_STRING);
                cell_1.setCellValue(queryColumns.get(i).getName());
            }
            /*得到当前sheet下的数据集合*/
            List<Map<String, Object>> list = entry.getValue();
            /*遍历该数据集合*/
            List<PoiInfo> poiInfos = new ArrayList();
            if(true){
                Iterator iterator = list.iterator();
                int index = 1;
                while (iterator.hasNext()){
                    Row row = sheet.createRow(index);
                    /*取得当前这行的map，该map中以key，value的形式存着这一行值*/
                    Map<String, String> map = (Map<String, String>)iterator.next();
                    /*循环列数，给当前行塞值*/
                    for(int i = 0; i<queryColumns.size(); i++){
                        String old = "";
                        /*old存的是上一行统一位置的单元的值，第一行是最上一行了，所以从第二行开始记*/
                        if(index > 1){
                            old = poiInfos.get(i)==null?"": poiInfos.get(i).getContent();
                        }
                        /*循环需要合并的列*/
                        for(int j = 0; j < mergeIndex.length; j++){
                            if(index == 1){
                                /*记录第一行的开始行和开始列*/
                                PoiInfo poiInfo = new PoiInfo();
                                poiInfo.setOldContent(String.valueOf(map.get(queryColumns.get(i).getName())));
                                poiInfo.setContent(String.valueOf(map.get(queryColumns.get(i).getName())));
                                poiInfo.setRowIndex(1);
                                poiInfo.setCellIndex(i);
                                poiInfos.add(poiInfo);
                                break;
                            }else if(i > 0 && mergeIndex[j] == i){
                                /*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/
                                /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/
                                /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中
　　　　　　　　　　　　　　　　　　||poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))去掉就行*/
                                if(!poiInfos.get(i).getContent().equals(String.valueOf(map.get(queryColumns.get(i).getName()))) || poiInfos.get(i).getContent().equals(String.valueOf(map.get(queryColumns.get(i).getName())))
                                        && !poiInfos.get(i - 1).getOldContent().equals(String.valueOf(map.get(queryColumns.get(i-1).getName())))){
                                    /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                    CellRangeAddress cra=new CellRangeAddress(poiInfos.get(i).getRowIndex(), index - 1, poiInfos.get(i).getCellIndex(), poiInfos.get(i).getCellIndex());
                                    //在sheet里增加合并单元格
                                    sheet.addMergedRegion(cra);
                                    /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/
                                    poiInfos.get(i).setContent(String.valueOf(map.get(queryColumns.get(i).getName())));
                                    poiInfos.get(i).setRowIndex(index);
                                    poiInfos.get(i).setCellIndex(i);
                                }
                            }
                            /*处理第一列的情况*/
                            if(mergeIndex[j] == i && i == 0 && !poiInfos.get(i).getContent().equals(String.valueOf(map.get(queryColumns.get(i).getName())))){
                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                CellRangeAddress cra=new CellRangeAddress(poiInfos.get(i).getRowIndex(), index - 1, poiInfos.get(i).getCellIndex(), poiInfos.get(i).getCellIndex());
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                                /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                                poiInfos.get(i).setContent(String.valueOf(map.get(queryColumns.get(i).getName())));
                                poiInfos.get(i).setRowIndex(index);
                                poiInfos.get(i).setCellIndex(i);
                            }

                            /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容*/
                            if(mergeIndex[j] == i && index == list.size()){
                                CellRangeAddress cra=new CellRangeAddress(poiInfos.get(i).getRowIndex(), index, poiInfos.get(i).getCellIndex(), poiInfos.get(i).getCellIndex());
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                            }
                        }
                        Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(String.valueOf(map.get(queryColumns.get(i).getName())));
                        /*在每一个单元格处理完成后，把这个单元格内容设置为old内容*/
                        poiInfos.get(i).setOldContent(old);
                    }
                    index++;
                }
            }
            n++;
        }
        return true;
    }
    protected void writeBody(SheetContext context) {
    }

>>>>>>> 7958af50c93c4e3a7d841b0169fec6aba1af2411
    protected Boolean refreshHeightWidth(SheetContext context) {
        SXSSFSheet sheet = (SXSSFSheet)context.getSheet();
        sheet.setDefaultRowHeight((short) (20 * 20));
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < context.getQueryColumns().size(); i++) {
            context.getSheet().autoSizeColumn(i, true);
            QueryColumn queryColumn = context.getQueryColumns().get(i);
            if (columnWidthMap.containsKey(queryColumn.getName())) {
                int width = columnWidthMap.get(queryColumn.getName());
                if (width > 0) {
                    width = width > 255 ? 255 : width;
                    context.getSheet().setColumnWidth(i, width * 256);
                }
            } else {
                context.getSheet().setColumnWidth(i, context.getSheet().getColumnWidth(i) * 12 / 10);
            }
        }
        return true;
    }


    private Double formatNumber(Object value, NumericUnitEnum unitEnum) {
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {

        }
        return null;
    }
}
