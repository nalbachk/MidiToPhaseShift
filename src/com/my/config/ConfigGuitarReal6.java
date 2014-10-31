package com.my.config;

import java.io.Serializable;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement
public class ConfigGuitarReal6 extends ConfigStrings implements Serializable {
	private static Logger LOG = LogManager.getLogger(ConfigGuitarReal6.class);

	private static final long serialVersionUID = 1L;

	public ConfigGuitarReal6() {
		// nothing
	}

	@Override
	protected void initMapMidiToPhaseShift() {
		mapMidiToPhaseShift = new TreeMap<Integer, NotePhaseShift>();

		int lineNr;

		lineNr = 1; // string 1
		mapMidiToPhaseShift.put(40, new NotePhaseShift(lineNr, 0)); // E
		mapMidiToPhaseShift.put(41, new NotePhaseShift(lineNr, 1));
		mapMidiToPhaseShift.put(42, new NotePhaseShift(lineNr, 2));
		mapMidiToPhaseShift.put(43, new NotePhaseShift(lineNr, 3));
		mapMidiToPhaseShift.put(44, new NotePhaseShift(lineNr, 4));

		lineNr = 2; // string 2
		mapMidiToPhaseShift.put(45, new NotePhaseShift(lineNr, 0)); // A
		mapMidiToPhaseShift.put(46, new NotePhaseShift(lineNr, 1));
		mapMidiToPhaseShift.put(47, new NotePhaseShift(lineNr, 2));
		mapMidiToPhaseShift.put(48, new NotePhaseShift(lineNr, 3));
		mapMidiToPhaseShift.put(49, new NotePhaseShift(lineNr, 4));

		lineNr = 3; // string 3
		mapMidiToPhaseShift.put(50, new NotePhaseShift(lineNr, 0)); // D
		mapMidiToPhaseShift.put(51, new NotePhaseShift(lineNr, 1));
		mapMidiToPhaseShift.put(52, new NotePhaseShift(lineNr, 2));
		mapMidiToPhaseShift.put(53, new NotePhaseShift(lineNr, 3));
		mapMidiToPhaseShift.put(54, new NotePhaseShift(lineNr, 4));

		lineNr = 4; // string 4
		mapMidiToPhaseShift.put(55, new NotePhaseShift(lineNr, 0)); // G
		mapMidiToPhaseShift.put(56, new NotePhaseShift(lineNr, 1));
		mapMidiToPhaseShift.put(57, new NotePhaseShift(lineNr, 2));
		mapMidiToPhaseShift.put(58, new NotePhaseShift(lineNr, 3));

		lineNr = 5; // string 5
		mapMidiToPhaseShift.put(59, new NotePhaseShift(lineNr, 0)); // H
		mapMidiToPhaseShift.put(60, new NotePhaseShift(lineNr, 1));
		mapMidiToPhaseShift.put(61, new NotePhaseShift(lineNr, 2));
		mapMidiToPhaseShift.put(62, new NotePhaseShift(lineNr, 3));
		mapMidiToPhaseShift.put(63, new NotePhaseShift(lineNr, 4));

		lineNr = 6; // string 6
		mapMidiToPhaseShift.put(64, new NotePhaseShift(lineNr, 0)); // E
		mapMidiToPhaseShift.put(65, new NotePhaseShift(lineNr, 1));
		mapMidiToPhaseShift.put(66, new NotePhaseShift(lineNr, 2));
		mapMidiToPhaseShift.put(67, new NotePhaseShift(lineNr, 3));
		mapMidiToPhaseShift.put(68, new NotePhaseShift(lineNr, 4));

		mapMidiToPhaseShift.put(69, new NotePhaseShift(lineNr, 5));
		mapMidiToPhaseShift.put(70, new NotePhaseShift(lineNr, 6));
		mapMidiToPhaseShift.put(71, new NotePhaseShift(lineNr, 7));
		mapMidiToPhaseShift.put(72, new NotePhaseShift(lineNr, 8));
		mapMidiToPhaseShift.put(73, new NotePhaseShift(lineNr, 9));

		mapMidiToPhaseShift.put(74, new NotePhaseShift(lineNr, 10));
		mapMidiToPhaseShift.put(75, new NotePhaseShift(lineNr, 11));
		mapMidiToPhaseShift.put(76, new NotePhaseShift(lineNr, 12));
		mapMidiToPhaseShift.put(77, new NotePhaseShift(lineNr, 13));
		mapMidiToPhaseShift.put(78, new NotePhaseShift(lineNr, 14));

		mapMidiToPhaseShift.put(79, new NotePhaseShift(lineNr, 15));
		mapMidiToPhaseShift.put(80, new NotePhaseShift(lineNr, 16));
		mapMidiToPhaseShift.put(81, new NotePhaseShift(lineNr, 17));
		mapMidiToPhaseShift.put(82, new NotePhaseShift(lineNr, 18));
		mapMidiToPhaseShift.put(83, new NotePhaseShift(lineNr, 19));

		mapMidiToPhaseShift.put(84, new NotePhaseShift(lineNr, 20));
		mapMidiToPhaseShift.put(85, new NotePhaseShift(lineNr, 21));
		mapMidiToPhaseShift.put(86, new NotePhaseShift(lineNr, 22));

		LOG.info("mapMidiToPhaseShift initialized");
	}

	@Override
	public Integer getNoteMidi(NotePhaseShift notePhaseShift) {
		Integer noteMidi = 35 + notePhaseShift.getLineNr() * 5;
		if (notePhaseShift.getLineNr() > 4) {
			noteMidi -= 1;
		}
		noteMidi += notePhaseShift.getFret();
		return noteMidi;
	}
}
