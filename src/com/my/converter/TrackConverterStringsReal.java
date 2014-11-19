package com.my.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			removeMidiEvents(midiTrack);

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
								TabPosition tabPosition = tabTrack.getPositionByIndex(tabPositionIndex);
								notePhaseShiftFromTab = getNotePhaseShiftFromTab(tabPosition, notePhaseShift);

								// if not found in tab -> workaround -> try the next position
								if (null == notePhaseShiftFromTab) {
									int tabPositionIndexOrg = tabPositionIndex;
									do {
										tabPositionIndex++;
										LOG.warn("tab track is wrong (long notes are often repeated in tab files)! tick: {} {} on orgiginal position: {} try next: {}", lastTick, noteMidi, tabPositionIndexOrg, tabPositionIndex);

										tabPosition = tabTrack.getPositionByIndex(tabPositionIndex);
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

	private void removeMidiEvents(Track midiTrack) {
		List<MidiEvent> midiEventsToRemove = new ArrayList<MidiEvent>();

		midiEventsToRemove.addAll(this.getMidiEventsToRemoveByStatus(midiTrack));

		LOG.info("midiEventsToRemove: {}", midiEventsToRemove.size());
		for (MidiEvent midiEvent : midiEventsToRemove) {
			midiTrack.remove(midiEvent);
		}
	}

	private List<MidiEvent> getMidiEventsToRemoveByStatus(Track midiTrack) {
		Map<Integer, List<MidiEvent>> midiEventsPerStatus = new HashMap<>();

		for (int index = 0; index < midiTrack.size(); index++) {
			MidiEvent midiEvent = midiTrack.get(index);
			MidiMessage midiMessage = midiEvent.getMessage();

			if (midiMessage instanceof ShortMessage) {
				ShortMessage shortMessage = (ShortMessage) midiMessage;

				switch (shortMessage.getCommand()) {
					case ShortMessage.NOTE_ON:
						// break;
					case ShortMessage.NOTE_OFF:
						Integer status = shortMessage.getStatus();
						if (!midiEventsPerStatus.containsKey(status)) {
							midiEventsPerStatus.put(status, new ArrayList<MidiEvent>());
						}
						midiEventsPerStatus.get(status).add(midiEvent);
						break;
				}
			}
		}

		// if less then X midi events with same state => remove them
		List<MidiEvent> midiEventsToRemove = new ArrayList<MidiEvent>();
		for (Integer status : midiEventsPerStatus.keySet()) {
			List<MidiEvent> midiEvents = midiEventsPerStatus.get(status);
			if (midiEvents.size() < config.getMidiEventsPerStateMin()) {
				midiEventsToRemove.addAll(midiEvents);
				LOG.warn("{} notes found with status: {} (they will be removed - see config)", midiEvents.size(), status);
			} else {
				LOG.info("{} notes found with status: {}", midiEvents.size(), status);
			}
		}

		return midiEventsToRemove;
	}
}
