package conti.ies.carpark.statics;


import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class XLSExporter implements HSSFListener {
	private final int	                 minColumns;
	private final POIFSFileSystem	     fs;
	private int	                         lastRowNumber;
	private int	                         lastColumnNumber;

	/** Should we output the formula, or the value it has? */
	private final boolean	             outputFormulaValues	= true;

	// Records we pick up as we process
	private SSTRecord	                 sstRecord;
	private FormatTrackingHSSFListener	 formatListener;

	// For handling formulas with string results
	private int	                         nextRow;
	private int	                         nextColumn;
	private boolean	                     outputNextStringRecord;

	protected HashMap<String, ArrayList>	sheetsByName;
	protected ArrayList<String>	         sheetsName;
	protected ArrayList<ArrayList>	     currentSheet;
	protected ArrayList<String>	         currentRow;

	/**
	 * Creates a new XLS -> CSV converter
	 *
	 * @param fs
	 *            The POIFSFileSystem to process
	 * @param output
	 *            The PrintStream to output the CSV to
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 */
	public XLSExporter(POIFSFileSystem fs, int minColumns) {
		this.fs = fs;
		this.minColumns = minColumns;
	}

	/**
	 * Creates a new XLS -> CSV converter
	 *
	 * @param filename
	 *            The file to process
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public XLSExporter(String filename, int minColumns) throws IOException, FileNotFoundException {
		this( new POIFSFileSystem( new FileInputStream( filename ) ), minColumns );
	}

	/**
	 * Initiates the processing of the XLS file to CSV
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, ArrayList> process() throws IOException {
		sheetsByName = new HashMap<String, ArrayList>();
		sheetsName = new ArrayList<String>();
		currentSheet = new ArrayList<ArrayList>();
		MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener( this );
		formatListener = new FormatTrackingHSSFListener( listener );

		HSSFEventFactory factory = new HSSFEventFactory();
		HSSFRequest request = new HSSFRequest();
		request.addListenerForAllRecords( formatListener );

		factory.processWorkbookEvents( request, fs );

		int idx = 0;
		for (String sheetName : sheetsName) {
			String key = Integer.toString( idx );
			ArrayList<ArrayList> sheet = sheetsByName.get( key );
			sheetsByName.remove( key );
			sheetsByName.put( sheetName, sheet );
			idx++;
		}

		return sheetsByName;
	}

	int	tmpIdx	= 0;

	/**
	 * Main HSSFListener method, processes events, and outputs the CSV as the file is processed.
	 */
	@Override
	public void processRecord(Record record) {
		int thisRow = -1;
		int thisColumn = -1;
		String thisStr = null;

		switch ( record.getSid() ) {
			case BOFRecord.sid:
				BOFRecord bof = ( BOFRecord ) record;
				if ( bof.getType() == bof.TYPE_WORKSHEET ) {
					currentSheet = new ArrayList<ArrayList>();
					currentRow = new ArrayList<String>();
					sheetsByName.put( Integer.toString( tmpIdx++ ), currentSheet );
				}
				break;
			case BoundSheetRecord.sid:
				BoundSheetRecord bsrecord = ( BoundSheetRecord ) record;
				sheetsName.add( bsrecord.getSheetname() );
				break;
			case SSTRecord.sid:
				sstRecord = ( SSTRecord ) record;
				break;

			case BlankRecord.sid:
				BlankRecord brec = ( BlankRecord ) record;

				thisRow = brec.getRow();
				thisColumn = brec.getColumn();
				thisStr = "";
				break;
			case BoolErrRecord.sid:
				BoolErrRecord berec = ( BoolErrRecord ) record;

				thisRow = berec.getRow();
				thisColumn = berec.getColumn();
				thisStr = "";
				break;

			case FormulaRecord.sid:
				FormulaRecord frec = ( FormulaRecord ) record;

				thisRow = frec.getRow();
				thisColumn = frec.getColumn();

				if ( outputFormulaValues ) {
					if ( Double.isNaN( frec.getValue() ) ) {
						// Formula result is a string
						// This is stored in the next record
						outputNextStringRecord = true;
						nextRow = frec.getRow();
						nextColumn = frec.getColumn();
					} else {
						thisStr = formatNumberDateCell( frec, frec.getValue() );
					}
//				} else {
//					thisStr = '"' + FormulaParser.toFormulaString( null, frec.getParsedExpression() ) + '"';
				}
				break;
			case StringRecord.sid:
				if ( outputNextStringRecord ) {
					// String for formula
					StringRecord srec = ( StringRecord ) record;
					thisStr = srec.getString();
					thisRow = nextRow;
					thisColumn = nextColumn;
					outputNextStringRecord = false;
				}
				break;

			case LabelRecord.sid:
				LabelRecord lrec = ( LabelRecord ) record;

				thisRow = lrec.getRow();
				thisColumn = lrec.getColumn();
				thisStr = '"' + lrec.getValue() + '"';
				break;
			case LabelSSTRecord.sid:
				LabelSSTRecord lsrec = ( LabelSSTRecord ) record;

				thisRow = lsrec.getRow();
				thisColumn = lsrec.getColumn();
				if ( sstRecord == null ) {
					thisStr = '"' + "(No SST Record, can't identify string)" + '"';
				} else {
					thisStr = '"' + sstRecord.getString( lsrec.getSSTIndex() ).toString() + '"';
				}
				break;
			case NoteRecord.sid:
				NoteRecord nrec = ( NoteRecord ) record;

				thisRow = nrec.getRow();
				thisColumn = nrec.getColumn();
				// TODO: Find object to match nrec.getShapeId()
				thisStr = '"' + "(TODO)" + '"';
				break;
			case NumberRecord.sid:
				NumberRecord numrec = ( NumberRecord ) record;

				thisRow = numrec.getRow();
				thisColumn = numrec.getColumn();

				// Format
				thisStr = formatNumberDateCell( numrec, numrec.getValue() );
				break;
			case RKRecord.sid:
				RKRecord rkrec = ( RKRecord ) record;

				thisRow = rkrec.getRow();
				thisColumn = rkrec.getColumn();
				thisStr = '"' + "(TODO)" + '"';
				break;
			default:
				break;
		}

		// Handle new row
		if ( ( thisRow != -1 ) && ( thisRow != lastRowNumber ) ) {
			lastColumnNumber = -1;
		}

		// Handle missing column
		if ( record instanceof MissingCellDummyRecord ) {
			MissingCellDummyRecord mc = ( MissingCellDummyRecord ) record;
			thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			thisStr = "";
		}

		// If we got something to print out, do so
		if ( thisStr != null ) {
			currentRow.add( thisStr );
		}

		// Update column and row count
		if ( thisRow > -1 ) {
			lastRowNumber = thisRow;
		}
		if ( thisColumn > -1 ) {
			lastColumnNumber = thisColumn;
		}

		// Handle end of row
		if ( record instanceof LastCellOfRowDummyRecord ) {
			// Print out any missing commas if needed
			if ( minColumns > 0 ) {
				// Columns are 0 based
				if ( lastColumnNumber == -1 ) {
					lastColumnNumber = 0;
				}
				for ( int i = lastColumnNumber; i < ( minColumns ); i++ ) {
					currentRow.add( "" );
				}
			}

			// We're onto a new row
			lastColumnNumber = -1;

			// End the row
			currentSheet.add( currentRow );
			currentRow = new ArrayList<String>();
		}
	}

	/**
	 * Formats a number or date cell, be that a real number, or the answer to a formula
	 */
	private String formatNumberDateCell(CellValueRecordInterface cell, double value) {
		// Get the built in format, if there is one
		int formatIndex = formatListener.getFormatIndex( cell );
		String formatString = formatListener.getFormatString( cell );

		if ( formatString == null ) {
			return Double.toString( value );
		} else {
			// Is it a date?
			if ( HSSFDateUtil.isADateFormat( formatIndex, formatString ) && HSSFDateUtil.isValidExcelDate( value ) ) {
				// Java wants M not m for month
				formatString = formatString.replace( 'm', 'M' );
				// Change \- into -, if it's there
				formatString = formatString.replaceAll( "\\\\-", "-" );

				// Format as a date
				Date d = HSSFDateUtil.getJavaDate( value, false );
				DateFormat df = new SimpleDateFormat( formatString );
				return df.format( d );
			} else {
				if ( formatString == "General" ) {
					// Some sort of wierd default
					return Double.toString( value );
				}

				// Format as a number
				DecimalFormat df = new DecimalFormat( formatString );
				return df.format( value );
			}
		}
	}

	public static void main(String[] args) throws Exception {
		if ( args.length < 1 ) {
			System.err.println( "Use:" );
			System.err.println( "  XLS2CSVmra <xls file> [min columns]" );
			System.exit( 1 );
		}

		int minColumns = -1;
		if ( args.length >= 2 ) {
			minColumns = Integer.parseInt( args[ 1 ] );
		}

		XLSExporter xls2csv = new XLSExporter( args[ 0 ], minColumns );
		xls2csv.process();
	}
}