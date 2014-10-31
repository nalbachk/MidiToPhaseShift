package com.my.config;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ConfigStrings extends Config implements Serializable {
	private static Logger LOG = LogManager.getLogger(ConfigStrings.class);

	private static final long serialVersionUID = 1L;

	@XmlElement
	protected Integer tuning = 0;

	@XmlElement
	protected Map<Integer, NotePhaseShift> mapMidiToPhaseShift = null;

	@XmlElement
	protected Map<NotePhaseShift, NoteEof> mapPhaseShiftToEof = null;

	public ConfigStrings() {
		this.initMapMidiToPhaseShift();
		this.initMapPhaseShiftToEof();
	}

	protected Integer getStringsCount() {
		return 6;
	}

	protected Integer getFretsCount() {
		return 23;
	}

	abstract protected void initMapMidiToPhaseShift();

	protected void initMapPhaseShiftToEof() {
		mapPhaseShiftToEof = new TreeMap<NotePhaseShift, NoteEof>();

		for (int lineNr = 1; lineNr < getStringsCount(); lineNr++) {
			for (int fret = 0; fret < getFretsCount(); fret++) {
				mapPhaseShiftToEof.put(new NotePhaseShift(lineNr, fret), new NoteEof(lineNr, fret));
			}
		}
	}

	abstract public Integer getNoteMidi(NotePhaseShift notePhaseShift);

	public NotePhaseShift getNotePhaseShift(Integer noteMidi) {
		noteMidi -= tuning;

		NotePhaseShift notePhaseShift = null;
		if (mapMidiToPhaseShift.containsKey(noteMidi)) {
			notePhaseShift = mapMidiToPhaseShift.get(noteMidi);
		} else {
			LOG.error("no config found for noteMidi: {}", noteMidi);
			notePhaseShift = new NotePhaseShift(1, 5);
		}

		return notePhaseShift;
	}

	public NoteEof getNoteEof(NotePhaseShift notePhaseShift) {
		NoteEof noteEof = null;
		if (mapPhaseShiftToEof.containsKey(notePhaseShift)) {
			noteEof = mapPhaseShiftToEof.get(notePhaseShift);
		} else {
			LOG.error("no config found for notePhaseShift: {}", notePhaseShift);
			noteEof = new NoteEof(1, 5);
		}

		return noteEof;
	}

	public NoteEof getNoteEof(Integer noteMidi) {
		NotePhaseShift notePhaseShift = this.getNotePhaseShift(noteMidi);
		NoteEof noteEof = this.getNoteEof(notePhaseShift);
		return noteEof;
	}
}
