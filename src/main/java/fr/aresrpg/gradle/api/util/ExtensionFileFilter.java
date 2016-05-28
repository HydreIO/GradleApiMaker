package fr.aresrpg.gradle.api.util;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter implements FileFilter{
	private String extension;

	public ExtensionFileFilter(String extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(File file) {
		return file.isDirectory() || file.getPath().endsWith('.' + extension);
	}
}
