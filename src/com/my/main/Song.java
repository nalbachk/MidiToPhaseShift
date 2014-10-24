package com.my.main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.my.phaseshift.Instruments;

/**
 * 
 * @author Administrator
 */
public class Song {

	private static final Logger LOG = Logger.getLogger(Song.class.getName());

	protected String artist;
	protected String title;
	protected Integer bpm;
	protected Long length;

	protected Instruments instrument;
	protected Set<Integer> noteValues;
	protected List<Note> notes;

	public Song() {
		this.title = "Unknown";
		this.artist = "Unknown";
		this.bpm = Integer.valueOf(120);
		this.length = Long.valueOf(0L);

		this.instrument = Instruments.DRUMS;
		this.noteValues = new HashSet<Integer>();
		this.notes = new ArrayList<Note>();
	}

	public Song(String artist, String title, Integer bpm, File fileMidi, Integer trackIndex) {
		this.artist = artist;
		this.title = title;
		this.bpm = bpm;
		this.length = Long.valueOf(0L);

		this.instrument = Instruments.GUITAR;
		this.noteValues = new HashSet<Integer>();
		this.notes = new ArrayList<Note>();

		try {
			this.parseMidi(fileMidi, trackIndex);
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void parseMidi(File fileMidi, Integer trackIndex) throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(fileMidi);
		List<Track> tracks = new ArrayList<Track>(Arrays.asList(sequence.getTracks()));
		LOG.info(tracks.size() + " tracks found");

		Track track = tracks.get(trackIndex);
		LOG.info("trackSize: " + track.size());
		LOG.info("trackTicks: " + track.ticks());

		this.length = track.ticks();

		long[] notesTmp = new long[256];

		for (int index = 0; index < track.size(); index++) {
			MidiEvent event = track.get(index);

			long tick = event.getTick();
			MidiMessage midiMessage = event.getMessage();

			int noteValue = 0;
			int command = 0;

			if (midiMessage instanceof ShortMessage) {
				ShortMessage shortMessage = (ShortMessage) midiMessage;

				noteValue = shortMessage.getData1();

				command = shortMessage.getCommand();
				switch (command) {
					case ShortMessage.NOTE_ON:
						notesTmp[noteValue] = tick;
						break;
					case ShortMessage.NOTE_OFF:
						Long start = notesTmp[noteValue];
						Long duration = tick - notesTmp[noteValue];
						Note note = new Note(start, duration, noteValue);
						this.notes.add(note);
						this.noteValues.add(noteValue);
						break;
					case ShortMessage.CONTROL_CHANGE:
						break;
					case ShortMessage.PITCH_BEND:
						break;
					default:
						break;
				}
			}
		}
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBpm() {
		return bpm;
	}

	public void setBpm(Integer bpm) {
		this.bpm = bpm;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Instruments getInstrument() {
		return instrument;
	}

	public void setInstrument(Instruments instrument) {
		this.instrument = instrument;
	}

	public Set<Integer> getNoteValues() {
		return noteValues;
	}

	public void setNoteValues(Set<Integer> noteValues) {
		this.noteValues = noteValues;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Song [artist=" + artist + ", title=" + title + ", bpm=" + bpm + ", length=" + length + ", instrument=" + instrument + ", noteValuesSize=" + noteValues.size() + ", notesSize=" + notes.size() + "]";
	}
}
