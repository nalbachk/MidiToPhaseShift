package com.my.midi;

import java.util.ArrayList;
import java.util.List;

import com.my.config.NotePhaseShift;

public class TabPosition {

	private List<NotePhaseShift> notes = null;

	public TabPosition(List<Integer> notesOnPosition) {
		notes = new ArrayList<NotePhaseShift>();

		for (int lineNr = 0; lineNr < notesOnPosition.size(); lineNr++) {
			Integer fret = notesOnPosition.get(lineNr);
			if (fret < 0) {
				notes.add(null);
			} else {
				NotePhaseShift note = new NotePhaseShift(lineNr + 1, fret);
				notes.add(note);
			}
		}
	}

	public List<NotePhaseShift> getNotes() {
		return notes;
	}

	public void setNotes(List<NotePhaseShift> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "TabPosition [notes=" + notes + "]";
	}
}
