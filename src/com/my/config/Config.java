package com.my.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Config {
	private static Logger LOG = LogManager.getLogger(Config.class);

	protected File getFile() {
		String fileName = getClass().getSimpleName() + ".xml";
		File file = new File(fileName);
		return file;
	}

	public void writeToFile() {
		try {
			JAXBContext jaxb = JAXBContext.newInstance(this.getClass());
			Marshaller marshaller = jaxb.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(this, this.getFile());
		} catch (JAXBException e) {
			LOG.error("cannot write config {}", getFile().getAbsolutePath(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T readFromFile(Class<? extends Config> clazz) {
		T t = null;
		String fileName = clazz.getSimpleName() + ".xml";
		File file = new File(fileName);
		try {
			JAXBContext jaxb = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jaxb.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(file);
		} catch (Exception e) {
			LOG.error("cannot read config {}", file.getAbsolutePath(), e);
		}
		return t;
	}
}
