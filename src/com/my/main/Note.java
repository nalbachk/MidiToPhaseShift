package com.my.main;
/**
 * 
 * @author Administrator
 */
public class Note {

	protected Long start;
	protected Long duration;
	protected Integer value;

	public Note(Long start, Long duration, Integer value) {
		this.start = start;
		this.duration = duration;
		this.value = value;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Note [start=" + start + ", duration=" + duration + ", value=" + value + "]";
	}
}
