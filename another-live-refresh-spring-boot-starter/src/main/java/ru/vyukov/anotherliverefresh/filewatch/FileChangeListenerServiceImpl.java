package ru.vyukov.anotherliverefresh.filewatch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import ru.vyukov.anotherliverefresh.autoconfigure.AnotherLiveRefreshProperties;

/**
 * TODO перевод
 * 
 * Этот сервис следит за изменениями в classpath и уведомляет подписчиков при
 * обнаружении. <br/>
 * <br/>
 * Созданные каталоги добавляются в список отслеживания.
 * 
 * @author gelo
 *
 */
@Slf4j
public class FileChangeListenerServiceImpl extends SimpleFileVisitor<Path>
		implements FileChangeListenerService, Runnable {

	private Thread watchThread;

	private volatile boolean run = true;

	private WatchService watchService;

	private Set<FileChangeListener> fileChangeListeners = new HashSet<>();

	private AnotherLiveRefreshProperties conf;

	public FileChangeListenerServiceImpl(java.util.List<URL> urls, AnotherLiveRefreshProperties conf)
			throws IOException, URISyntaxException {
		this.conf = conf;
		watchService = FileSystems.getDefault().newWatchService();

		// recursive register all classpath folders
		for (URL url : urls) {
			Files.walkFileTree(Paths.get(url.toURI()), this);
		}
	}

	@PostConstruct
	public void init() throws IOException {
		watchThread = new Thread(this);
		watchThread.start();

	}

	@PreDestroy
	public void destroy() throws IOException {
		run = false;
		// see watchService.close(); after run(){ while(run){...}}
	}

	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public void run() {
		WatchKey key = null;
		while (run) {
			try {
				key = watchService.poll(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				continue;
			}
			if (null == key) {
				continue;
			}
			// Итерации для каждого события
			for (WatchEvent<?> event : key.pollEvents()) {
				onFsEvent(key, event);
			}
			//
			key.reset();
		}

		try {
			watchService.close();
		} catch (IOException e) {
			log.error("watchService close problem", e);
		}
	}

	private void onFsEvent(WatchKey key, WatchEvent<?> event) {
		Path dir = (Path) key.watchable();
		Path fullPath = dir.resolve((Path) event.context());
		Kind<?> kind = event.kind();

		if (conf.isIgnorePath(fullPath)) {
			log.debug("Ignore FS event: " + kind.name() + " " + fullPath);
			return;
		}

		log.debug("FS event: " + kind.name() + " " + fullPath);
		fileChangeListeners.forEach(l -> l.fileChange(fullPath));

		if ("ENTRY_CREATE".equals(kind.name())) {
			// регистрация новго каталога
			try {
				Files.walkFileTree(fullPath, this);
			} catch (IOException e) {
				log.error("watch register error", e);
			}
		}
	}

	public boolean addFileChangeListener(FileChangeListener fileChangeListener) {
		return fileChangeListeners.add(fileChangeListener);
	}

	public boolean removeFileChangeListener(FileChangeListener fileChangeListener) {
		return fileChangeListeners.remove(fileChangeListener);
	}

}
