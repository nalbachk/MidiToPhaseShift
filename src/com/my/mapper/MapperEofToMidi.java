package com.my.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Administrator
 */
public class MapperEofToMidi {

	private static final Logger LOG = Logger.getLogger(MapperEofToMidi.class.getName());

	private static Map<NotesEof, Integer> map;

	// init
	static {
		map = new HashMap<NotesEof, Integer>();
		map.put(NotesEof.GREEN, 96);
		map.put(NotesEof.RED, 97);
		map.put(NotesEof.YELLOW, 110); // 98
		map.put(NotesEof.BLUE, 111); // 99
		map.put(NotesEof.PINK, 112); // 100
		// map.put(NotesEof.ORANGE, 101); //TODO
		map.put(NotesEof.YELLOW_CY, 98);
		map.put(NotesEof.BLUE_CY, 99);
		map.put(NotesEof.PINK_CY, 100);
	}

	public static Integer getNote(NotesEof noteEof) {
		if (map.containsKey(noteEof)) {
			return map.get(noteEof);
		} else {
			LOG.info("no mapping found: " + noteEof.name());
			return 96;
		}
	}
}
