package conti.ies.comp;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileBean {

	  private CommonsMultipartFile fileData;

	  public CommonsMultipartFile getFileData()
	  {
	    return fileData;
	  }

	  public void setFileData(CommonsMultipartFile fileData)
	  {
	    this.fileData = fileData;
	  }
}