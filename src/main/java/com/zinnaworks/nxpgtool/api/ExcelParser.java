package com.zinnaworks.nxpgtool.api;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zinnaworks.nxpgtool.dao.FieldDao;
import com.zinnaworks.nxpgtool.dao.JSONFieldDaoImpl;
import com.zinnaworks.nxpgtool.util.ExcelUtils;
import static com.zinnaworks.nxpgtool.util.CommonUtils.*;

//로직이 복잡함, simple하게 수정할 
public class ExcelParser {
	
	private static Map<String, Field.Type> types = new HashMap<>();
	static {
		types.put("string" , Field.Type.String);
		types.put("list" , Field.Type.List);
		types.put("integer" , Field.Type.Integer);
		types.put("number" , Field.Type.Number);
		types.put("numeric" , Field.Type.Number);
		types.put("map" , Field.Type.Map);
	}
	
	private FieldDao repository = new JSONFieldDaoImpl();
	Workbook wb;

	public ExcelParser(String fn) throws Exception {
		this.wb = this.load(fn);
	}
	
	private Workbook load(String fn) throws Exception {
		InputStream inputStream = new FileInputStream(fn);
		Workbook wb = new XSSFWorkbook(inputStream);
		return wb;
	}
	
	public Map<String, Map<String, Object>> parseAllSheet() throws Exception {
		Map<String, Map<String, Object>> map = new HashMap<>();
		for (int sheetIndex = 0; sheetIndex < this.wb.getNumberOfSheets(); sheetIndex++) {
			String sheetName = wb.getSheetName(sheetIndex);
			map.put(sheetName, parse(sheetName));
		}
		return map;
	}

	public Map<String, Object> parse(String sheetName) throws Exception {
		Sheet sheet = wb.getSheet(sheetName);
		Field root = Field.getRoot();
		repository.insert(sheet.getSheetName(), root);

		Row startRow = sheet.getRow(sheet.getFirstRowNum());
		ExcelApiRange excelApiRange = ExcelParser.range(sheet);
		parse2(sheet, startRow, root, excelApiRange);
		Map<String, Object> tree = getFieldsTree(repository.getAllMap(sheetName));
		wb.close();
		return tree;
	}
	
	public int parse2(Sheet sheet, Row startRow, Field parent, ExcelApiRange excelApiRange) {
		int lastRowNum = sheet.getLastRowNum();

		Range fieldRange = excelApiRange.getFieldRange();
		Range nesRange = excelApiRange.getNecessaryRange();
		Range nullRange = excelApiRange.getNNullYNRange();
		Range tRange = excelApiRange.getTypeRange();

		for (int i = startRow.getRowNum() + 1; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			Field currentField;
			// extract field name
			for (int j = fieldRange.getStartCloumnNum(); j <= fieldRange.getEndCloumnNum(); j++) {
				Cell cell = row.getCell(j);
				String text = cell.getStringCellValue();
				if (!"".equals(text.trim())) {
					int depth = j - fieldRange.getStartCloumnNum() + 1;
					if (depth <= parent.getDepth()) {
						return i - 1;
					}
					String fieldName = text.trim();
					int id = depth + fieldName.hashCode();
					
					Field.Type type = null;
					try {
						type = getType(row.getCell(tRange.getStartCloumnNum()).getStringCellValue());
					} catch(Exception e) {
						throw new RuntimeException("sheet name: " + sheet.getSheetName() + "Row: " + i);
					}
					
					// get Field Instance by Type(Leaf, Compsite)
					currentField = fieldFactory(type);
					currentField.setDepth(depth).setName(fieldName).setId(id)
							.setNullYN(strToBoolean(row.getCell(nullRange.getStartCloumnNum()).getStringCellValue()))
					.setNecessaryYN(
							strToBoolean(row.getCell(nesRange.getStartCloumnNum()).getStringCellValue()))
					.setType(type)
					.setParentId(parent.getId());

					repository.insert(sheet.getSheetName(), currentField);

					if (type == Field.Type.List || type == Field.Type.Map) {
						i = parse2(sheet, sheet.getRow(i), currentField, excelApiRange);
					}
				}
			}
		}
		return lastRowNum;
	}
	
