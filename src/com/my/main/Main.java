package com.my.main;

import com.my.config.Config;
import com.my.config.ConfigBassReal4;
import com.my.config.ConfigBassReal5;
import com.my.config.ConfigBassReal6;
import com.my.config.ConfigDrums;
import com.my.config.ConfigGuitarReal6;
import com.my.converter.MidiConverter;

public class Main {

	public static void main(String[] args) {
		Main.createConfigs();

		MidiConverter midiConverter = new MidiConverter();
		midiConverter.convertFiles();
	}

	private static void createConfigs() {
		ConfigGuitarReal6 configGuitarReal6 = Config.readFromFile(ConfigGuitarReal6.class);
		if (null == configGuitarReal6) {
			configGuitarReal6 = new ConfigGuitarReal6();
			configGuitarReal6.writeToFile();
		}

		ConfigBassReal4 configBassReal4 = Config.readFromFile(ConfigBassReal4.class);
		if (null == configBassReal4) {
			configBassReal4 = new ConfigBassReal4();
			configBassReal4.writeToFile();
		}

		ConfigBassReal5 configBassReal5 = Config.readFromFile(ConfigBassReal5.class);
		if (null == configBassReal5) {
			configBassReal5 = new ConfigBassReal5();
			configBassReal5.writeToFile();
		}

		ConfigBassReal6 configBassReal6 = Config.readFromFile(ConfigBassReal6.class);
		if (null == configBassReal6) {
			configBassReal6 = new ConfigBassReal6();
			configBassReal6.writeToFile();
		}

		ConfigDrums configDrums = Config.readFromFile(ConfigDrums.class);
		if (null == configDrums) {
			configDrums = new ConfigDrums();
			configDrums.writeToFile();
		}
	}
}
