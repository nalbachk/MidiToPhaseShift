package com.my.config;

import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ConfigStrings extends Config {
	private static Logger LOG = LogManager.getLogger(ConfigStrings.class);

	public ConfigStrings() {
		// nothing
	}

	protected Integer getStringsCount() {
		return 6;
	}

	protected Integer getFretsCount() {
		return 23;
	}

	protected void initMapPhaseShiftToEof() {
		mapPhaseShiftToEof = new TreeMap<NotePhaseShift, NoteEof>();

		for (int lineNr = 1; lineNr <= getStringsCount(); lineNr++) {
			for (int fret = 0; fret < getFretsCount(); fret++) {
				mapPhaseShiftToEof.put(new NotePhaseShift(lineNr, fret), new NoteEof(lineNr, fret));
			}
		}

		LOG.info("mapPhaseShiftToEof initialized");
	}
}
