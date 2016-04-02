package gui;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FileChangedThread extends Thread {
	private WatchService watchService;
	private Path path;
	private TranslationViewerGUI gui;
	
	public FileChangedThread(String folder, TranslationViewerGUI gui) {
		this.gui = gui;
		
		path = FileSystems.getDefault().getPath(folder);
		
		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			WatchKey watchKey = null;
			try {
				watchKey = watchService.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			List<WatchEvent<?>> events = watchKey.pollEvents();
	        
        	if (!gui.isActive() && !events.isEmpty()) {
        		// external file change has occurred
        		gui.setExternalChange(true);
        	}
        	
	        watchKey.reset();
		}
	}
}
