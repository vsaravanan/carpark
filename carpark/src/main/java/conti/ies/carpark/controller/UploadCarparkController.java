package conti.ies.carpark.controller;

import conti.ies.carpark.dao.ICarparkStageDao;
import conti.ies.carpark.model.CarparkStage;
import conti.ies.carpark.model.IPrepareAndExecute;
import conti.ies.carpark.model.User;
import conti.ies.carpark.service.CommonService;
import conti.ies.carpark.service.UploadCarparkService;
import conti.ies.carpark.statics.StaticFuncs;
import conti.ies.comp.FileBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UploadCarparkController {

	private static final Logger logger = LoggerFactory.getLogger(UploadCarparkController.class);


	@Autowired
	private ICarparkStageDao carparkStageDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private IPrepareAndExecute pae;

	@RequestMapping("external/showUploadCarpark")
	public String showUploadCarpark(Map<String, Object> model) {
		model.put("fileBean", new FileBean());
        return "UploadCarpark";
	}


	// @RequestPart("fileData") tried not working
	@RequestMapping(value = "external/uploadCarpark", method = RequestMethod.POST)
	public String upload(
	        @RequestParam(value = "fileData", required = false) CommonsMultipartFile fileData ,
	        Model model
	        )
	{

		model.addAttribute("fileBean", new FileBean());

		if (! (fileData.getContentType().equalsIgnoreCase("application/vnd.ms-excel") ||
				fileData.getContentType().equalsIgnoreCase(
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
			)){
			model.addAttribute("error","InvalidExcelFile");
			model.addAttribute("fileName",fileData.getOriginalFilename());
			return "UploadCarpark";
			//throw new ValidationException();
		}


		List<List<String>>  csv = null;
		try {
			FileBean fileBean = new FileBean();
			fileBean.setFileData(fileData);

			 csv = UploadCarparkService.extractFromExcel(fileBean);
		} catch (Exception e) {

		}


		String userName = StaticFuncs.getUserName();
		User user = commonService.getUserFromUserName(userName);
		Integer userId = user.getUserId();
		Integer sessionId = commonService.getSessionId();


		int maxCols = 4;
		int skipRows = 1;

		List<CarparkStage> lstUplData = new ArrayList<>();

		int j = 0;
		for (List<String> list : csv) {

			if (j++ > skipRows) {

				CarparkStage uplData = new CarparkStage(sessionId, userId);

				for (int i = 0; i < maxCols && i < list.size(); i++) {

					switch (i) {
					case 0:
						uplData.setA(list.get(i));
						break;
					case 1:
						uplData.setB(list.get(i));
						break;
					case 2:
						uplData.setC(list.get(i));
						break;
					case 3:
						uplData.setD(list.get(i));
						break;

					default:
						break;
					}


				}

				String c = uplData.getC();
				String d = uplData.getD();

				String latlong = null;

				if (StringUtils.isNotBlank(d))
					latlong = d;
				else if (StringUtils.isNotBlank(c))
					latlong = c;

				if (latlong != null)
				{
					String[] latlongSplit = latlong.split(",");
					uplData.setLatitude(StaticFuncs.stringToBigDecimal(latlongSplit[0]));
					uplData.setLongitude(StaticFuncs.stringToBigDecimal(latlongSplit[1]));

				}


				lstUplData.add(uplData);

			}

		}

		String sql = "delete from carparkStage where userId = <userId>";
		sql = sql.replace("<userId>", userId.toString() );
		pae.executeUpdate(sql, "clean carparkStage table for the user");


		carparkStageDao.saveOrUpdate(lstUplData);

		sql = "delete from carpark c\r\n" +
				"using carparkStage s\r\n" +
				"where c.carpark = s.b\r\n" +
				"and s.latitude is not null\r\n" +
				"and s.longitude is not null\r\n" +
				"and s.sessionId = <sessionId>\r\n" +
				"and s.userId = <userId>";
		sql = sql.replace("<sessionId>", sessionId.toString() );
		sql = sql.replace("<userId>", userId.toString() );
		pae.executeUpdate(sql, "delete matching existing records before inserting new records");


		sql = "insert into carpark \r\n" +
				"	(carpark)\r\n" +
				"	select \r\n" +
				"	 distinct b\r\n" +
				"	from carparkStage s\r\n" +
				"	where s.latitude is not null\r\n" +
				"	and s.longitude is not null \r\n" +
				"	and s.sessionId = <sessionId>\r\n" +
				"	and s.userId = <userId>";
		sql = sql.replace("<sessionId>", sessionId.toString() );
		sql = sql.replace("<userId>", userId.toString() );
		int inserted = pae.executeUpdate(sql, "insert new records uploaded into carpark main table");

		sql = "		update	carpark c\r\n" +
				"	set	latitude = s.latitude,\r\n" +
				"		longitude = s.longitude,\r\n" +
				"		userId = s.userId,\r\n" +
				"		whenUploaded = s.whenUploaded\r\n" +
				"	from 	carparkStage s\r\n" +
				"	where 	c.carpark = s.b\r\n" +
				"	and 	s.latitude is not null\r\n" +
				"	and s.longitude is not null\r\n" +
				"	and s.sessionId = <sessionId>\r\n" +
				"	and s.userId = <userId>";
		sql = sql.replace("<sessionId>", sessionId.toString() );
		sql = sql.replace("<userId>", userId.toString() );
		pae.executeUpdate(sql, "updated with latitude, longitude");


		sql = "insert into carparkHistory\r\n" +
				"select * from carparkStage s\r\n" +
				"where s.sessionId = <sessionId>\r\n" +
				"and s.userId = <userId>";
		sql = sql.replace("<sessionId>", sessionId.toString() );
		sql = sql.replace("<userId>", userId.toString() );
		int totalRecords = pae.executeUpdate(sql, "insert new records into carpark history table for audit");

		//GenSearch search = new GenSearch();
		//search.setFormMsg("Successfully uploaded file " + fileData.getOriginalFilename());
		//model.addAttribute("search", search );
		model.addAttribute("success","success");
		model.addAttribute("filename",fileData.getOriginalFilename());
		model.addAttribute("inserted",inserted);
		model.addAttribute("totalRecords", totalRecords);


		return "UploadCarpark";
		//return "redirect:/external/showUploadCarpark?success";
	}


}
