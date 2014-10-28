package com.my.main;

import com.my.config.Config;
import com.my.config.ConfigGuitarReal;
import com.my.converter.MidiConverter;

public class Main {

	public static void main(String[] args) {
		ConfigGuitarReal config = Config.readFromFile(ConfigGuitarReal.class);
		if (null == config) {
			config = new ConfigGuitarReal();
			config.writeToFile();
		}

		MidiConverter midiConverter = new MidiConverter();
		midiConverter.convertFiles();
	}
}
