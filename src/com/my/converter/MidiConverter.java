package com.my.converter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.main.Song;
import com.my.midi.MidiFile;
import com.my.midi.MidiLogger;
import com.my.phaseshift.Instruments;
import com.my.print.Highway;

public class MidiConverter {
	private static Logger LOG = LogManager.getLogger(MidiConverter.class);

	public void convertFiles() {
		File directory = new File("midi");
		List<File> files = this.findFilesMidi(directory);
		LOG.info("{} midiFiles found in {}", files.size(), directory.getAbsolutePath());

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
		strPath += midiFile.getSongArtist() + " - " + midiFile.getSongTitle() + System.getProperty("file.separator");
		File path = new File(strPath);
		path.mkdirs();

		// create new midi file
		File fileMidiNew = new File(strPath + "midi.mid");
		int fileType = MidiSystem.getMidiFileTypes(midiFile.getSequence())[0];
		try {
			MidiSystem.write(midiFile.getSequence(), fileType, fileMidiNew);
			LOG.info("midi conversion completed '{}'", fileMidiNew.getAbsolutePath());
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
				Instruments instrument = midiFile.getInstruments().get(trackIndex);
				LOG.info("track with index {} will be modified for {}", trackIndex, instrument);
				this.modifyTrack(track, instrument);

				// test
				this.writeImage(midiFile, trackIndex);
			} else {
				LOG.info("track with index {} will be removed", trackIndex);
				tracksToDelete.add(track);
			}
		}

		for (Track track : tracksToDelete) {
			midiFile.getSequence().deleteTrack(track);
		}
	}

	protected void writeImage(MidiFile midiFile, Integer trackIndex) {
		String strPath = midiFile.getFilePath();
		strPath += midiFile.getSongArtist() + " - " + midiFile.getSongTitle() + System.getProperty("file.separator");
		File path = new File(strPath);
		path.mkdirs();

		Song song = new Song("Dummy", "Dummy", 999, new File(midiFile.getFilePath() + midiFile.getFileName()), trackIndex);
		Highway highway = new Highway(song);
		BufferedImage image = highway.generateImage();

		File output = new File(strPath + trackIndex + ".png");
		try {
			ImageIO.write(image, "PNG", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void modifyTrack(Track track, Instruments instrument) {
		MidiLogger.logTrack(track);

		TrackConverter trackConverter = null;

		switch (instrument) {
			case DRUMS:
				trackConverter = new TrackConverterDrums();
				break;
			case GUITAR_REAL:
				trackConverter = new TrackConverterGuitarReal();
				break;
			default:
				break;
		}

		trackConverter.convert(track);

		MidiLogger.logTrack(track);
	}
}
