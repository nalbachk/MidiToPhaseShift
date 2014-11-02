package com.my.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.Config;

public class TrackConverterGuitarReal extends TrackConverterStringsReal {
	private static Logger LOG = LogManager.getLogger(TrackConverterGuitarReal.class);

	public TrackConverterGuitarReal(Config config) {
		super(config);
	}

	@Override
	protected String getTrackName() {
		return "PART REAL_GUITAR_22";
	}
}
