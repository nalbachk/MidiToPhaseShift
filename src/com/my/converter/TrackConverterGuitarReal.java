package com.my.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.config.ConfigStrings;

public class TrackConverterGuitarReal extends TrackConverterStringsReal {
	private static Logger LOG = LogManager.getLogger(TrackConverterGuitarReal.class);

	public TrackConverterGuitarReal(ConfigStrings configStrings) {
		super(configStrings);
	}

	@Override
	protected String getTrackName() {
		return "PART REAL_GUITAR_22";
	}
}
