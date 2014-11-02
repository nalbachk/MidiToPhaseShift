package com.my.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigFile {
	private static Logger LOG = LogManager.getLogger(ConfigFile.class);

	public static <T extends Config> void write(T config) {
		Class<? extends Config> clazz = config.getClass();
		String fileName = clazz.getSimpleName() + ".xml";
		File file = new File(fileName);

		try {
			JAXBContext jaxb = JAXBContext.newInstance(clazz);
			Marshaller marshaller = jaxb.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(config, file);
		} catch (JAXBException e) {
			LOG.error("cannot write config {} to file {}", file.getAbsolutePath(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T read(String filePath, Class<? extends Config> clazz) {
		T t = null;
		String fileName = clazz.getSimpleName() + ".xml";
		File file = new File(filePath + fileName);
		if (file.exists()) {
			try {
				JAXBContext jaxb = JAXBContext.newInstance(clazz);
				Unmarshaller unmarshaller = jaxb.createUnmarshaller();
				t = (T) unmarshaller.unmarshal(file);
			} catch (Exception e) {
				LOG.error("cannot read config {}", file.getAbsolutePath(), e);
			}
		} else if (filePath.length() > 0) {
			// try again without path
			return read("", clazz);
		}
		return t;
	}
}
