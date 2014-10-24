package com.my.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MapperPhaseShiftToEof {

	private static final Logger LOG = Logger.getLogger(MapperPhaseShiftToEof.class.getName());

	private static Map<NotesPhaseShift, NotesEof> map;

	// init
	static {
		map = new HashMap<NotesPhaseShift, NotesEof>();

		map.put(NotesPhaseShift.DRUMS_BASS, NotesEof.GREEN);
		map.put(NotesPhaseShift.DRUMS_SNARE, NotesEof.RED);
		map.put(NotesPhaseShift.DRUMS_HIHAT, NotesEof.YELLOW_CY);
		map.put(NotesPhaseShift.DRUMS_TOM_1, NotesEof.YELLOW);
		map.put(NotesPhaseShift.DRUMS_CC_1, NotesEof.BLUE_CY); //TODO not working
		map.put(NotesPhaseShift.DRUMS_TOM_2, NotesEof.BLUE);
		map.put(NotesPhaseShift.DRUMS_CC_2, NotesEof.PINK_CY);
		map.put(NotesPhaseShift.DRUMS_TOM_3, NotesEof.PINK);
		//map.put(NotesPhaseShift.DRUMS_TOM_3, NotesEof.ORANGE);

	}

	public static NotesEof getNote(NotesPhaseShift notePhaseShift) {
		if (map.containsKey(notePhaseShift)) {
			return map.get(notePhaseShift);
		} else {
			LOG.info("no mapping found: " + notePhaseShift.name());
			return NotesEof.GREEN;
		}
	}
}
