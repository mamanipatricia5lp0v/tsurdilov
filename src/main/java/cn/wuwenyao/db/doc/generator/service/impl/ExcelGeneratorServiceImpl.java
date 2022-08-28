package cn.wuwenyao.db.doc.generator.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;

import cn.wuwenyao.db.doc.generator.entity.TableFieldInfo;
import cn.wuwenyao.db.doc.generator.entity.TableInfo;
import cn.wuwenyao.db.doc.generator.entity.TableKeyInfo;

/***
 * 生成文档服务-excel实现
 * 
 * @author wwy
 *
 */
public final class ExcelGeneratorServiceImpl extends AbstractGeneratorServiceImpl {

	@Override
	public void generateDbDoc() throws Exception {
		String databaseName = dbInfoDao.databaseName();
		List<TableInfo> tableInfos = dbInfoDao.tableInfoList();
		// 生成文件
		File dir = new File(generatorConfig.getTargetFileDir());
		FileUtils.forceMkdir(dir);
		Random random = new Random();
		String filename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_hh-mm-ss") + random.nextInt(10) + ".xls";
		File file = new File(dir, filename);

		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建通用样式
		HSSFCellStyle simpleCellStyle = workbook.createCellStyle();
		HSSFFont simpleFont = workbook.createFont();
		simpleFont.setFontName("Constantia");
		simpleFont.setFontHeightInPoints((short) 16);
		simpleCellStyle.setFont(simpleFont);
		// 创建超链接样式
		HSSFCellStyle linkCellStyle = workbook.createCellStyle();
		HSSFFont linkFont = workbook.createFont();
		linkFont.setFontName("Constantia");
		linkFont.setFontHeightInPoints((short) 16);
		linkFont.setItalic(true);
		linkFont.setUnderline(Font.U_SINGLE);
		linkCellStyle.setFont(linkFont);

		CreationHelper createHelper = workbook.getCreationHelper();
		// 创建目录sheet
		String indexSheetName = "目录";
		HSSFSheet indexSheet = workbook.createSheet(indexSheetName);
		indexSheet.setActive(true);
		indexSheet.setDefaultColumnWidth(40);
		// 创建数据库名称row
		HSSFRow dbNameRow = indexSheet.createRow(0);
		createCell(1, dbNameRow, "数据库名称:" + databaseName, simpleCellStyle);

		HSSFRow tablesRow = indexSheet.createRow(2);
		createCell(0, tablesRow, "表", simpleCellStyle);
		// 创建返回目录sheet的超链接
		Hyperlink toIndexLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		toIndexLink.setAddress(String.format("'%s'!A1", indexSheetName));

		// 创建各种表格sheet
		for (int i = 0; i < tableInfos.size(); i++) {
			TableInfo tableInfo = tableInfos.get(i);

			// 目录sheet创建一个cell超链接到表格Sheet
			HSSFRow indexRow = indexSheet.createRow(i + 3);
			HSSFCell indexRowCell = createCell(0, indexRow, tableInfo.getTableName(), linkCellStyle);
			Hyperlink toTableLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
			toTableLink.setAddress(String.format("'%s'!A1", tableInfo.getTableName()));
			indexRowCell.setHyperlink(toTableLink);

			// 创建表格sheet
			HSSFSheet tableSheet = workbook.createSheet(tableInfo.getTableName());
			tableSheet.setDefaultColumnWidth(45);
			int rowIndex = 0;
			// 创建第一行，包含返回首页的按钮和表名
			HSSFRow tableSheetIndexRow = tableSheet.createRow(rowIndex++);
			HSSFCell tableSheetIndexRowFirstCell = createCell(0, tableSheetIndexRow, "返回目录", linkCellStyle);
			tableSheetIndexRowFirstCell.setHyperlink(toIndexLink);

			// 创建第二行，显示表名
			HSSFRow tableSheetTableNameRow = tableSheet.createRow(rowIndex++);
			createCell(0, tableSheetTableNameRow, "表名：" + tableInfo.getTableName(), simpleCellStyle);

			// 创建第三行，注释
			HSSFRow tableSheetTableCommentRow = tableSheet.createRow(rowIndex++);
			createCell(0, tableSheetTableCommentRow, "注释：" + tableInfo.getTableRemark(), simpleCellStyle);

			// 创建表格头部
			HSSFRow tableSheetHeadRow = tableSheet.createRow(rowIndex++);

			createCell(0, tableSheetHeadRow, "字段", simpleCellStyle);
			createCell(1, tableSheetHeadRow, "类型", simpleCellStyle);
			createCell(2, tableSheetHeadRow, "键", simpleCellStyle);
			createCell(3, tableSheetHeadRow, "能否为空", simpleCellStyle);
			createCell(4, tableSheetHeadRow, "默认值", simpleCellStyle);
			createCell(5, tableSheetHeadRow, "其他信息", simpleCellStyle);

			// 创建表格内容
			for (int j = 0; j < tableInfo.getFields().size(); j++) {

				TableFieldInfo tableFieldInfo = tableInfo.getFields().get(j);
				HSSFRow fieldRow = tableSheet.createRow(rowIndex++);
				createCell(0, fieldRow, tableFieldInfo.getField(), simpleCellStyle);
				createCell(1, fieldRow, tableFieldInfo.getType(), simpleCellStyle);
				createCell(2, fieldRow, tableFieldInfo.getKey(), simpleCellStyle);
				createCell(3, fieldRow, tableFieldInfo.getNullAble(), simpleCellStyle);
				createCell(4, fieldRow, tableFieldInfo.getDefaultValue(), simpleCellStyle);
				createCell(5, fieldRow, tableFieldInfo.getExtra(), simpleCellStyle);
			}

			// 空三行
			tableSheet.createRow(rowIndex++);
			tableSheet.createRow(rowIndex++);
			tableSheet.createRow(rowIndex++);
			//索引标题
			createCell(0, tableSheet.createRow(rowIndex++), "索引信息", simpleCellStyle);
			//索引头部
			HSSFRow indexSheetHeadRow = tableSheet.createRow(rowIndex++);
			createCell(0, indexSheetHeadRow, "名称", simpleCellStyle);
			createCell(1, indexSheetHeadRow, "栏位", simpleCellStyle);
			createCell(2, indexSheetHeadRow, "索引类型", simpleCellStyle);
			createCell(3, indexSheetHeadRow, "索引方式", simpleCellStyle);
			createCell(4, indexSheetHeadRow, "索引备注", simpleCellStyle);
			//创建索引内容
			for (int j = 0; j < tableInfo.getKeys().size(); j++) {
				TableKeyInfo tableKeyInfo = tableInfo.getKeys().get(j);
				HSSFRow keyRow = tableSheet.createRow(rowIndex++);
				createCell(0, keyRow, tableKeyInfo.getName(), simpleCellStyle);
				createCell(1, keyRow, tableKeyInfo.getColumnCombine(), simpleCellStyle);
				createCell(2, keyRow, tableKeyInfo.getUnique() ? "Unique" : "Normal", simpleCellStyle);
				createCell(3, keyRow, tableKeyInfo.getIndexType(), simpleCellStyle);
				createCell(4, keyRow, tableKeyInfo.getIndexComment(), simpleCellStyle);
			}
		}

		FileOutputStream exportXls = new FileOutputStream(file);
		workbook.write(exportXls);
		workbook.close();
		exportXls.close();

	}

	/***
	 * 创建单元格
	 * 
	 * @param indexColumn
	 *            第几个单元格
	 * @param row
	 *            HSSFRow
	 * @param value
	 *            值
	 * @param style
	 *            样式
	 * @return
	 */
	public HSSFCell createCell(int indexColumn, HSSFRow row, String value, HSSFCellStyle style) {
		HSSFCell cell = row.createCell(indexColumn, CellType.STRING);
		cell.setCellValue(value);
		cell.setCellStyle(style);
		return cell;
	}

}
