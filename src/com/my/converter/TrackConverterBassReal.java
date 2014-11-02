package com.my.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.Config;

public class TrackConverterBassReal extends TrackConverterStringsReal {
	private static Logger LOG = LogManager.getLogger(TrackConverterBassReal.class);

	public TrackConverterBassReal(Config config) {
		super(config);
	}

	@Override
	protected String getTrackName() {
		return "PART REAL_BASS_22";
	}
}
