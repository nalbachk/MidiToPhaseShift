package com.my.config;

import java.io.Serializable;

public class NotePhaseShift implements Serializable, Comparable<NotePhaseShift> {

	private static final long serialVersionUID = 1L;

	protected Integer lineNr;

	protected Integer fret;

	protected Boolean closed;

	protected String comment;

	public NotePhaseShift() {
		this.setLineNr(1);
		this.setFret(0);
		this.setClosed(false);
		this.setComment("dummy");
	}

	public NotePhaseShift(Integer lineNr) {
		this.setLineNr(lineNr);
		this.setFret(0);
		this.setClosed(false);
		this.setComment("LINE " + lineNr + "-" + fret);
	}

	public NotePhaseShift(Integer lineNr, Integer fret) {
		this.setLineNr(lineNr);
		this.setFret(fret);
		this.setClosed(false);
		this.setComment("LINE " + lineNr + "-" + fret);
	}

	public Integer getLineNr() {
		return lineNr;
	}

	public void setLineNr(Integer lineNr) {
		this.lineNr = lineNr;
	}

	public Integer getFret() {
		return fret;
	}

	public void setFret(Integer fret) {
		this.fret = fret;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "NotePhaseShift [lineNr=" + lineNr + ", fret=" + fret + ", closed=" + closed + ", comment=" + comment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineNr == null) ? 0 : lineNr.hashCode());
		result = prime * result + ((fret == null) ? 0 : fret.hashCode());
		result = prime * result + ((closed == null) ? 0 : closed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotePhaseShift other = (NotePhaseShift) obj;
		if (lineNr == null) {
			if (other.lineNr != null)
				return false;
		} else if (!lineNr.equals(other.lineNr))
			return false;
		if (fret == null) {
			if (other.fret != null)
				return false;
		} else if (!fret.equals(other.fret))
			return false;
		if (closed == null) {
			if (other.closed != null)
				return false;
		} else if (!closed.equals(other.closed))
			return false;
		return true;
	}

	@Override
	public int compareTo(NotePhaseShift other) {
		int result = 0;

		result = this.getLineNr().compareTo(other.getLineNr());
		if (0 != result) {
			return result;
		}
		result = this.getFret().compareTo(other.getFret());
		if (0 != result) {
			return result;
		}

		return result;
	}
}
