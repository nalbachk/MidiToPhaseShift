package com.my.main;

import com.my.config.ConfigBassReal4;
import com.my.config.ConfigBassReal5;
import com.my.config.ConfigBassReal6;
import com.my.config.ConfigDrums;
import com.my.config.ConfigFile;
import com.my.config.ConfigGuitarReal6;
import com.my.converter.MidiConverter;

public class Main {

	public static void main(String[] args) {
		Main.createConfigs();

		MidiConverter midiConverter = new MidiConverter();
		midiConverter.convertFiles();
	}

	private static void createConfigs() {
		String filePath = "";
		ConfigGuitarReal6 configGuitarReal6 = ConfigFile.read(filePath, ConfigGuitarReal6.class);
		if (null == configGuitarReal6) {
			configGuitarReal6 = new ConfigGuitarReal6();
			ConfigFile.write(configGuitarReal6);
		}

		ConfigBassReal4 configBassReal4 = ConfigFile.read(filePath, ConfigBassReal4.class);
		if (null == configBassReal4) {
			configBassReal4 = new ConfigBassReal4();
			ConfigFile.write(configBassReal4);
		}

		ConfigBassReal5 configBassReal5 = ConfigFile.read(filePath, ConfigBassReal5.class);
		if (null == configBassReal5) {
			configBassReal5 = new ConfigBassReal5();
			ConfigFile.write(configBassReal5);
		}

		ConfigBassReal6 configBassReal6 = ConfigFile.read(filePath, ConfigBassReal6.class);
		if (null == configBassReal6) {
			configBassReal6 = new ConfigBassReal6();
			ConfigFile.write(configBassReal6);
		}

		ConfigDrums configDrums = ConfigFile.read(filePath, ConfigDrums.class);
		if (null == configDrums) {
			configDrums = new ConfigDrums();
			ConfigFile.write(configDrums);
		}
	}
}
