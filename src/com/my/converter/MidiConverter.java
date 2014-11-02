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

import com.my.config.Config;
import com.my.config.ConfigBassReal4;
import com.my.config.ConfigBassReal5;
import com.my.config.ConfigBassReal6;
import com.my.config.ConfigDrums;
import com.my.config.ConfigFile;
import com.my.config.ConfigGuitarReal6;
import com.my.main.Song;
import com.my.midi.MidiFile;
import com.my.midi.MidiLogger;
import com.my.midi.TabFile;
import com.my.midi.TabTrack;
import com.my.phaseshift.Instruments;
import com.my.print.Highway;

public class MidiConverter {
	private static Logger LOG = LogManager.getLogger(MidiConverter.class);

	protected String filePath = "";

	protected Track metaTrack = null;

	protected Boolean bpmReplaced = false;

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

	protected void convertFile(File fileMidi) {
		LOG.info("convert midiFile {}", fileMidi.getAbsolutePath());
		try {
			MidiFile midiFile = new MidiFile(fileMidi);

			// check if additional tabFile exists
			TabFile tabFile = null;
			File fileTab = new File(midiFile.getFilePath() + midiFile.getSongArtist() + " - " + midiFile.getSongTitle() + ".txt");
			if (fileTab.exists()) {
				tabFile = new TabFile(fileTab);
			}

			// create sub directory
			String strPath = midiFile.getFilePath();
			strPath += midiFile.getSongArtist() + " - " + midiFile.getSongTitle() + System.getProperty("file.separator");
			File path = new File(strPath);
			path.mkdirs();
			this.filePath = strPath;

			this.modifyMidi(midiFile, tabFile);
			this.writeMidiNew(midiFile);
		} catch (InvalidMidiDataException | IOException e) {
			LOG.error("cannot read midiFile '{}'", fileMidi.getAbsolutePath(), e);
		}
	}

	protected void writeMidiNew(MidiFile midiFile) {
		// create new midi file
		File fileMidiNew = new File(this.filePath + "midi.mid");
		int fileType = MidiSystem.getMidiFileTypes(midiFile.getSequence())[0];
		try {
			MidiSystem.write(midiFile.getSequence(), fileType, fileMidiNew);
			LOG.info("midi conversion completed '{}'", fileMidiNew.getAbsolutePath());
		} catch (IOException e) {
			LOG.error("midi conversion failed", e);
		}
	}

	protected void modifyMidi(MidiFile midiFile, TabFile tabFile) {
		Set<Integer> trackIndices = midiFile.getInstruments().keySet();

		// modify tracks
		List<Track> tracksToDelete = new ArrayList<Track>();

		for (int trackIndex = 0; trackIndex < midiFile.getTracks().size(); trackIndex++) {
			Track midiTrack = midiFile.getSequence().getTracks()[trackIndex];
			if (trackIndex == 0) {
				LOG.info("track with index {} is metaTrack and will be modified", trackIndex);
				MidiLogger.logTrack("metaTrack", midiTrack);
				metaTrack = midiTrack;
				continue;
			}

			if (trackIndices.contains(trackIndex)) {
				Instruments instrument = midiFile.getInstruments().get(trackIndex);
				TabTrack tabTrack = null;
				if (null != tabFile) {
					if (tabFile.getTracks().size() >= trackIndex) {
						tabTrack = tabFile.getTracks().get(trackIndex - 1);
					}
				}

				LOG.info("track with index {} will be modified for {}", trackIndex, instrument);
				this.modifyTrack(instrument, midiTrack, tabTrack);

				// test
				// this.writeImage(midiFile, trackIndex);
			} else {
				LOG.info("track with index {} will be removed", trackIndex);
				tracksToDelete.add(midiTrack);
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

	protected void modifyTrack(Instruments instrument, Track midiTrack, TabTrack tabTrack) {
		MidiLogger.logTrack("before", midiTrack);

		Config config = null;
		TrackConverter trackConverter = null;

		switch (instrument) {
			case GUITAR_REAL_6:
				config = ConfigFile.read(this.filePath, ConfigGuitarReal6.class); // first try subdirectory
				trackConverter = new TrackConverterGuitarReal(config);
				break;
			case BASS_REAL_4:
				config = ConfigFile.read(this.filePath, ConfigBassReal4.class); // first try subdirectory
				trackConverter = new TrackConverterBassReal(config);
				break;
			case BASS_REAL_5:
				config = ConfigFile.read(this.filePath, ConfigBassReal5.class); // first try subdirectory
				trackConverter = new TrackConverterBassReal(config);
				break;
			case BASS_REAL_6:
				config = ConfigFile.read(this.filePath, ConfigBassReal6.class); // first try subdirectory
				trackConverter = new TrackConverterBassReal(config);
				break;
			case DRUMS:
				config = ConfigFile.read(this.filePath, ConfigDrums.class); // first try subdirectory
				trackConverter = new TrackConverterDrums(config);
				tabTrack = null;
				break;
			default:
				break;
		}

		//		if (!bpmReplaced) {
		//			if (null != metaTrack && null != config) {
		//				TrackConverterMeta trackConverterMeta = new TrackConverterMeta(config);
		//				trackConverterMeta.convert(metaTrack);
		//			}
		//			bpmReplaced = true;
		//		}

		trackConverter.convert(metaTrack, midiTrack, tabTrack);
		MidiLogger.logTrack("after", midiTrack);
	}
}
