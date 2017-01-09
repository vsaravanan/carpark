package conti.ies.carpark.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class CarparkStage  implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer carparkStageId;

	public CarparkStage() {}


	public CarparkStage(Integer sessionId, Integer userId) {
		this.sessionId = sessionId;
		this.userId = userId;
	}

	private Integer sessionId;
	private Integer userId;

    @Temporal( value = TemporalType.TIMESTAMP )
	private Date whenUploaded = new Date();

	private String a;
	private String b;
	private String c;
	private String d;


	private BigDecimal latitude;
	private BigDecimal longitude;

	public Integer getCarparkStageId() {
		return carparkStageId;
	}
	public void setCarparkStageId(Integer carparkStageId) {
		this.carparkStageId = carparkStageId;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public Integer getSessionId() {
		return sessionId;
	}
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
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

}

