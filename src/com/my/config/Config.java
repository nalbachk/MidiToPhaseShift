package com.my.config;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Config {
	private static Logger LOG = LogManager.getLogger(Config.class);

	@XmlElement
	private Double bpmMultiplier = 0.92D;

	@XmlElement
	private Integer midiEventPerStateMin = 25;

	@XmlElement
	private Integer tuning = 0;

	@XmlElement
	protected Map<Integer, NotePhaseShift> mapMidiToPhaseShift = null;

	@XmlElement
	protected Map<NotePhaseShift, NoteEof> mapPhaseShiftToEof = null;

	public Config() {
		this.initMapMidiToPhaseShift();
		this.initMapPhaseShiftToEof();
	}

	abstract protected void initMapMidiToPhaseShift();

	abstract protected void initMapPhaseShiftToEof();

	public NoteEof getNoteEof(Integer noteMidi) {
		NotePhaseShift notePhaseShift = this.getNotePhaseShift(noteMidi);
		NoteEof noteEof = this.getNoteEof(notePhaseShift);
		return noteEof;
	}

	public NotePhaseShift getNotePhaseShift(Integer noteMidi) {
		noteMidi -= this.getTuning();

		NotePhaseShift notePhaseShift = null;
		if (mapMidiToPhaseShift.containsKey(noteMidi)) {
			notePhaseShift = mapMidiToPhaseShift.get(noteMidi);
		} else {
			LOG.error("no config found for noteMidi: {}", noteMidi);
			notePhaseShift = this.getNotePhaseShiftDefault();
		}

		return notePhaseShift;
	}

	protected NotePhaseShift getNotePhaseShiftDefault() {
		return new NotePhaseShift(1, 5);
	}

	public NoteEof getNoteEof(NotePhaseShift notePhaseShift) {
		NoteEof noteEof = null;
		if (mapPhaseShiftToEof.containsKey(notePhaseShift)) {
			noteEof = mapPhaseShiftToEof.get(notePhaseShift);
		} else {
			LOG.error("no config found for notePhaseShift: {}", notePhaseShift);
			noteEof = this.getNoteEofDefault();
		}

		return noteEof;
	}

	protected NoteEof getNoteEofDefault() {
		return new NoteEof(1, 5);
	}

	abstract public Integer getNoteMidi(NotePhaseShift notePhaseShift);

	public Double getBpmMultiplier() {
		return bpmMultiplier;
	}

	public void setBpmMultiplier(Double bpmMultiplier) {
		this.bpmMultiplier = bpmMultiplier;
	}

	public Integer getMidiEventsPerStateMin() {
		return midiEventPerStateMin;
	}

	public void setMidiEventsPerStateMin(Integer midiEventPerStateMin) {
		this.midiEventPerStateMin = midiEventPerStateMin;
	}

	public Integer getTuning() {
		return tuning;
	}

	public void setTuning(Integer tuning) {
		this.tuning = tuning;
	}
}
