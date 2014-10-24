package com.my.midi;


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;

import javax.sound.midi.Track;

/**
 * @author Administrator
 */
public class MidiHelper {


	public void replaceTrackBpm(Track track, int bpm) throws InvalidMidiDataException {
		int tempo = (60 * 1000 * 1000) / bpm;
		byte[] byteTempo = new byte[] {
				(byte) (tempo / 65536), (byte) (tempo % 65536 / 256), (byte) (tempo % 256) };

		boolean trackTempoReplaced = false;
		for (int index = 0; index < track.size(); index++) {
			MidiEvent event = track.get(index);
			MidiMessage midiMessage = event.getMessage();

			if (midiMessage instanceof MetaMessage) {
				MetaMessage metaMessage = (MetaMessage) midiMessage;

				if (0x51 == metaMessage.getType()) {
					metaMessage.setMessage(0x51, byteTempo, 3);
					trackTempoReplaced = true;
					break;
				}
			}
		}

		if (!trackTempoReplaced) {
			MetaMessage metaMessage = new MetaMessage();
			metaMessage.setMessage(0x51, byteTempo, 3);
			MidiEvent midiEvent = new MidiEvent(metaMessage, 0L);
			track.add(midiEvent);
		}
	}
}
