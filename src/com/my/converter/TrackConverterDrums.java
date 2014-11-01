package com.my.converter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.ConfigDrums;
import com.my.config.ConfigFile;
import com.my.config.NoteEof;
import com.my.midi.TabTrack;

public class TrackConverterDrums extends TrackConverter {

	private static Logger LOG = LogManager.getLogger(TrackConverterDrums.class);

	protected ConfigDrums configDrums;

	public TrackConverterDrums() {
		this.configDrums = ConfigFile.read(ConfigDrums.class);
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
		try {
			for (int index = 0; index < midiTrack.size(); index++) {
				MidiEvent midiEvent = midiTrack.get(index);
				MidiMessage midiMessage = midiEvent.getMessage();

				if (midiMessage instanceof ShortMessage) {
					ShortMessage shortMessage = (ShortMessage) midiMessage;

					switch (shortMessage.getCommand()) {
						case ShortMessage.NOTE_ON:
						case ShortMessage.NOTE_OFF:
							Integer noteMidi = shortMessage.getData1();
							NoteEof noteEof = configDrums.getNoteEof(noteMidi);
							Integer data1 = getData1(noteEof);
							Integer data2 = shortMessage.getData2();

							shortMessage.setMessage(shortMessage.getStatus(), data1, data2);
							break;
					}
				}
			}
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackNotes");
		}
	}
}
