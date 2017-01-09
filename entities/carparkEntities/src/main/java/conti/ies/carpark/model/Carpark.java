package conti.ies.carpark.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import conti.ies.comp.EntityProps;
import conti.ies.comp.FieldFilter;

@Entity
public class Carpark implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "carpark_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name = "carpark_seq", sequenceName="carpark_seq", allocationSize = 1)
	private Integer carparkId;

	@NotBlank
	private String carpark;

	@Column(precision=10, scale=6)
	@NotNull
	private BigDecimal latitude;

	@Column(precision=10, scale=6)
	@NotNull
	private BigDecimal longitude;

	private Integer userId;

	private Date whenUploaded = new Date();


	@Column(insertable=false, updatable = false)
	private String userName;

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
				"), u as \r\n" +
				"(\r\n" +
				"	select s.*, \r\n" +
				"		u.userName \r\n" +
				"	from s\r\n" +
				"	left join users u\r\n" +
				"	on s.userId = u.userId\r\n" +
				")\r\n" +
				", o as\r\n" +
				"(\r\n" +
				"	select u.* from u \r\n" +
				")\r\n" +
				"	select * from o\r\n" +
				"	<orderBy>;";


		Map<String, String> fieldMap = new HashMap<>();
		//fieldMap.put("userName", "userId");

		FieldFilter ff = new FieldFilter(
								"string",
								"u",
								"users",
								"s.userId = u.userId"
								);

		Map<String, FieldFilter> fieldFilters = new HashMap<>();
		fieldFilters.put("userName", ff);


		Map<String, String> classProps = new HashMap<>();
		classProps.put("keyId", "carparkId");
		classProps.put("sortKey", "carpark");
		classProps.put("sql", sql);
		classProps.put("drivingTable", "carpark");

		EntityProps ep = new EntityProps(this.getClass());
		ep.setFieldMap(fieldMap);
		ep.setClassProps(classProps);
		ep.setFieldFilters(fieldFilters);

		return ep;



	}

	public Integer getCarparkId() {
		return carparkId;
	}

	public void setCarparkId(Integer carparkId) {
		this.carparkId = carparkId;
	}

	public String getCarpark() {
		return carpark;
	}

	public void setCarpark(String carpark) {
		this.carpark = carpark;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getWhenUploaded() {
		return whenUploaded;
	}

	public void setWhenUploaded(Date whenUploaded) {
		this.whenUploaded = whenUploaded;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
