package conti.ies.carpark.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;

@Entity
public class SlotUsed {

	@Id
	@SequenceGenerator(name ="SlotUsed_seq" ,sequenceName="SlotUsed_seq", allocationSize = 1)
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator="SlotUsed_seq")
	private Integer slotUsedId;

	@NotNull
	private Integer calendarId;

	@NotNull
	private Integer slotId;

	private String userId;

	private String parkingId;

	@NotBlank
	private String status;



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

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParkingId() {
		return parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}