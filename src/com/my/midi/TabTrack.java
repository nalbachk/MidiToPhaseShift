package com.my.midi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TabTrack {
	private static Logger LOG = LogManager.getLogger(TabTrack.class);

	private String trackContent = null;

	private Integer linesCount = 0;

	private List<TabPosition> positions = null;

	public TabTrack(String trackContent) {
		this.trackContent = trackContent;
	}

	public void init() {
		int pos = 0;

		// remove crap before beginning
		pos = trackContent.indexOf('|') - 1;
		trackContent = trackContent.substring(pos);

		// remove crap after end
		pos = trackContent.lastIndexOf('|') + 1;
		trackContent = trackContent.substring(0, pos);

		List<String> trackRows = Arrays.asList(trackContent.split(System.lineSeparator() + System.lineSeparator()));
		LOG.trace("{} trackRows found in track", trackRows.size());

		positions = new LinkedList<TabPosition>();
		for (String trackRow : trackRows) {
			List<List<Integer>> notesByTrackRow = this.readNotesByTrackRow(trackRow);
			positions.addAll(this.convertToPositions(notesByTrackRow));
		}
		LOG.info("{} positions found in track", positions.size());

		// log
		for (int i = 0; i < positions.size(); i++) {
			LOG.trace("{}: {}", i, positions.get(i));
		}
	}

	protected List<List<Integer>> readNotesByTrackRow(String trackRow) {
		int pos = 0;

		// remove crap before beginning
		pos = trackRow.indexOf('|') - 1;
		trackRow = trackRow.substring(pos);

		// remove crap after end
		pos = trackRow.lastIndexOf('|') + 1;
		trackRow = trackRow.substring(0, pos);

		List<StringBuilder> lines = new ArrayList<StringBuilder>();
		List<String> linesTmp = Arrays.asList(trackRow.split(System.lineSeparator()));
		for (int i = 0; i < linesTmp.size(); i++) { // DESC because later top-line has lineNr=6 and bottom-line has lineNr=1
			String line = linesTmp.get(linesTmp.size() - 1 - i);

			// remove crap before beginning
			pos = line.indexOf('|') + 1;
			line = line.substring(pos);

			// remove crap after end
			pos = line.lastIndexOf('|');
			line = line.substring(0, pos);

			lines.add(new StringBuilder(line)); // convert
		}

		if (0 == this.linesCount) {
			LOG.info("{} lines found in track", lines.size());
		} else if (this.linesCount != lines.size()) {
			LOG.error("different linesCount in the same track: before={} after={}", this.linesCount, lines.size());
		}
		this.linesCount = lines.size();

		// replace "-" that stands for no note (because "-" is delimiter too)
		for (int position = 0; position < lines.get(0).length(); position++) {
			if (this.isNumberOnPosition(lines, position)) {
				this.replaceDummiesOnPosition(lines, position);
			}
		}

		List<List<Integer>> notesByTrackRow = new ArrayList<List<Integer>>();
		for (StringBuilder line : lines) {
			List<Integer> notesPerTrackRowLine = readNotes(line.toString());
			notesByTrackRow.add(notesPerTrackRowLine);
		}

		return notesByTrackRow;
	}

	protected boolean isNumberOnPosition(List<StringBuilder> lines, int position) {
		for (StringBuilder line : lines) {
			if (line.charAt(position) >= '0' && line.charAt(position) <= '9') {
				return true;
			}
		}
		return false;
	}

	protected void replaceDummiesOnPosition(List<StringBuilder> lines, int position) {
		for (StringBuilder line : lines) {
			if ('-' == line.charAt(position)) {
				line.setCharAt(position, ' ');
			}
		}
	}

	protected List<Integer> readNotes(String line) {
		// remove "|" symbols
		line = line.replaceAll("\\x7C", "");

		// split by "-" symbol
		List<String> strNotes = Arrays.asList(line.split("\\x2D"));
		LOG.trace("{} strNotes found in line", strNotes.size());

		List<Integer> notes = new ArrayList<Integer>(strNotes.size());
		for (String strNote : strNotes) {
			if (strNote.length() > 0) {
				strNote = strNote.trim();
				Integer note = -1;
				if (strNote.length() > 0) {
					note = Integer.valueOf(strNote);
				}
				notes.add(note);
			}
		}
		LOG.trace("{} notes found found in line", notes.size());
		LOG.trace(notes);

		return notes;
	}

	protected List<TabPosition> convertToPositions(List<List<Integer>> notesByTrackRow) {
		int positionsCount = notesByTrackRow.get(0).size();

		List<TabPosition> positionsByTrackRow = new ArrayList<TabPosition>(positionsCount);

		for (int pos = 0; pos < positionsCount; pos++) {
			List<Integer> notesOnPosition = new ArrayList<Integer>(this.linesCount);
			for (List<Integer> notesByTrackRowLine : notesByTrackRow) {
				notesOnPosition.add(notesByTrackRowLine.get(pos));
			}
			TabPosition position = new TabPosition(notesOnPosition);
			positionsByTrackRow.add(position);
		}

		return positionsByTrackRow;
	}

	public Integer getLinesCount() {
		return linesCount;
	}

	public void setLinesCount(Integer linesCount) {
		this.linesCount = linesCount;
	}

	public List<TabPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<TabPosition> positions) {
		this.positions = positions;
	}

	public TabPosition getPositionByIndex(int index) {
		if (index >= getPositions().size()) {
			LOG.error("tab track is wrong! please fix the .txt file! position: {}", index);
			throw new IllegalArgumentException();
		}
		return this.getPositions().get(index);
	}
}
