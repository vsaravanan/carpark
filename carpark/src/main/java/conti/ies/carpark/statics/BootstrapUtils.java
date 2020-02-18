package conti.ies.carpark.statics;


import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BootstrapUtils {
	private static BootstrapUtils	    instance;

	private HashMap<String, String[][]>	bootstrapSheetContent	= null;

	private BootstrapUtils() {
	}

	public static BootstrapUtils instance() {
		if ( instance == null ) {
			instance = new BootstrapUtils();
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public String[][] getBootstrapSheetContent(String sheetName) {
		if ( bootstrapSheetContent == null ) {
			try {
				//final InputStream fin = getClass().getClassLoader().getResourceAsStream( "D:/work/carparkupload/capitaland.xls" );
				final InputStream fin = new FileInputStream( "D:/work/carparkupload/capitaland.xls" );

				// final InputStream fin = new
				// FileInputStream("D:/DevArea/OnlineFreeJob/OnlineJobCenter/common/src/main/java/Bootstrap.xls" );
				POIFSFileSystem poifs = new POIFSFileSystem( fin );
				XLSExporter exporter = new XLSExporter( poifs, -1 );
				HashMap<String, ArrayList> result = exporter.process();

				bootstrapSheetContent = new HashMap<String, String[][]>();

				for (String sName : result.keySet()) {
					ArrayList<ArrayList> sheet = result.get( sName );
					String[][] sheetContent = new String[ sheet.size() ][];

					int rowIndex = 0;
					for (ArrayList arrayList : sheet) {
						ArrayList<String> oneRow = arrayList;
						sheetContent[ rowIndex ] = new String[ oneRow.size() ];
						int colIndex = 0;
						for (String cell : oneRow) {
							if ( cell.startsWith( "\"" ) && cell.endsWith( "\"" ) ) {
								cell = cell.substring( 1, cell.length() - 1 ).trim();
							} else if ( cell.endsWith( ".0" ) ) {
								cell = cell.substring( 0, cell.length() - 2 );
							}
							sheetContent[ rowIndex ][ colIndex++ ] = cell;
						}
						rowIndex++;
					}
					bootstrapSheetContent.put( sName, sheetContent );
				}
			} catch ( IOException ioe ) {
				ioe.printStackTrace();
			}
		}
		return bootstrapSheetContent.get( sheetName );
	}

	public static void main(String[] args) {
		( new BootstrapUtils() ).getBootstrapSheetContent( null );
	}
}