	public static Map<String, Object> getFieldsTree(Map<Integer, Field> allMap) {
		Field root = Field.getRoot();
		Map<String, Object> map = new HashMap<>();
		addChild(root, map, allMap);
		return map;
	}

	public static Object addChild(Field f, Map<String, Object> map, Map<Integer, Field> allMap) {
		if (isLeaf(f, allMap)) {
			return f;
		} else {
			Set<Integer> childs = allMap.get(f.getId()).getChildIds();
			Iterator<Integer> it = childs.iterator();
			while (it.hasNext()) {
				int id = it.next();
				Field ff = allMap.get(id);
				Map<String, Object> mapc = new HashMap<>();
				map.put(ff.getName(), addChild(ff, mapc, allMap));
			}
			return map;
		}
	}

	private static ExcelApiRange getRange(Sheet sheet) throws Exception {
		Row firstrow = sheet.getRow(sheet.getFirstRowNum());
		int lastRowNum = sheet.getLastRowNum();
		CellRangeAddress frange = getFieldCellRange(sheet);
		Cell nullYN = getNullYN(sheet);
		Cell nessaryYN = getNessaryYN(sheet);
		Cell type = getType(sheet);

		ExcelApiRange excelApiRange = new ExcelApiRange();
		excelApiRange.setExcelStartRowIndex(sheet.getFirstRowNum()).setExcelLastRowIndex(firstrow.getLastCellNum())
				.setFieldRange(
						new Range(frange.getFirstRow(), lastRowNum, frange.getFirstColumn(), frange.getLastColumn()))
				.setNecessaryRange(new Range(nessaryYN.getRowIndex(), lastRowNum, nessaryYN.getColumnIndex(),
						nessaryYN.getColumnIndex()))
				.setNNullYNRange(
						(new Range(nullYN.getRowIndex(), lastRowNum, nullYN.getColumnIndex(), nullYN.getColumnIndex())))
				.setTypeRange(new Range(type.getRowIndex(), lastRowNum, type.getColumnIndex(), type.getColumnIndex()));
		return excelApiRange;
	}

	private static Cell getType(Sheet sheet) throws Exception {
		Cell type = ExcelUtils.findCellByValue(sheet, "타입");
		return type;
	}

	private static Cell getNessaryYN(Sheet sheet) throws Exception {
		Cell nessaryYN = ExcelUtils.findCellByValue(sheet, "필수여부");
		return nessaryYN;
	}

	private static CellRangeAddress getFieldCellRange(Sheet sheet) {
		return findMergedRegion(sheet, "field");
	}

	private static Cell getNullYN(Sheet sheet) throws Exception {
		return ExcelUtils.findCellByValue(sheet, "NULL 여부");
	}

	private static Field fieldFactory(Field.Type type) {
		return (type == Field.Type.List || type == Field.Type.Map) ? new CompositeField() : new LeafField();
	}

	private static Field.Type getType(String str) {
		Field.Type t = types.get(str.toLowerCase());
		if(t != null) return t;
		else throw new RuntimeException("해당 타입없습니다!!!!! " + str );
	}

	private static CellRangeAddress findMergedRegion(Sheet sheet, String keyword) {
		List<CellRangeAddress> listCombineCell = ExcelUtils.getCombineCell(sheet);
		for (CellRangeAddress ca : listCombineCell) {
			int firstC = ca.getFirstColumn();
			int firstR = ca.getFirstRow();
			String text = ExcelUtils.getMergedRegionValue(sheet, firstR, firstC);
			System.out.println(text);
			if (text.toLowerCase().contains(keyword)) {
				return ca;
			}
		}
		throw new NoSuchElementException("Field가없음.. Excel를 확인하세요");
	}

	private static boolean isLeaf(Field f, Map<Integer, Field> allMap) {
		Field f1 = allMap.get(f.getId());
		return f1.getChildIds().isEmpty() ? true : false;
	}

	private static ExcelApiRange range(Sheet sheet) throws Exception {
		return ExcelParser.getRange(sheet);
	}
}
