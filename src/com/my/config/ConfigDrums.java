package com.my.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement
public class ConfigDrums extends Config implements Serializable {
	private static Logger LOG = LogManager.getLogger(ConfigDrums.class);

	private static final long serialVersionUID = 1L;

	@XmlElement
	protected Map<Integer, NotePhaseShift> mapMidiToPhaseShift = null;

	@XmlElement
	protected Map<NotePhaseShift, NoteEof> mapPhaseShiftToEof = null;

	public ConfigDrums() {
		this.initMapMidiToPhaseShift();
		this.initMapPhaseShiftToEof();
	}

	public NoteEof getNoteEof(Integer noteMidi) {
		NotePhaseShift notePhaseShift = null;
		if (mapMidiToPhaseShift.containsKey(noteMidi)) {
			notePhaseShift = mapMidiToPhaseShift.get(noteMidi);
		} else {
			LOG.error("no config found for noteMidi: {}", noteMidi);
			notePhaseShift = new NotePhaseShift(0); // bass
		}

		NoteEof noteEof = null;
		if (mapPhaseShiftToEof.containsKey(notePhaseShift)) {
			noteEof = mapPhaseShiftToEof.get(notePhaseShift);
		} else {
			LOG.error("no config found for notePhaseShift: {}", notePhaseShift);
			noteEof = new NoteEof(1, false); // green
		}

		return noteEof;
	}

	private void initMapMidiToPhaseShift() {
		mapMidiToPhaseShift = new HashMap<Integer, NotePhaseShift>();

		mapMidiToPhaseShift.put(26, new NotePhaseShift(0)); // bass
		mapMidiToPhaseShift.put(29, new NotePhaseShift(0)); // bass
		mapMidiToPhaseShift.put(31, new NotePhaseShift(0)); // bass
		mapMidiToPhaseShift.put(33, new NotePhaseShift(0)); // bass
		mapMidiToPhaseShift.put(35, new NotePhaseShift(0)); // bass
		mapMidiToPhaseShift.put(36, new NotePhaseShift(0)); // bass

		mapMidiToPhaseShift.put(38, new NotePhaseShift(1)); // snare
		mapMidiToPhaseShift.put(40, new NotePhaseShift(1)); // snare

		mapMidiToPhaseShift.put(42, new NotePhaseShift(2)); // hihat
		mapMidiToPhaseShift.put(44, new NotePhaseShift(2)); // hihat
		mapMidiToPhaseShift.put(46, new NotePhaseShift(2)); // hihat

		mapMidiToPhaseShift.put(48, new NotePhaseShift(3)); // tom 1
		mapMidiToPhaseShift.put(50, new NotePhaseShift(3)); // tom 1

		mapMidiToPhaseShift.put(49, new NotePhaseShift(4)); // cymbal 1
		mapMidiToPhaseShift.put(51, new NotePhaseShift(4)); // cymbal 1
		mapMidiToPhaseShift.put(52, new NotePhaseShift(4)); // cymbal 1
		mapMidiToPhaseShift.put(53, new NotePhaseShift(4)); // cymbal 1
		mapMidiToPhaseShift.put(55, new NotePhaseShift(4)); // cymbal 1

		mapMidiToPhaseShift.put(45, new NotePhaseShift(5)); // tom 2
		mapMidiToPhaseShift.put(47, new NotePhaseShift(5)); // tom 2

		mapMidiToPhaseShift.put(57, new NotePhaseShift(6)); // cymbal 2
		mapMidiToPhaseShift.put(59, new NotePhaseShift(6)); // cymbal 2

		mapMidiToPhaseShift.put(41, new NotePhaseShift(7)); // tom 3
		mapMidiToPhaseShift.put(43, new NotePhaseShift(7)); // tom 3
	}

	private void initMapPhaseShiftToEof() {
		mapPhaseShiftToEof = new HashMap<NotePhaseShift, NoteEof>();

		mapPhaseShiftToEof.put(new NotePhaseShift(0), new NoteEof(1, false)); // bass > green
		mapPhaseShiftToEof.put(new NotePhaseShift(1), new NoteEof(2, false)); // snare > red
		mapPhaseShiftToEof.put(new NotePhaseShift(2), new NoteEof(3, true)); // hihat > yellow cymbal
		mapPhaseShiftToEof.put(new NotePhaseShift(3), new NoteEof(3, false)); // tom 1 > yellow
		mapPhaseShiftToEof.put(new NotePhaseShift(4), new NoteEof(4, true)); // cymbal 1 > blue cymbal
		mapPhaseShiftToEof.put(new NotePhaseShift(5), new NoteEof(4, false)); // tom 2 > blue
		mapPhaseShiftToEof.put(new NotePhaseShift(6), new NoteEof(5, true)); // cymbal 2 > pink cymbal
		mapPhaseShiftToEof.put(new NotePhaseShift(7), new NoteEof(5, false)); // tom 3 > pink
		//mapPhaseShiftToEof.put(new NotePhaseShift(7), new NoteEof(6)); // ? > orange
	}
}