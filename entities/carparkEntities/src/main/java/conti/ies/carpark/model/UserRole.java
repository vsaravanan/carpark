package conti.ies.carpark.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotBlank;

@Entity
//@Table(name="USERROLE")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer roleId;


	@NotBlank
	private String roleType;

	@NotBlank
	private String accessTo;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roleId;
		result = prime * result + ((roleType == null) ? 0 : roleType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserRole))
			return false;
		UserRole other = (UserRole) obj;
		if (roleId != other.roleId)
			return false;
		if (roleType == null) {
			if (other.roleType != null)
				return false;
		} else if (!roleType.equals(other.roleType))
			return false;

		if (accessTo == null) {
			if (other.accessTo != null)
				return false;
		} else if (!accessTo.equals(other.accessTo))
			return false;


		return true;
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + roleId + ",  type=" + roleType	+ ",  accessTo=" + accessTo	+ "]";
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getAccessTo() {
		return accessTo;
	}

	public void setAccessTo(String accessTo) {
		this.accessTo = accessTo;
	}


}
