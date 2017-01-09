package conti.ies.carpark.model;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Vehicle implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "Vehicle_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name = "Vehicle_seq", sequenceName="Vehicle_seq", allocationSize = 1)
	@Column(name = "VehicleId")
	private Integer id;

	private String iuNo;

	@NotBlank
	private String vehicleNo;




	public Vehicle(VwVehicle vw) {
		super();
		if (vw.getId() != null)
			this.setId(vw.getId());

		this.setIuNo(vw.getIuNo());
		this.setVehicleNo(vw.getVehicleNo());
	}

	public Vehicle() {super();}

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


}
