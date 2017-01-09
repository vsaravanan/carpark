package conti.ies.carpark.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import conti.ies.comp.State;


@Entity
@Table(name = "USERS")
public class User  implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "users_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_seq", sequenceName="users_seq", allocationSize = 1)
	private Integer userId;

	@NotBlank
	private String userName;
	private String pwd;

	@NotBlank
	private String address;

	@Email(message="Email format should be abc@conti.com ")
	private String email;

	@Pattern(regexp="(^$|[0-9]{8})", message="8 digit phone no only")
	private String phoneNo;


	private String userType;

	private Integer vehicleId;

	private String state=State.ACTIVE.getState();


//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "APP_USER_USER_PROFILE",
//             joinColumns = { @JoinColumn(name = "USER_ID") },
//             inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID") })
//	private Set<UserProfile> userProfiles = new HashSet<UserProfile>();

//	@OneToOne(fetch=FetchType.LAZY)   //(cascade = CascadeType.ALL)
//	@JoinColumn(name ="VehicleId", insertable = false, updatable = false)
//	private Vehicle vehicle;
//	@OneToMany(mappedBy="user")
//	@JsonBackReference
//	private Set<SlotUsed> slotUseds;
//
//	@OneToMany(mappedBy="user")
//	private Set<Parking> parkings;
//	@ManyToOne
//	@JoinColumn(name ="VehicleId", insertable = false, updatable = false)
//	private Vehicle vehicle;


//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "APP_USER_USER_PROFILE",
//             joinColumns = { @JoinColumn(name = "USER_ID") },
//             inverseJoinColumns = { @JoinColumn(name = "USER_PROFILE_ID") })
//	private Set<UserRole> userProfiles = new HashSet<UserProfile>();

//	@NotBlank
//	@OneToMany(fetch = FetchType.EAGER)
//	@JoinColumn(referencedColumnName="userType", name ="RoleType", insertable = false, updatable = false)
//	private Set<UserRole> userRole = new HashSet<UserRole>();


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}




	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	@Transient
	private boolean guest = false;

	@Transient
	public boolean isGuest() {
		return guest;
	}

	@Transient
	public void setGuest(boolean yn) {
		this.guest = yn;
	}

	@Transient
	private boolean view = false;

	@Transient
	public boolean isView() {
		return view;
	}

	@Transient
	public void setView(boolean yn) {
		this.view = yn;
	}

}

