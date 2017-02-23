package ru.vyukov.anotherliverefresh.filewatch;

import java.nio.file.Path;

public interface FileChangeListener {

	/**
	 * Invoked when an change occurs.
	 * 
	 * @param fullPath
	 */
	public void fileChange(Path fullPath);

}
