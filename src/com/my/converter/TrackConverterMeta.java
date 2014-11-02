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

public class TrackConverterMeta extends TrackConverter {
	private static Logger LOG = LogManager.getLogger(TrackConverterMeta.class);

	public TrackConverterMeta(Config config) {
		super(config);
	}

	@Override
	protected String getTrackName() {
		return null; // unused
	}

	@Override
	protected void modifyTrackNotes(Track midiTrack, TabTrack tabTrack) {
		// unused
	}

	public void convert(Track trackMeta) {
		this.modifyTrackBpm(trackMeta);
	}

	// try to fix the bpm difference
	public void modifyTrackBpm(Track trackMeta) {
		for (int index = 0; index < trackMeta.size(); index++) {
			MidiEvent midiEvent = trackMeta.get(index);
			MidiMessage midiMessage = midiEvent.getMessage();

			if (midiMessage instanceof MetaMessage) {
				MetaMessage metaMessage = (MetaMessage) midiMessage;

				if (0x51 == metaMessage.getType()) { // 81
					double bpmOld = this.toBpm(metaMessage);
					double bpmNew = bpmOld * config.getBpmMultiplier();
					LOG.info("change bpm value from: {} to: {}", bpmOld, bpmNew);
					this.changeBpm(metaMessage, bpmNew);
				}
			}
		}
	}

	private double toBpm(MetaMessage metaMessage) {
		int microSecondsPerQuarterNode = 0;
		microSecondsPerQuarterNode += TrackConverter.toUnsignedByte(metaMessage.getData()[0]) * 256 * 256;
		microSecondsPerQuarterNode += TrackConverter.toUnsignedByte(metaMessage.getData()[1]) * 256;
		microSecondsPerQuarterNode += TrackConverter.toUnsignedByte(metaMessage.getData()[2]);
		double bpm = (60 * 1000 * 1000) / microSecondsPerQuarterNode;
		return bpm;
	}

	private void changeBpm(MetaMessage metaMessage, double bpm) {
		int microSecondsPerQuarterNode = (int) ((60 * 1000 * 1000) / bpm);
		byte[] data = new byte[] {
				(byte) (microSecondsPerQuarterNode / 256 / 256), //
				(byte) (microSecondsPerQuarterNode % (256 * 256) / 256), //
				(byte) (microSecondsPerQuarterNode % 256) };
		try {
			metaMessage.setMessage(0x51, data, 3);
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify bpm", e);
		}
	}
}
