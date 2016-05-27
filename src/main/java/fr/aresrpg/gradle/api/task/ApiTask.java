package fr.aresrpg.gradle.api.task;

import fr.aresrpg.gradle.api.asm.ApiWriter;
import fr.aresrpg.gradle.api.asm.LibraryReader;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.*;
import org.objectweb.asm.ClassReader;

import javax.inject.Inject;
import java.io.*;

public class ApiTask extends DefaultTask{
	public static final String NAME = "api";

	private File outputFolder;
	private File inputFolder;

	public ApiTask(File outputFolder, File inputFolder) {
		this.outputFolder = outputFolder;
		this.inputFolder = inputFolder;
	}

	@Inject
	public ApiTask() {
		//Empty for inject
	}

	@TaskAction
	public void buildApi() throws IOException {
		if(!this.outputFolder.exists() && !this.outputFolder.mkdirs())
			throw new IllegalStateException("Could'not create output folder " + outputFolder.getPath());
		processFolder(inputFolder);
	}

	protected void processFolder(File folder) throws IOException {
		for(File file : folder.listFiles(f -> f.isDirectory() || f.getPath().endsWith(".class"))){
			if(file.isDirectory())
				processFolder(file);
			else
				buildFile(file);
		}
	}

	public void buildFile(File file) throws IOException {
		ClassReader reader = new ClassReader(new FileInputStream(file));
		ApiWriter writer = new ApiWriter();
		reader.accept(new LibraryReader(writer) , 0);
		File outFile = new File(outputFolder.getPath() + file.getPath().replaceFirst(inputFolder.getPath() , ""));
		File outFolder = outFile.getParentFile();

		if(outFolder == null)
			throw new IllegalStateException("File dos'ent have parent folder");
		if(!outFolder.exists() && !outFolder.mkdirs())
			throw new IllegalStateException("Could'not create folder " + outFolder.getPath());
		if(!outFile.exists() && !outFile.createNewFile())
			throw new IllegalStateException("Could'not create output file " + outFile);

		try(OutputStream out = new FileOutputStream(outFile)){
			out.write(writer.toByteArray());
		}

	}

	@OutputDirectory
	@Optional
	public File getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;
	}

	@InputDirectory
	public File getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(File inputFolder) {
		this.inputFolder = inputFolder;
	}
}
