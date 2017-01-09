package conti.ies.carpark.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import conti.ies.comp.EntityProps;
import conti.ies.comp.FieldFilter;

@Entity
public class VwSlotUsed  implements Serializable {



	private static final long serialVersionUID = 1L;

	@Id
	private Integer slotUsedId;
	private Integer calendarId;
	private String calendarRow;
	private Integer slotId;
	private String  parkingSlotRow;
	private String status;
	private Integer userId;
	private String  userRow;
	private Integer parkingId;


/*	@JsonIgnore
	@Transient
	public String joinTableQuery(HashMap<String,?> map) {
		String joinQry = "";
		if (map != null && map.size() > 0 )
		{
			for (Map.Entry<String, ?> entry : map.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			}
		}

		return joinQry;
	}*/


	//@JsonIgnore
	@Transient
	public EntityProps getEntityProps() {


		final String sql = "WITH r as  \r\n" +
				"(  \r\n" +
				"	select s.<keyId>, ROW_NUMBER() OVER(<orderByNum>) as rownum  \r\n" +
				"	from <drivingTable> s \r\n" +
				"	<extraTables>\r\n" +
				"	<where>  \r\n" +
				")   \r\n" +
				", s as \r\n" +
				"( \r\n" +
				"	select s.*  \r\n" +
				"	from r  \r\n" +
				"	inner join <drivingTable> s  \r\n" +
				"	on r.<keyId> = s.<keyId>  \r\n" +
				"	and r.rownum between ( <skip>  + 1 ) and (<skip> + <pageSize>)  \r\n" +
				")	 \r\n" +
				", p as \r\n" +
				"( \r\n" +
				"	select s.*, \r\n" +
				"		p.location || ' - ' || \r\n" +
				"		p.level || ' - ' || \r\n" +
				"		p.slotNo  parkingSlotRow\r\n" +
				"	from s  \r\n" +
				"	inner join parkingSlot p \r\n" +
				"	on s.slotId = p.slotId \r\n" +
				") \r\n" +
				", c as\r\n" +
				"(\r\n" +
				"	select p.*, \r\n" +
				"		to_char(c.entryDate, 'DD-Mon-YYYY') || '  ' ||\r\n" +
				"		to_char(c.entryTime,'HH24:MI') || ' - ' || \r\n" +
				"		to_char(c.exitTime,'HH24:MI') calendarRow \r\n" +
				"	from p \r\n" +
				"	inner join calendar c \r\n" +
				"	on p.calendarId = c.calendarId \r\n" +
				"), u as\r\n" +
				"(\r\n" +
				"	select c.*, \r\n" +
				"		u.userName || ' ' || \r\n" +
				"		u.email  || ' ' ||\r\n" +
				"		u.phoneNo userRow\r\n" +
				"	from c\r\n" +
				"	left join users u\r\n" +
				"	on c.userId = u.userId\r\n" +
				")\r\n" +
				", o as\r\n" +
				"(\r\n" +
				"	select u.* from u\r\n" +
				")\r\n" +
				"	select * from o\r\n" +
				"	<orderBy>";


		Map<String, String> fieldMap = new HashMap<>();

		FieldFilter ff = new FieldFilter(
				"string",
				"c",
				"calendar",
				"s.calendarId = c.calendarId"
				);
		Map<String, FieldFilter> fieldFilters = new HashMap<>();
		fieldFilters.put("entryTime", ff);
		fieldFilters.put("exitTime", ff);
		fieldFilters.put("slotId",
				new FieldFilter(
						"integer",
						"",
						"",
						""
						)
				);

		fieldFilters.put("userId",
				new FieldFilter(
						"integer",
						"",
						"",
						""
						)
				);


		Map<String, String> classProps = new HashMap<>();
		classProps.put("keyId", "slotUsedId");
		classProps.put("sortKey", "slotUsedId");
		classProps.put("sql", sql);
		classProps.put("drivingTable", "slotUsed");

		EntityProps ep = new EntityProps(this.getClass());
		ep.setFieldMap(fieldMap);
		ep.setClassProps(classProps);
		ep.setFieldFilters(fieldFilters);

		return ep;



	}

	public Integer getSlotUsedId() {
		return slotUsedId;
	}
	public void setSlotUsedId(Integer slotUsedId) {
		this.slotUsedId = slotUsedId;
	}
	public Integer getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}
	public String getCalendarRow() {
		return calendarRow;
	}
	public void setCalendarRow(String calendarRow) {
		this.calendarRow = calendarRow;
	}
	public Integer getSlotId() {
		return slotId;
	}
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}
	public String getParkingSlotRow() {
		return parkingSlotRow;
	}
	public void setParkingSlotRow(String parkingSlotRow) {
		this.parkingSlotRow = parkingSlotRow;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getParkingId() {
		return parkingId;
	}
	public void setParkingId(Integer parkingId) {
		this.parkingId = parkingId;
	}
	public String getUserRow() {
		return userRow == null ? "" : userRow;
	}
	public void setUserRow(String userRow) {
		this.userRow = userRow;
	}

}