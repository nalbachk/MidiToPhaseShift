package com.my.converter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.Config;
import com.my.config.NoteEof;
import com.my.config.NotePhaseShift;
import com.my.midi.TabPosition;
import com.my.midi.TabTrack;

public abstract class TrackConverterStringsReal extends TrackConverter {
	private static Logger LOG = LogManager.getLogger(TrackConverterStringsReal.class);

	public TrackConverterStringsReal(Config config) {
		super(config);
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
							NotePhaseShift notePhaseShift = config.getNotePhaseShift(noteMidi);
							NotePhaseShift notePhaseShiftFromTab = null;

							// try to find same note on other line from tab file
							if (null != tabTrack) {
								TabPosition tabPosition = tabTrack.getPositions().get(tabPositionIndex);
								notePhaseShiftFromTab = getNotePhaseShiftFromTab(tabPosition, notePhaseShift);

								// if not found in tab -> workaround -> try the next position
								if (null == notePhaseShiftFromTab) {
									int tabPositionIndexOrg = tabPositionIndex;
									do {
										if (tabPositionIndex++ > tabTrack.getPositions().size()) {
											LOG.error("tab track is wrong! please fix the .txt file! position: {}", tabPositionIndexOrg);
											break;
										}
										LOG.warn("tab track is wrong (long notes are often repeated in tab files)! try next position: {}", tabPositionIndex);

										tabPosition = tabTrack.getPositions().get(tabPositionIndex);
										notePhaseShiftFromTab = getNotePhaseShiftFromTab(tabPosition, notePhaseShift);
									} while (null == notePhaseShiftFromTab);
								}

								// take note from tab
								if (null != notePhaseShiftFromTab) {
									notePhaseShift = notePhaseShiftFromTab;
								}
							}

							NoteEof noteEof = config.getNoteEof(notePhaseShift);
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
		Integer midiNoteFromMapping = config.getNoteMidi(notePhaseShift);
		for (NotePhaseShift fromTab : tabPosition.getNotes()) {
			if (null != fromTab) {
				Integer midiNoteFromTab = config.getNoteMidi(fromTab);
				if (midiNoteFromTab == midiNoteFromMapping) { // midi note value is the same
					return fromTab;
				}
			}
		}

		// no tab note found
		return null;
	}
}
