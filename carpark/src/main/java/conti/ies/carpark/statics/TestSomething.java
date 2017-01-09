package conti.ies.carpark.statics;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSomething {

	private static final Logger logger = LoggerFactory.getLogger(TestSomething.class);

	public static void main(String[] args) throws XmlException, OpenXML4JException, IOException {

		   XSSFEventBasedExcelExtractor extractor = new XSSFEventBasedExcelExtractor("D:\\work\\carparkupload\\capitaland.xlsx");
		   extractor.setIncludeTextBoxes(true);
		   String something = extractor.getText();
		   logger.info("XSSFEventBasedExcelExtractor");
		   extractor.close();

		   //MissingRecordAwareHSSFListener mr = new MissingRecordAwareHSSFListener(null);

	}

}
