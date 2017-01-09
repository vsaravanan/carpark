package conti.ies.carpark.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import conti.ies.comp.EntityProps;

@Entity
public class ParkingSlot implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "ParkingSlot_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ParkingSlot_seq", sequenceName="ParkingSlot_seq", allocationSize = 1)
	private Integer slotId;

	@NotBlank
	private String location;

	@Min(1)
	private Integer level;

	@Min(1)
	private Integer slotNo;

	private String  tagId;

	private String  status;


	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(Integer slotNo) {
		this.slotNo = slotNo;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



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
				", o as\r\n" +
				"(\r\n" +
				"	select s.* from s\r\n" +
				")\r\n" +
				"	select * from o\r\n" +
				"	<orderBy>;";


		Map<String, String> fieldMap = new HashMap<>();


		Map<String, String> classProps = new HashMap<>();
		classProps.put("keyId", "slotId");
		classProps.put("sortKey", "slotId");
		classProps.put("sql", sql);
		classProps.put("drivingTable", "parkingSlot");

		EntityProps ep = new EntityProps(this.getClass());
		ep.setFieldMap(fieldMap);
		ep.setClassProps(classProps);

		return ep;



	}

}
