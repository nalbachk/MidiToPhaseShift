package com.my.main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;



import com.my.converter.Converter;

public class Main {

	public static void main(String[] args) {
		Converter converter = new Converter();
		converter.convertFiles();
	}
	
//	protected void createHighwayImage(Song song, String path)
//			throws IOException {
//		// create highway
//		Highway highway = new Highway(song);
//		BufferedImage image = highway.generateImage();
//
//		File fileImage = new File(path + "img.png");
//		ImageIO.write(image, "png", fileImage);
//		LOG.info("image saved: " + fileImage.getAbsolutePath());
//	}
//

//
//		File fileMidi = new File(path + "midi.mid");
//		int fileType = MidiSystem.getMidiFileTypes(sequence)[0];
//		MidiSystem.write(sequence, fileType, fileMidi);
//		LOG.info("midi saved: " + fileMidi.getAbsolutePath());
//	}
}
