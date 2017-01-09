package conti.ies.carpark.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import conti.ies.comp.Cons;
import conti.ies.comp.EntityProps;

@Entity
public class Calendar  implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "calendar_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name = "calendar_seq", sequenceName="calendar_seq", allocationSize = 1)
	private Integer calendarId;

	@NotNull
    @Temporal( value = TemporalType.DATE )
	private Date entryDate;

	@NotNull
    @Temporal(value = TemporalType.TIMESTAMP )
	private Date entryTime;

	@NotNull
    @Temporal( value = TemporalType.TIME)
	private Date exitTime;

	public Integer getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getExitTime() {
		return exitTime;
	}

	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}



	public String getEntryTimeHHmm() {
		return Cons.HHmm.format(this.entryTime);
	}




	//@JsonIgnore
	@Transient
	public EntityProps getEntityProps() {
		final String sql = "WITH r as  \r\n" +
				"(  \r\n" +
				"	select s.<keyId>, ROW_NUMBER() OVER(<orderByNum>) as rownum  \r\n" +
				"	from <drivingTable> s  \r\n" +
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
				", o as\r\n" +
				"(\r\n" +
				"	select s.* from s\r\n" +
				")\r\n" +
				"	select * from o\r\n" +
				"	<orderBy>";


		Map<String, String> fieldMap = new HashMap<>();
		fieldMap.put("entryTimeHHmm", "entryTime");


		Map<String, String> classProps = new HashMap<>();
		classProps.put("keyId", "calendarId");
		classProps.put("sortKey", "entryTime");
		classProps.put("sql", sql);
		classProps.put("drivingTable", "calendar");

		EntityProps ep = new EntityProps(this.getClass());
		ep.setFieldMap(fieldMap);
		ep.setClassProps(classProps);

		return ep;



	}


}


//@Transient
//private final String fieldMap = "{\"entryTimeHHmm\":\"entryTime\"}";
//
//@Transient
//private final String keyId = "calendarId";
//
//@Transient
//private final String sortKey = "entryTime";
