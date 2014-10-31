package com.my.phaseshift;

// TODO others
//(X) PART GUITAR: 0
//(X) PART BASS: 0
//(X) PART GUITAR COOP: 0
//(X) PART RHYTHM: 0
//(X) PART DRUMS: 1
//(X) PART KEYS: 0
//(X) PART REAL_DRUMS_PS: 1

public enum Instruments {
	DRUMS("PART DRUMS"), //
	DRUMS_ADVANCED("PART DRUMS"), //
	DRUMS_REAL("PART REAL_DRUMS_PS"), //
	GUITAR("PART GUITAR"), //
	GUITAR_ADVANCED("PART GUITAR"), //
	GUITAR_REAL_6("PART GUITAR"), //
	BASS("PART BASS"), //
	BASS_ADVANCED("PART BASS"), //
	BASS_REAL_4("PART BASS"), //
	BASS_REAL_5("PART BASS"), //
	BASS_REAL_6("PART BASS"), //
	KEYS("PART KEYS"), //
	KEYS_ADVANCED("PART KEYS"), //
	KEYS_REAL("PART KEYS");

	String trackName = null;

	private Instruments(String trackName) {
		this.trackName = trackName;
	}

	public String getTrackName() {
		return trackName;
	}

	protected void setTrackName(String trackName) {
		this.trackName = trackName;
	}
}
