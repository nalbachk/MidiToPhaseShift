package com.my.converter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.ConfigStrings;
import com.my.config.NoteEof;
import com.my.config.NotePhaseShift;
import com.my.midi.TabPosition;
import com.my.midi.TabTrack;

public abstract class TrackConverterStringsReal extends TrackConverter {
	private static Logger LOG = LogManager.getLogger(TrackConverterStringsReal.class);

	protected ConfigStrings configStrings = null;

	public TrackConverterStringsReal(ConfigStrings configStrings) {
		this.configStrings = configStrings;
	}

	@Override
	protected void modifyTrackNotes(Track midiTrack, TabTrack tabTrack) {
		try {
			long lastTick = -1;
			int tabPositionIndex = -1;

			for (int index = 0; index < midiTrack.size(); index++) {
				MidiEvent midiEvent = midiTrack.get(index);
				MidiMessage midiMessage = midiEvent.getMessage();

				if (midiMessage instanceof ShortMessage) {
					ShortMessage shortMessage = (ShortMessage) midiMessage;

					switch (shortMessage.getCommand()) {
						case ShortMessage.NOTE_ON:
							// position for compare with tabTrack
							if (midiEvent.getTick() != lastTick) {
								lastTick = midiEvent.getTick();
								tabPositionIndex++;
							}
							// break;
						case ShortMessage.NOTE_OFF:
							Integer noteMidi = shortMessage.getData1();
							NotePhaseShift notePhaseShift = configStrings.getNotePhaseShift(noteMidi);

							// try to find same note on other line from tab file
							if (null != tabTrack) {
								TabPosition tabPosition = tabTrack.getPositions().get(tabPositionIndex);
								notePhaseShift = getNotePhaseShiftFromTab(tabPosition, notePhaseShift);
							}

							NoteEof noteEof = configStrings.getNoteEof(notePhaseShift);
							Integer data1 = getData1(noteEof);
							Integer data2 = getData2(noteEof);
							shortMessage.setMessage(shortMessage.getStatus(), data1, data2);
							break;
					}
				}
			}
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackNotes");
		}
	}

	public int getData1(NoteEof noteEof) {
		return 95 + noteEof.getLineNr();
	}

	public int getData2(NoteEof noteEof) {
		return 100 + noteEof.getFret();
	}

	protected NotePhaseShift getNotePhaseShiftFromTab(TabPosition tabPosition, NotePhaseShift notePhaseShift) {
		Integer midiNoteFromMapping = configStrings.getNoteMidi(notePhaseShift);
		for (NotePhaseShift fromTab : tabPosition.getNotes()) {
			if (null != fromTab) {
				Integer midiNoteFromTab = configStrings.getNoteMidi(fromTab);
				if (midiNoteFromTab == midiNoteFromMapping) { // midi note value is the same
					return fromTab;
				}
			}
		}
		return notePhaseShift;
	}
}
