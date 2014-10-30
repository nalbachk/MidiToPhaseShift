package com.my.midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.phaseshift.Instruments;

public class MidiFile {
	private static Logger LOG = LogManager.getLogger(MidiFile.class);

	protected String filePath = null;
	protected String fileName = null;
	protected String fileNameNoEnding = null;

	protected String songArtist = null;
	protected String songTitle = null;
	protected Map<Integer, Instruments> instruments = null;

	protected Sequence sequence = null;
	protected List<Track> tracks = null;

	public MidiFile(File file) throws InvalidMidiDataException, IOException {
		this.filePath = file.getParentFile().getAbsolutePath() + File.separatorChar;
		this.fileName = file.getName();
		this.fileNameNoEnding = this.fileName.split(".mid")[0];
		try {
			this.readInfoFromFileName(this.fileNameNoEnding);
		} catch (IllegalArgumentException e) {
			String fileNameExample = "Artist - SongName - 1=GUITAR,2=BASS,3=DRUMS.mid";
			LOG.error("invalid midiFileName '{}' (example: '{}')", this.fileName, fileNameExample);
			throw e;
		}

		// read midi tracks
		this.sequence = MidiSystem.getSequence(file);
		this.tracks = new ArrayList<Track>(Arrays.asList(this.sequence.getTracks()));

		LOG.info("{} tracks found in '{}'", tracks.size(), fileName);
	}

	protected void readInfoFromFileName(String fileName) {
		List<String> list = Arrays.asList(fileName.split(" - "));
		if (3 != list.size()) {
			throw new IllegalArgumentException("invalid midiFileName");
		}

		// get info
		this.songArtist = list.get(0);
		this.songTitle = list.get(1);
		String mappingInstruments = list.get(2);
		this.readInstruments(mappingInstruments);
	}

	protected void readInstruments(String mappingInstruments) {
		this.instruments = new HashMap<Integer, Instruments>();
		List<String> mappings = Arrays.asList(mappingInstruments.split(","));
		if (mappings.size() < 1) {
			throw new IllegalArgumentException("invalid midiFileName");
		}

		for (String mapping : mappings) {
			List<String> list = Arrays.asList(mapping.split("="));
			if (2 != list.size()) {
				throw new IllegalArgumentException("invalid midiFileName");
			}

			String strTrackIndex = list.get(0);
			String strInstrument = list.get(1);

			Integer trackIndex = Integer.valueOf(strTrackIndex);
			Instruments instrument = Instruments.valueOf(strInstrument);
			this.instruments.put(trackIndex, instrument);
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileNameNoEnding() {
		return fileNameNoEnding;
	}

	public void setFileNameNoEnding(String fileNameNoEnding) {
		this.fileNameNoEnding = fileNameNoEnding;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public Map<Integer, Instruments> getInstruments() {
		return instruments;
	}

	public void setInstruments(Map<Integer, Instruments> instruments) {
		this.instruments = instruments;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
}
