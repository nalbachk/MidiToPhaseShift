package com.my.converter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.Config;
import com.my.midi.TabTrack;

public abstract class TrackConverter {
	private static Logger LOG = LogManager.getLogger(TrackConverter.class);

	public static int toUnsignedByte(byte b) {
		return (int) b & 0xff;
	}

	protected Config config = null;

	public TrackConverter(Config config) {
		this.config = config;
	}

	public void convert(Track trackMeta, Track midiTrack, TabTrack tabTrack) {
		if (null != tabTrack) {
			tabTrack.init();
		}
		this.modifyTrackName(midiTrack);
		this.modifyTrackNotes(midiTrack, tabTrack);
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

	protected abstract void modifyTrackNotes(Track midiTrack, TabTrack tabTrack);
}
