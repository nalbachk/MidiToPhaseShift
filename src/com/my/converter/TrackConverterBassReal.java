package com.my.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.ConfigStrings;

public class TrackConverterBassReal extends TrackConverterStringsReal {
	private static Logger LOG = LogManager.getLogger(TrackConverterBassReal.class);

	public TrackConverterBassReal(ConfigStrings configStrings) {
		super(configStrings);
	}

	@Override
	protected String getTrackName() {
		return "PART REAL_BASS_22";
	}
}
