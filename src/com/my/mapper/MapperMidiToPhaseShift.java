package com.my.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.phaseshift.Instruments;

public class MapperMidiToPhaseShift {
	private static Logger LOG = LogManager
			.getLogger(MapperMidiToPhaseShift.class);

	private static Map<Integer, NotesPhaseShift> map;

	// init
	static {
		map = new HashMap<Integer, NotesPhaseShift>();

		// TODO mapping 
		map.put(26, NotesPhaseShift.DRUMS_BASS);
		map.put(29, NotesPhaseShift.DRUMS_BASS);
		map.put(31, NotesPhaseShift.DRUMS_BASS);
		map.put(33, NotesPhaseShift.DRUMS_BASS);
		
		map.put(35, NotesPhaseShift.DRUMS_BASS);
		map.put(36, NotesPhaseShift.DRUMS_BASS);

		map.put(38, NotesPhaseShift.DRUMS_SNARE);
		map.put(40, NotesPhaseShift.DRUMS_SNARE);

		map.put(42, NotesPhaseShift.DRUMS_HIHAT);
		map.put(44, NotesPhaseShift.DRUMS_HIHAT);
		map.put(46, NotesPhaseShift.DRUMS_HIHAT);

		map.put(48, NotesPhaseShift.DRUMS_TOM_1);
		map.put(50, NotesPhaseShift.DRUMS_TOM_1);

		map.put(49, NotesPhaseShift.DRUMS_CC_1);
		map.put(51, NotesPhaseShift.DRUMS_CC_1);
		map.put(52, NotesPhaseShift.DRUMS_CC_1);
		map.put(53, NotesPhaseShift.DRUMS_CC_1);
		map.put(55, NotesPhaseShift.DRUMS_CC_1); // splash cymbal

		map.put(45, NotesPhaseShift.DRUMS_TOM_2);
		map.put(47, NotesPhaseShift.DRUMS_TOM_2);

		map.put(57, NotesPhaseShift.DRUMS_CC_2);
		map.put(59, NotesPhaseShift.DRUMS_CC_2);

		map.put(41, NotesPhaseShift.DRUMS_TOM_3);
		map.put(43, NotesPhaseShift.DRUMS_TOM_3);
	}

	public MapperMidiToPhaseShift(Instruments instrument) {

	}

	public NotesPhaseShift getNote(Integer noteMidi) {
		if (map.containsKey(noteMidi)) {
			return map.get(noteMidi);
		} else {
			LOG.error("no mapping found for noteMidi {}", noteMidi);
			return NotesPhaseShift.DRUMS_BASS;
		}
	}
}
