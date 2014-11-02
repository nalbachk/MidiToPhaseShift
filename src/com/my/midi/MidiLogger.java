package com.my.midi;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.my.converter.TrackConverter;

/**
 * @author Administrator
 */
public class MidiLogger {
	private static Logger LOG = LogManager.getLogger(MidiLogger.class);

	public static void logTrack(String message, Track track) {
		LOG.trace("---------");
		LOG.trace(message);
		LOG.trace(toString(track));
		LOG.trace("---------");
	}

	private static String toString(Track track) {
		StringBuilder str = new StringBuilder();

		str.append("\nTrack[");
		str.append("size=" + track.size());
		str.append(", ticks=" + track.ticks());
		str.append("]");

		for (int index = 0; index < track.size(); index++) {
			MidiEvent event = track.get(index);
			str.append(toString(event));
		}

		return str.toString();
	}

	private static String toString(MidiEvent event) {
		String str = "";

		MidiMessage midiMessage = event.getMessage();
		String strTmp = "";
		if (midiMessage instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) midiMessage;
			strTmp += toString(shortMessage);
		} else if (midiMessage instanceof MetaMessage) {
			MetaMessage metaMessage = (MetaMessage) midiMessage;
			strTmp += toString(metaMessage);
		} else if (midiMessage instanceof SysexMessage) {
			SysexMessage sysexMessage = (SysexMessage) midiMessage;
			strTmp += toString(sysexMessage);
		} else {
			strTmp += toString(midiMessage);
		}

		str += "\nMidiEvent[";
		str += "tick=" + event.getTick();
		str += " " + strTmp;
		str += "]";

		return str;
	}

	private static String toString(byte[] message) {
		String str = "";
		for (byte b : message) {
			str += TrackConverter.toUnsignedByte(b) + ";";
		}
		//return new String(message, Charset.forName("ASCII"));
		return str;
	}

	private static String toString(ShortMessage message) {
		String str = "";

		// type 81 = setTempo
		// type 88 = Time signature

		String commandName = String.valueOf(message.getCommand());
		switch (message.getCommand()) {
			case ShortMessage.NOTE_ON:
				commandName = "NOTE_ON";
				break;
			case ShortMessage.NOTE_OFF:
				commandName = "NOTE_OFF";
				break;
			case ShortMessage.CONTROL_CHANGE:
				commandName = "CONTROL_CHANGE";
				break;
			case ShortMessage.PITCH_BEND:
				commandName = "PITCH_BEND";
				break;
			case ShortMessage.PROGRAM_CHANGE:
				commandName = "PROGRAM_CHANGE";
				break;
		}

		str += "ShortMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + toString(message.getMessage());
		str += ", data1=" + message.getData1();
		str += ", data2=" + message.getData2();
		str += ", commandName=" + commandName;
		str += "]";

		return str;
	}

	private static String toString(MetaMessage message) {
		String str = "";

		str += "MetaMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + toString(message.getMessage());
		str += ", data=" + toString(message.getData());
		str += ", type=" + message.getType();
		str += "]";

		return str;
	}

	private static String toString(SysexMessage message) {
		String str = "";

		str += "SysexMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + toString(message.getMessage());
		str += ", data=" + toString(message.getData());
		str += "]";

		return str;
	}

	private static String toString(MidiMessage message) {
		String str = "";

		str += "MidiMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + toString(message.getMessage());
		str += "]";

		return str;
	}
}
