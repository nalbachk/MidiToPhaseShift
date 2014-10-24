package com.my.mapper;

import java.awt.Color;

/**
 * @author Administrator
 */
public enum NotesPhaseShift {
	DRUMS_BASS, //
	DRUMS_SNARE, //
	DRUMS_HIHAT, //
	DRUMS_TOM_1, //
	DRUMS_CC_1, //
	DRUMS_TOM_2, //
	DRUMS_CC_2, //
	DRUMS_TOM_3;

	public Integer getLineNumber() {
		switch (this) {
			case DRUMS_BASS:
				return 0;
			case DRUMS_SNARE:
				return 1;
			case DRUMS_HIHAT:
				return 2;
			case DRUMS_TOM_1:
				return 3;
			case DRUMS_CC_1:
				return 4;
			case DRUMS_TOM_2:
				return 5;
			case DRUMS_CC_2:
				return 6;
			case DRUMS_TOM_3:
				return 7;
			default:
				return 0;
		}
	}

	public Color getColor() {
		switch (this) {
			case DRUMS_BASS:
				return Color.WHITE;
			case DRUMS_SNARE:
				return Color.RED;
			case DRUMS_HIHAT:
				return Color.YELLOW;
			case DRUMS_TOM_1:
				return Color.BLUE;
			case DRUMS_CC_1:
				return Color.MAGENTA;
			case DRUMS_TOM_2:
				return Color.PINK;
			case DRUMS_CC_2:
				return Color.ORANGE;
			case DRUMS_TOM_3:
				return Color.GREEN;
			default:
				return Color.WHITE;
		}
	}
}
