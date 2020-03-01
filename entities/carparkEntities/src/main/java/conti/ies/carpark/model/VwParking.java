package conti.ies.carpark.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import conti.ies.comp.EntityProps;
import conti.ies.comp.FieldFilter;

@Entity
public class VwParking  implements Serializable {



	private static final long serialVersionUID = 1L;

	@Id
	private Integer parkingId;
	private Integer billId;
	private String  parkingBillRow;
	private String  status;
	private Integer slotId;
	private String  parkingSlotRow;
	private Integer userId;
	private String  userRow;
	private Integer vehicleId;
	private String  vehicleRow;
	private Integer slotUsedId;
	private Integer finalBillId;




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
				"		p.slotNo  || ' - ' || \r\n" +
				"		p.status\r\n" +
				"			parkingSlotRow\r\n" +
				"	from s  \r\n" +
				"	inner join parkingSlot p \r\n" +
				"	on s.slotId = p.slotId \r\n" +
				") \r\n" +
				", b as\r\n" +
				"(\r\n" +
				"	select p.*, \r\n" +
				"		to_char(b.entryTimeInReserve, 'DD-Mon-YYYY HH24:MI') || ' - ' || \r\n" +
				"		 to_char(b.exitTimeInReserve,  'HH24:MI') || ' ,  ' ||\r\n" +
				"		case when b.entryTime is null then ' ' else 'entry: ' || to_char(b.entryTime, 'HH24:MI') || ',  ' end ||\r\n" +
				"		case when b.exitTime is null then ' ' else 'exit: ' || to_char(b.exitTime, 'HH24:MI') || ',  ' end ||\r\n" +
				"		case when b.numMinsParked is null then ' ' else b.numMinsParked || ' mins,  ' end ||\r\n" +
				"		case when b.charges is null then ' ' else ' S$ ' || b.charges end parkingBillRow,\r\n" +
				"		b.status\r\n" +
				" 	from p \r\n" +
				"	inner join parkingBill b\r\n" +
				"	on COALESCE(p.finalBillId, p.billId) = b.billId \r\n" +
				"), u as\r\n" +
				"(\r\n" +
				"	select b.*, \r\n" +
				"		u.userName || ' ' || \r\n" +
				"		u.email  || ' ' ||\r\n" +
				"		u.phoneNo userRow\r\n" +
				"	from b\r\n" +
				"	inner join users u\r\n" +
				"	on b.userId = u.userId\r\n" +
				"), v as\r\n" +
				"(\r\n" +
				"	select u.*, \r\n" +
				"		v.vehicleNo || ' ' || \r\n" +
				"		v.IUNo vehicleRow\r\n" +
				"	from u\r\n" +
				"	left join vehicle v\r\n" +
				"	on u.vehicleId = v.vehicleId\r\n" +
				")\r\n" +
				", o as\r\n" +
				"(\r\n" +
				"	select v.* from v\r\n" +
				")\r\n" +
				"	select * from o\r\n" +
				"	<orderBy>";


		Map<String, String> fieldMap = new HashMap<>();

		FieldFilter ff1 = new FieldFilter(
				"string",
				"u",
				"slotused",
				"s.slotusedid = u.slotusedid"
		);
		FieldFilter ff2 = new FieldFilter(
				"string",
				"c",
				"calendar",
				"u.calendarId = c.calendarId"
		);
		Multimap<String, FieldFilter> fieldFilters= ArrayListMultimap.create();

		fieldFilters.put("entryTime", ff1);
		fieldFilters.put("entryTime", ff2);
		fieldFilters.put("exitTime", ff1);
		fieldFilters.put("exitTime", ff2);


		Map<String, String> classProps = new HashMap<>();
		classProps.put("keyId", "parkingId");
		classProps.put("sortKey", "parkingId");
		classProps.put("sql", sql);
		classProps.put("drivingTable", "parking");

		EntityProps ep = new EntityProps(this.getClass());
		ep.setFieldMap(fieldMap);
		ep.setClassProps(classProps);
		ep.setFieldFilters(fieldFilters);


		return ep;



	}




	public Integer getParkingId() {
		return parkingId;
	}




	public void setParkingId(Integer parkingId) {
		this.parkingId = parkingId;
	}




	public Integer getBillId() {
		return billId;
	}




	public void setBillId(Integer billId) {
		this.billId = billId;
	}




	public String getParkingBillRow() {
		return parkingBillRow;
	}




	public void setParkingBillRow(String parkingBillRow) {
		this.parkingBillRow = parkingBillRow;
	}




	public String getStatus() {
		return status;
	}




	public void setStatus(String status) {
		this.status = status;
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




	public Integer getUserId() {
		return userId;
	}




	public void setUserId(Integer userId) {
		this.userId = userId;
	}




	public String getUserRow() {
		return userRow;
	}




	public void setUserRow(String userRow) {
		this.userRow = userRow;
	}




	public Integer getVehicleId() {
		return vehicleId;
	}




	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}




	public String getVehicleRow() {
		return vehicleRow;
	}




	public void setVehicleRow(String vehicleRow) {
		this.vehicleRow = vehicleRow;
	}

	public Integer getSlotUsedId() {
		return slotUsedId;
	}

	public void setSlotUsedId(Integer slotUsedId) {
		this.slotUsedId = slotUsedId;
	}

	public Integer getFinalBillId() {
		return finalBillId;
	}

	public void setFinalBillId(Integer finalBillId) {
		this.finalBillId = finalBillId;
	}
}