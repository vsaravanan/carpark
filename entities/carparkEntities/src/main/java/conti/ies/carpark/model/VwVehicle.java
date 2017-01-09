package conti.ies.carpark.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Subselect;
import org.hibernate.validator.constraints.NotBlank;




@Entity
@Subselect("select distinct v.*, u.userid is not null as hasUser " +
" from vehicle v " +
" left join users u " +
" on v.vehicleid = u.vehicleid order by v.vehicleId desc")
//@Synchronize( "vehicle")
public class VwVehicle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VehicleId")
	private Integer id;


	private String iuNo;

	@NotBlank
	private String vehicleNo;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIuNo() {
		return iuNo;
	}

	public void setIuNo(String iuNo) {
		this.iuNo = iuNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}


	private boolean hasUser;

	public boolean isHasUser() {
		return hasUser;
	}

	public void setHasUser(boolean hasUser) {
		this.hasUser = hasUser;
	}
}
