package ru.vyukov.anotherliverefresh.filewatch;

import java.nio.file.Path;

public interface FileChangeListener {

	public void fileChange(Path fullPath);

}
