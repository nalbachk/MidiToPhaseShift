package com.my.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement
public class ConfigGuitarReal extends Config implements Serializable {
	private static Logger LOG = LogManager.getLogger(ConfigGuitarReal.class);

	private static final long serialVersionUID = 1L;

	@XmlElement
	protected Map<Integer, NotePhaseShift> mapMidiToPhaseShift = null;

	@XmlElement
	protected Map<NotePhaseShift, NoteEof> mapPhaseShiftToEof = null;

	public ConfigGuitarReal() {
		this.initMapMidiToPhaseShift();
		this.initMapPhaseShiftToEof();
	}

	public NoteEof getNoteEof(Integer noteMidi) {
		NotePhaseShift notePhaseShift = null;
		if (mapMidiToPhaseShift.containsKey(noteMidi)) {
			notePhaseShift = mapMidiToPhaseShift.get(noteMidi);
		} else {
			LOG.error("no config found for noteMidi: {}", noteMidi);
			notePhaseShift = new NotePhaseShift(1, 5);
		}

		NoteEof noteEof = null;
		if (mapPhaseShiftToEof.containsKey(notePhaseShift)) {
			noteEof = mapPhaseShiftToEof.get(notePhaseShift);
		} else {
			LOG.error("no config found for notePhaseShift: {}", notePhaseShift);
			noteEof = new NoteEof(1, 5);
		}

		return noteEof;
	}

	private void initMapMidiToPhaseShift() {
		mapMidiToPhaseShift = new HashMap<Integer, NotePhaseShift>();

		mapMidiToPhaseShift.put(40, new NotePhaseShift(1, 0));
		mapMidiToPhaseShift.put(41, new NotePhaseShift(1, 1));
		mapMidiToPhaseShift.put(42, new NotePhaseShift(1, 2));
		mapMidiToPhaseShift.put(43, new NotePhaseShift(1, 3));
		mapMidiToPhaseShift.put(44, new NotePhaseShift(1, 4));

		mapMidiToPhaseShift.put(45, new NotePhaseShift(2, 0));
		mapMidiToPhaseShift.put(46, new NotePhaseShift(2, 1));
		mapMidiToPhaseShift.put(47, new NotePhaseShift(2, 2));
		mapMidiToPhaseShift.put(48, new NotePhaseShift(2, 3));
		mapMidiToPhaseShift.put(49, new NotePhaseShift(2, 4));

		mapMidiToPhaseShift.put(50, new NotePhaseShift(3, 0));
		mapMidiToPhaseShift.put(51, new NotePhaseShift(3, 1));
		mapMidiToPhaseShift.put(52, new NotePhaseShift(3, 2));
		mapMidiToPhaseShift.put(53, new NotePhaseShift(3, 3));
		mapMidiToPhaseShift.put(54, new NotePhaseShift(3, 4));

		mapMidiToPhaseShift.put(55, new NotePhaseShift(4, 0));
		mapMidiToPhaseShift.put(56, new NotePhaseShift(4, 1));
		mapMidiToPhaseShift.put(57, new NotePhaseShift(4, 2));
		mapMidiToPhaseShift.put(58, new NotePhaseShift(4, 3));
		mapMidiToPhaseShift.put(59, new NotePhaseShift(4, 4));

		mapMidiToPhaseShift.put(60, new NotePhaseShift(5, 0));
		mapMidiToPhaseShift.put(61, new NotePhaseShift(5, 1));
		mapMidiToPhaseShift.put(62, new NotePhaseShift(5, 2));
		mapMidiToPhaseShift.put(63, new NotePhaseShift(5, 3));
		mapMidiToPhaseShift.put(64, new NotePhaseShift(5, 4));

		mapMidiToPhaseShift.put(65, new NotePhaseShift(6, 0));
		mapMidiToPhaseShift.put(66, new NotePhaseShift(6, 1));
		mapMidiToPhaseShift.put(67, new NotePhaseShift(6, 2));
		mapMidiToPhaseShift.put(68, new NotePhaseShift(6, 3));
		mapMidiToPhaseShift.put(69, new NotePhaseShift(6, 4));

		mapMidiToPhaseShift.put(70, new NotePhaseShift(6, 5));
		mapMidiToPhaseShift.put(71, new NotePhaseShift(6, 6));
		mapMidiToPhaseShift.put(72, new NotePhaseShift(6, 7));
		mapMidiToPhaseShift.put(73, new NotePhaseShift(6, 8));
		mapMidiToPhaseShift.put(74, new NotePhaseShift(6, 9));

		mapMidiToPhaseShift.put(75, new NotePhaseShift(6, 10));
		mapMidiToPhaseShift.put(76, new NotePhaseShift(6, 11));
		mapMidiToPhaseShift.put(77, new NotePhaseShift(6, 12));
		mapMidiToPhaseShift.put(78, new NotePhaseShift(6, 13));
		mapMidiToPhaseShift.put(79, new NotePhaseShift(6, 14));

		mapMidiToPhaseShift.put(80, new NotePhaseShift(6, 15));
		mapMidiToPhaseShift.put(81, new NotePhaseShift(6, 16));
		mapMidiToPhaseShift.put(82, new NotePhaseShift(6, 17));
		mapMidiToPhaseShift.put(83, new NotePhaseShift(6, 18));
		mapMidiToPhaseShift.put(84, new NotePhaseShift(6, 19));

		mapMidiToPhaseShift.put(85, new NotePhaseShift(6, 20));
		mapMidiToPhaseShift.put(86, new NotePhaseShift(6, 21));
		mapMidiToPhaseShift.put(87, new NotePhaseShift(6, 22));
	}

	private void initMapPhaseShiftToEof() {
		mapPhaseShiftToEof = new HashMap<NotePhaseShift, NoteEof>();

		for (int lineNr = 1; lineNr < 7; lineNr++) {
			for (int fret = 0; fret < 23; fret++) {
				mapPhaseShiftToEof.put(new NotePhaseShift(lineNr, fret), new NoteEof(lineNr, fret));
			}
		}
	}
}
