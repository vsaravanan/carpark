package conti.ies.carpark.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import conti.ies.comp.EntityProps;

@Entity
public class ParkingBill implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
    @GeneratedValue(generator = "ParkingBill_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ParkingBill_seq", sequenceName="ParkingBill_seq", allocationSize = 1)

	private Integer billId;

    @Temporal( value = TemporalType.TIMESTAMP )
	private Date entryTimeInReserve;

	@Temporal( value = TemporalType.TIMESTAMP )
	private Date exitTimeInReserve;


    @Temporal( value = TemporalType.TIMESTAMP )
	private Date entryTime;

	@Temporal( value = TemporalType.TIMESTAMP )
	private Date exitTime;

	private Integer numMinsParked;

	private BigDecimal charges;

	private String status;

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Date getEntryTimeInReserve() {
		return entryTimeInReserve;
	}

	public void setEntryTimeInReserve(Date entryTimeInReserve) {
		this.entryTimeInReserve = entryTimeInReserve;
	}

	public Date getExitTimeInReserve() {
		return exitTimeInReserve;
	}

	public void setExitTimeInReserve(Date exitTimeInReserve) {
		this.exitTimeInReserve = exitTimeInReserve;
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

	public Integer getNumMinsParked() {
		return numMinsParked;
	}

	public void setNumMinsParked(Integer numMinsParked) {
		this.numMinsParked = numMinsParked;
	}

	public BigDecimal getCharges() {
		return charges;
	}

	public void setCharges(BigDecimal charges) {
		this.charges = charges;
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


		//Map<String, String> fieldMap = new HashMap<>();
		//fieldMap.put("entryTimeHHmm", "entryTime");


		Map<String, String> classProps = new HashMap<>();
		classProps.put("keyId", "billId");
		classProps.put("sortKey", "billId");
		classProps.put("sql", sql);
		classProps.put("drivingTable", "parkingBill");

		EntityProps ep = new EntityProps(this.getClass());
		//ep.setFieldMap(fieldMap);
		ep.setClassProps(classProps);

		return ep;



	}


}