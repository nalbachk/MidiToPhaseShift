package com.my.print;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.my.main.Note;
import com.my.main.Song;
import com.my.mapper.NotesPhaseShift;
import com.my.phaseshift.Instruments;

public class Highway {

	protected final int lineWidth = 10;
	protected final double scaleX = 1.0D;
	protected final double scaleY = 0.1D;

	protected Song song;

	public Highway(Song song) {
		this.song = song;
	}

	public BufferedImage generateImage() {
		final int lineWidth = 10;

		// lines for notes
		HashMap<Integer, Integer> lines = new HashMap<Integer, Integer>();
		Integer linePos = 0;
		List<Integer> noteValues = new ArrayList<Integer>(this.song.getNoteValues());
		Collections.sort(noteValues);
		for (Integer noteValue : noteValues) {
			lines.put(noteValue, linePos++);
		}

		Integer width = lineWidth * 8 + 10;
		Integer height = (int) (this.song.getLength().intValue() * scaleY);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setBackground(Color.BLACK);

		for (Note note : this.song.getNotes()) {
			NotesPhaseShift notePhaseShift = null; //TODO mapperMidiToPhaseShift.getNote(note.getValue());
			g.setColor(notePhaseShift.getColor());
			int positionX1 = notePhaseShift.getLineNumber() * lineWidth;
			int positionY1 = (int) (note.getStart().intValue() * scaleY);
			int noteHeight = 10;
			if (Instruments.DRUMS != song.getInstrument()) {
				noteHeight = (int) (note.getDuration().intValue() * scaleY);
			}
			g.fillRect(positionX1, positionY1, lineWidth, noteHeight);
		}
		g.dispose();
		image.flush();

		return image;
	}
}
