package com.my.config;

import java.io.Serializable;

public class NoteEof implements Serializable, Comparable<NoteEof> {

	private static final long serialVersionUID = 1L;

	protected Integer lineNr;

	protected Integer fret;

	protected Boolean cymbal;

	protected Boolean closed;

	protected String comment;

	public NoteEof() {
		this.setLineNr(1);
		this.setFret(0);
		this.setCymbal(false);
		this.setClosed(false);
		this.setComment("dummy");
	}

	public NoteEof(Integer lineNr, Integer fret) {
		this.setLineNr(lineNr);
		this.setFret(fret);
		this.setCymbal(false);
		this.setClosed(false);
		this.setComment("LINE " + lineNr + "-" + fret);
	}

	public NoteEof(Integer lineNr, Boolean cymbal) {
		this.setLineNr(lineNr);
		this.setFret(0);
		this.setCymbal(cymbal);
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

	public Boolean getCymbal() {
		return cymbal;
	}

	public void setCymbal(Boolean cymbal) {
		this.cymbal = cymbal;
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
		return "NoteEof [lineNr=" + lineNr + ", fret=" + fret + ", cymbal=" + cymbal + ", closed=" + closed + ", comment=" + comment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closed == null) ? 0 : closed.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((cymbal == null) ? 0 : cymbal.hashCode());
		result = prime * result + ((fret == null) ? 0 : fret.hashCode());
		result = prime * result + ((lineNr == null) ? 0 : lineNr.hashCode());
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
		NoteEof other = (NoteEof) obj;
		if (closed == null) {
			if (other.closed != null)
				return false;
		} else if (!closed.equals(other.closed))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (cymbal == null) {
			if (other.cymbal != null)
				return false;
		} else if (!cymbal.equals(other.cymbal))
			return false;
		if (fret == null) {
			if (other.fret != null)
				return false;
		} else if (!fret.equals(other.fret))
			return false;
		if (lineNr == null) {
			if (other.lineNr != null)
				return false;
		} else if (!lineNr.equals(other.lineNr))
			return false;
		return true;
	}

	@Override
	public int compareTo(NoteEof other) {
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
