package ru.vyukov.anotherliverefresh.filewatch;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileChangeListenerServiceImplTest {

	private final URL thisClassFile = FileChangeListenerServiceImplTest.class.getProtectionDomain().getCodeSource()
			.getLocation();

	private List<URL> urls = Arrays.asList(thisClassFile);

	@Mock
	private FileChangeListener fileChangeListener;

	private FileChangeListenerServiceImpl fileChangeListenerServiceImpl;

	@Before
	public void before() throws IOException, URISyntaxException {
		fileChangeListenerServiceImpl = new FileChangeListenerServiceImpl(urls);
		fileChangeListenerServiceImpl.addFileChangeListener(fileChangeListener);
	}

	@Test
	public void testRun() throws Exception {
		fileChangeListenerServiceImpl.init();

		File newFolrder = new File(new File(thisClassFile.getFile()), "testFolder");
		assertTrue("failed to create folder", newFolrder.mkdir());
		newFolrder.deleteOnExit();

		TimeUnit.SECONDS.sleep(2);

		File newFile = new File(newFolrder, "testFile");
		assertTrue("failed to create file", newFile.createNewFile());
		newFile.deleteOnExit();

		TimeUnit.SECONDS.sleep(2);
		fileChangeListenerServiceImpl.destroy();

		verify(fileChangeListener).fileChange(newFolrder.toPath());
		verify(fileChangeListener).fileChange(newFile.toPath());
	}

}
