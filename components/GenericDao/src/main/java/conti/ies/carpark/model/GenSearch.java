package conti.ies.carpark.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import conti.ies.comp.Cons;
import conti.ies.comp.Cons.eSlot;

@Service
public class GenSearch implements Serializable {


	private static final long serialVersionUID = 1L;

	private String formMsg;

	private String formErr;

	private int skip;

	private int page;

	private int pageSize;


	@DateTimeFormat(pattern = Cons.STRddMMMyyyyHHmm)
	@Temporal(TemporalType.TIMESTAMP )
	private Date fromDate;

	@DateTimeFormat(pattern  = Cons.STRddMMMyyyyHHmm)
	@Temporal(TemporalType.TIMESTAMP)
	private Date toDate;


	private String slotId;

	private String newStatus;

	private String selectedRows;

	private String userId;

	private String userName;

	private eSlot slot;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getFormMsg() {
		return formMsg;
	}

	public void setFormMsg(String formMsg) {
		this.formMsg = formMsg;
	}

	public String getFormErr() {
		return formErr;
	}

	public void setFormErr(String formErr) {
		this.formErr = formErr;
	}


	public eSlot getSlot() {
		return slot;
	}

	public void setSlot(eSlot slot) {
		this.slot = slot;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}


	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}



	public String getSelectedRows() {
		return selectedRows;
	}

	public void setSelectedRows(String selectedRows) {
		this.selectedRows = selectedRows;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}



}

