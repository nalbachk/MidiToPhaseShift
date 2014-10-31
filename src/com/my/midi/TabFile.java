package com.my.midi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TabFile {
	private static Logger LOG = LogManager.getLogger(TabFile.class);

	protected String filePath = null;
	protected String fileName = null;
	protected String fileNameNoEnding = null;

	protected List<TabTrack> tracks = null;

	public TabFile(File file) {
		this.filePath = file.getParentFile().getAbsolutePath() + System.getProperty("file.separator");
		this.fileName = file.getName();
		this.fileNameNoEnding = this.fileName.split(".txt")[0];

		// read tab tracks
		this.tracks = readTracks(file);
	}

	protected List<TabTrack> readTracks(File file) {
		List<TabTrack> tracks = new ArrayList<TabTrack>();

		String fileContent = this.readFileContent(file);
		List<String> trackContents = Arrays.asList(fileContent.split(System.lineSeparator() + "Track"));
		LOG.info("{} possible tracks found in '{}'", trackContents.size(), file.getAbsoluteFile());

		// first contains no track
		for (int index = 1; index < trackContents.size(); index++) {
			TabTrack track = new TabTrack(trackContents.get(index));
			tracks.add(track);
		}
		LOG.info("{} tracks found in '{}'", tracks.size(), file.getAbsolutePath());

		return tracks;
	}

	protected String readFileContent(File file) {
		StringBuilder content = new StringBuilder();

		try (FileReader fileReader = new FileReader(file); //
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			String line = null;
			while (null != (line = bufferedReader.readLine())) {
				content.append(line);
				content.append(System.lineSeparator());
			}
		} catch (IOException e) {
			LOG.error("could not read file content {}", file.getAbsolutePath(), e);
		}

		return content.toString();
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

	public List<TabTrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<TabTrack> tracks) {
		this.tracks = tracks;
	}
}
