package com.my.converter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.mapper.MapperEofToMidi;
import com.my.mapper.MapperMidiToPhaseShift;
import com.my.mapper.MapperPhaseShiftToEof;
import com.my.mapper.NotesEof;
import com.my.mapper.NotesPhaseShift;
import com.my.midi.MidiFile;
import com.my.midi.MidiHelper;
import com.my.midi.MidiLogger;
import com.my.phaseshift.Instruments;

public class Converter {
	private static Logger LOG = LogManager.getLogger(Converter.class);

	protected MidiHelper midiHelper = new MidiHelper();

	public void convertFiles() {
		File directory = new File("midi");
		List<File> files = this.findFilesMidi(directory);
		LOG.info("{} midiFiles found in {}", files.size(),
				directory.getAbsolutePath());

		for (File file : files) {
			convertFile(file);
		}
	}

	protected List<File> findFilesMidi(File directory) {
		File[] array = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".mid")) {
					return true;
				}
				return false;
			}
		});

		List<File> files = Arrays.asList(array);
		return files;
	}

	protected void convertFile(File file) {
		LOG.info("convert midiFile {}", file.getAbsolutePath());
		try {
			MidiFile midiFile = new MidiFile(file);
			this.modifyMidi(midiFile);
			this.writeMidiNew(midiFile);
		} catch (InvalidMidiDataException | IOException e) {
			LOG.error("cannot read midiFile '{}'", file.getAbsolutePath(), e);
		}
	}

	protected void writeMidiNew(MidiFile midiFile) {
		// create sub directory
		String strPath = midiFile.getFilePath();
		strPath += midiFile.getSongArtist() + " - " + midiFile.getSongTitle()
				+ System.getProperty("file.separator");
		File path = new File(strPath);
		path.mkdirs();

		// create new midi file
		File fileMidiNew = new File(strPath + "midi.mid");
		int fileType = MidiSystem.getMidiFileTypes(midiFile.getSequence())[0];
		try {
			MidiSystem.write(midiFile.getSequence(), fileType, fileMidiNew);
			LOG.info("midi conversion completed '{}'",
					fileMidiNew.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("midi conversion failed", e);
		}
	}

	protected void modifyMidi(MidiFile midiFile) {
		Set<Integer> trackIndices = midiFile.getInstruments().keySet();

		// modify tracks
		List<Track> tracksToDelete = new ArrayList<Track>();
		for (int trackIndex = 0; trackIndex < midiFile.getTracks().size(); trackIndex++) {
			Track track = midiFile.getSequence().getTracks()[trackIndex];
			if (trackIndices.contains(trackIndex)) {
				Instruments instrument = midiFile.getInstruments().get(
						trackIndex);
				LOG.info("track with index {} will be modified for {}",
						trackIndex, instrument);
				this.modifyTrack(track, instrument);
			} else {
				LOG.info("track with index {} will be removed", trackIndex);
				tracksToDelete.add(track);
			}
		}

		for (Track track : tracksToDelete) {
			midiFile.getSequence().deleteTrack(track);
		}
	}

	protected void modifyTrack(Track track, Instruments instrument) {
		MidiLogger.logTrack(track);

		this.modifyTrackName(track, instrument.getTrackName());
		this.modifyTrackNotes(track, instrument);

		MidiLogger.logTrack(track);
	}

	protected void modifyTrackName(Track track, String trackName) {
		try {
			boolean trackNameReplaced = false;
			for (int index = 0; index < track.size(); index++) {
				MidiEvent event = track.get(index);
				MidiMessage midiMessage = event.getMessage();

				if (midiMessage instanceof MetaMessage) {
					MetaMessage metaMessage = (MetaMessage) midiMessage;

					if (0x03 == metaMessage.getType()) {
						metaMessage.setMessage(0x03, trackName.getBytes(),
								trackName.length());
						trackNameReplaced = true;
						break;
					}
				}
			}

			if (!trackNameReplaced) {
				MetaMessage metaMessage = new MetaMessage();
				metaMessage.setMessage(0x03, trackName.getBytes(),
						trackName.length());
				MidiEvent midiEvent = new MidiEvent(metaMessage, 0L);
				track.add(midiEvent);
			}
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackName to {}", trackName);
		}
	}

	protected void modifyTrackNotes(Track track, Instruments instrument) {
		MapperMidiToPhaseShift mapperMidiToPhaseShift = new MapperMidiToPhaseShift(
				instrument);

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
						NotesPhaseShift notePhaseShift = mapperMidiToPhaseShift
								.getNote(noteMidi);
						NotesEof noteEof = MapperPhaseShiftToEof
								.getNote(notePhaseShift);
						Integer noteMidiNew = MapperEofToMidi.getNote(noteEof);

						LOG.trace(
								"noteMidi={} > notePhaseShift={} > noteEof={} > noteMidiNew={}",
								noteMidi, notePhaseShift.name(),
								noteEof.name(), noteMidiNew);

						shortMessage.setMessage(shortMessage.getStatus(),
								noteMidiNew, shortMessage.getData2());
						break;
					}
				}
			}
		} catch (InvalidMidiDataException e) {
			LOG.error("cannot modify trackNotes to {}", instrument);
		}
	}
}
