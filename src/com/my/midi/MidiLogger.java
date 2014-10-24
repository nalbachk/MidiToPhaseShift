package com.my.midi;

import java.nio.charset.Charset;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Administrator
 */
public class MidiLogger {
	private static Logger LOG = LogManager.getLogger(MidiLogger.class);

	public static void logTrack(Track track) {
		LOG.trace("---------");
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

	private static String toString(ShortMessage message) {
		String str = "";

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
		}

		str += "ShortMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + new String(message.getMessage(), Charset.forName("ASCII"));
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
		str += ", message=" + new String(message.getMessage(), Charset.forName("ASCII"));
		str += ", data=" + new String(message.getData(), Charset.forName("ASCII"));
		str += ", type=" + message.getType();
		str += "]";

		return str;
	}

	private static String toString(SysexMessage message) {
		String str = "";

		str += "SysexMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + new String(message.getMessage(), Charset.forName("ASCII"));
		str += ", data=" + new String(message.getData(), Charset.forName("ASCII"));
		str += "]";

		return str;
	}

	private static String toString(MidiMessage message) {
		String str = "";

		str += "MidiMessage[";
		str += "status=" + message.getStatus();
		str += ", length=" + message.getLength();
		str += ", message=" + new String(message.getMessage(), Charset.forName("ASCII"));
		str += "]";

		return str;
	}
}
