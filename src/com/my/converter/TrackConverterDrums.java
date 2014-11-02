package com.my.converter;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.Config;
import com.my.config.NoteEof;
import com.my.midi.TabTrack;

public class TrackConverterDrums extends TrackConverter {

	private static Logger LOG = LogManager.getLogger(TrackConverterDrums.class);

	public TrackConverterDrums(Config config) {
		super(config);
	}

	@Override
	protected String getTrackName() {
		return "PART DRUMS";
	}

	public int getData1(NoteEof noteEof) {
		if (1 == noteEof.getLineNr()) { // green
			return 96;
		} else if (2 == noteEof.getLineNr()) { // red
			return 97;
		} else if (3 == noteEof.getLineNr()) {
			if (!noteEof.getCymbal()) { // yellow
				return 110;
			} else { // yellow cymbal
				return 98;
			}
		} else if (4 == noteEof.getLineNr()) {
			if (!noteEof.getCymbal()) { // blue
				return 111;
			} else { // blue cymbal
				return 99;
			}
		} else if (5 == noteEof.getLineNr()) {
			if (!noteEof.getCymbal()) { // pink
				return 112;
			} else { // pink cymbal
				return 100;
			}
		}

		// map.put(NotesEof.ORANGE, 101); //TODO

		return 96;
	}

	@Override
	protected void modifyTrackNotes(Track midiTrack, TabTrack tabTrack) {
		List<MidiEvent> eventsToAdd = new ArrayList<MidiEvent>();

		int midiTrackSize = midiTrack.size();
		for (int index = 0; index < midiTrackSize; index++) {
			MidiEvent midiEvent = midiTrack.get(index);
			MidiMessage midiMessage = midiEvent.getMessage();

			if (midiMessage instanceof ShortMessage) {
				ShortMessage shortMessage = (ShortMessage) midiMessage;

				switch (shortMessage.getCommand()) {
					case ShortMessage.NOTE_ON:
					case ShortMessage.NOTE_OFF:
						Integer noteMidi = shortMessage.getData1();
						NoteEof noteEof = config.getNoteEof(noteMidi);
						this.modifyTrackNote(midiEvent, noteEof);

						// add additional message for non cymbals
						if (shortMessage.getData1() > 109) {
							try {
								int data1 = shortMessage.getData1() - 12;
								ShortMessage shortMessageAdd;
								shortMessageAdd = new ShortMessage(shortMessage.getCommand(), shortMessage.getChannel(), shortMessage.getData1(), shortMessage.getData2());
								shortMessageAdd.setMessage(shortMessage.getStatus(), data1, shortMessage.getData2());
								MidiEvent midiEventAdd = new MidiEvent(shortMessageAdd, midiEvent.getTick());
								eventsToAdd.add(midiEventAdd);
							} catch (InvalidMidiDataException e) {
								e.printStackTrace();
							}
						}
						break;
				}
			}
		}

		for (MidiEvent midiEvent : eventsToAdd) {
			midiTrack.add(midiEvent);
		}
	}

	public void modifyTrackNote(MidiEvent midiEvent, NoteEof noteEof) {
		ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
		Integer data1 = getData1(noteEof);
		Integer data2 = shortMessage.getData2();

		try {
			shortMessage.setMessage(shortMessage.getStatus(), data1, data2);
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackNotes");
		}
	}
}
