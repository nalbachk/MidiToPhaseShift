package com.my.converter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.Config;
import com.my.config.ConfigGuitarReal;
import com.my.config.NoteEof;

public class TrackConverterGuitarReal extends TrackConverter {

	private static Logger LOG = LogManager.getLogger(TrackConverterGuitarReal.class);

	protected ConfigGuitarReal configGuitarReal;

	public TrackConverterGuitarReal() {
		this.configGuitarReal = Config.readFromFile(ConfigGuitarReal.class);
	}

	@Override
	protected String getTrackName() {
		return "PART REAL_GUITAR_22";
	}

	public int getData1(NoteEof noteEof) {
		return 95 + noteEof.getLineNr();
	}

	public int getData2(NoteEof noteEof) {
		return 100 + noteEof.getFret();
	}

	@Override
	protected void modifyTrackNotes(Track track) {
		try {
			for (int index = 0; index < track.size(); index++) {
				MidiEvent midiEvent = track.get(index);
				MidiMessage midiMessage = midiEvent.getMessage();

				if (midiMessage instanceof ShortMessage) {
					ShortMessage shortMessage = (ShortMessage) midiMessage;

					switch (shortMessage.getCommand()) {
						case ShortMessage.NOTE_ON:
						case ShortMessage.NOTE_OFF:
							Integer noteMidi = shortMessage.getData1();
							NoteEof noteEof = configGuitarReal.getNoteEof(noteMidi);
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
}
