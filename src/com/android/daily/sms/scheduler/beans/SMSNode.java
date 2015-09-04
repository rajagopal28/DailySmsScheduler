package com.android.daily.sms.scheduler.beans;

import java.util.Date;

/**
 * @author Rajagopal
 *
 */
public class SMSNode {
	private String content;
	private String toNumber;
	private int hours;
	private int mins;
	private boolean isRepeat;
	private int id;
	private Date lastSentTimeStamp;

	public SMSNode(int hh, int mm, String cont, String to, boolean isRepeat) {
		this.hours = hh;
		this.mins = mm;
		this.content = cont;
		this.isRepeat = isRepeat;
		this.toNumber = to;
	}

	public SMSNode() {
		
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the toNumber
	 */
	public String getToNumber() {
		return toNumber;
	}

	/**
	 * @param toNumber the toNumber to set
	 */
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @return the mins
	 */
	public int getMins() {
		return mins;
	}

	/**
	 * @param mins the mins to set
	 */
	public void setMins(int mins) {
		this.mins = mins;
	}

	/**
	 * @return the isRepeat
	 */
	public boolean isRepeat() {
		return isRepeat;
	}

	/**
	 * @param isRepeat the isRepeat to set
	 */
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the lastSentTimeStamp
	 */
	public Date getLastSentTimeStamp() {
		return lastSentTimeStamp;
	}

	/**
	 * @param lastSentTimeStamp the lastSentTimeStamp to set
	 */
	public void setLastSentTimeStamp(Date lastSentTimeStamp) {
		this.lastSentTimeStamp = lastSentTimeStamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SMSNode [content=" + content + ", toNumber=" + toNumber
				+ ", hours=" + hours + ", mins=" + mins + ", isRepeat="
				+ isRepeat + ", id=" + id + ", lastSentTimeStamp="
				+ lastSentTimeStamp + "]";
	}

	
	
}
