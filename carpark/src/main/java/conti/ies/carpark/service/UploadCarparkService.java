package conti.ies.carpark.service;

import conti.ies.comp.Cons;
import conti.ies.comp.FileBean;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UploadCarparkService {

	public static List<List<String>> extractFromExcel(FileBean fileBean)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		List<List<String>> csv = new ArrayList<>();

		ByteArrayInputStream bis = new ByteArrayInputStream(fileBean.getFileData().getBytes());

		// InputStream inp = new FileInputStream(fname);

		Workbook wb = WorkbookFactory.create(bis);

		Sheet sheet = wb.getSheetAt(0);

		Iterator rows = sheet.iterator();
		while (rows.hasNext()) {
			Row row = (Row) rows.next();
			List<String> mrow = new ArrayList<>();

			int lastCell = row.getLastCellNum();
			for (short k = 0; k < lastCell; k++) {
				Cell cell = row.getCell(k);

				if (cell == null) {
					// log(k + "");
					mrow.add("");
				} else {
					switch (cell.getCellType()) {
					case STRING:
						// log(k+cell.getStringCellValue());
						mrow.add(cell.getStringCellValue());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							// log(k+""+cell.getDateCellValue()+"");
							mrow.add(Cons.ddMMMyyyy.format(cell.getDateCellValue()));
						} else {
							// log(k+""+cell.getNumericCellValue());
							mrow.add(cell.getNumericCellValue() + "");
						}
						break;
					case BOOLEAN:
						// log(k+""+cell.getBooleanCellValue()+"");
						mrow.add(cell.getBooleanCellValue() + "");
						break;
					case FORMULA:
						// log(k+cell.getCellFormula());

						switch (cell.getCachedFormulaResultType()) {
						case STRING:
							// log(k+cell.getStringCellValue());
							mrow.add(cell.getStringCellValue());
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								// log(k+""+cell.getDateCellValue()+"");
								mrow.add(Cons.ddMMMyyyy.format(cell.getDateCellValue()));
							} else {
								// log(k+""+cell.getNumericCellValue());
								mrow.add(cell.getNumericCellValue() + "");
							}
							break;
						case BOOLEAN:
							// log(k+""+cell.getBooleanCellValue()+"");
							mrow.add(cell.getBooleanCellValue() + "");
							break;
						default:
							// log(k+"");
							mrow.add("");
						}

						break;

					} // switch outer
				} // when cell is non-empty
			} // for col level
			csv.add(mrow);

		} // while row level

		return csv;
	}

	// private static void log(String message)
	// {
	// System.out.println(message);
	// }

}