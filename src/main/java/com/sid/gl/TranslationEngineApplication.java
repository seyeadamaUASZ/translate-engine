package com.sid.gl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class TranslationEngineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TranslationEngineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("templates/assets/i18n/en.json").getFile());
		//String fileCode = path_i18n + "/"+ "en"+ ".json";
		System.out.println(new FileReader(file).toString());
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line =reader.readLine()) != null) {
			System.out.println("line on section while 11"+line);
		//	buffer.append(line);
		}*/
	}
}
