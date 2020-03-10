package conti.ies.carpark.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import javax.validation.constraints.NotBlank;

@Entity
public class Parking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name ="Parking_seq" ,sequenceName="Parking_seq", allocationSize = 1)
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator="Parking_seq")
	private Integer parkingId;

	@NotBlank
	private Integer userId;
	@NotBlank
	private Integer vehicleId;
	@NotBlank
	private Integer slotId;
	@NotBlank
	private Integer billId;

	public Integer getParkingId() {
		return parkingId;
	}
	public void setParkingId(Integer parkingId) {
		this.parkingId = parkingId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Integer getSlotId() {
		return slotId;
	}
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}



}
