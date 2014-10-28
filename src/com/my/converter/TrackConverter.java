package com.my.converter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TrackConverter {
	private static Logger LOG = LogManager.getLogger(TrackConverter.class);

	public void convert(Track track) {
		this.modifyTrackName(track);
		this.modifyTrackNotes(track);
		this.modifyTrackBpm(track, 200);
	}

	protected void modifyTrackName(Track track) {
		String trackName = this.getTrackName();

		try {
			boolean trackNameReplaced = false;
			for (int index = 0; index < track.size(); index++) {
				MidiEvent event = track.get(index);
				MidiMessage midiMessage = event.getMessage();

				if (midiMessage instanceof MetaMessage) {
					MetaMessage metaMessage = (MetaMessage) midiMessage;

					if (0x03 == metaMessage.getType()) {
						metaMessage.setMessage(0x03, trackName.getBytes(), trackName.length());
						trackNameReplaced = true;
						break;
					}
				}
			}

			if (!trackNameReplaced) {
				MetaMessage metaMessage = new MetaMessage();
				metaMessage.setMessage(0x03, trackName.getBytes(), trackName.length());
				MidiEvent midiEvent = new MidiEvent(metaMessage, 0L);
				track.add(midiEvent);
			}
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackName to {}", trackName);
		}
	}

	protected abstract String getTrackName();

	protected abstract void modifyTrackNotes(Track track);

	public void modifyTrackBpm(Track track, int bpm) {
		int tempo = (60 * 1000 * 1000) / bpm;
		byte[] byteTempo = new byte[] {
				(byte) (tempo / 65536), (byte) (tempo % 65536 / 256), (byte) (tempo % 256) };

		try {
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
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackBpm to {}", bpm);
		}
	}
}
