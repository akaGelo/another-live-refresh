package ru.vyukov.anotherliverefresh.ws;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;
import ru.vyukov.anotherliverefresh.filewatch.FileChangeListener;

@Slf4j
public class LiveRefreshConnectionHandler extends TextWebSocketHandler implements FileChangeListener {

	private Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

	private final TextMessage refreshMessage = new TextMessage("refresh");

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		sessions.remove(session);

	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);
	}

	private void sendRefreshMessage(WebSocketSession session) {
		try {
			session.sendMessage(refreshMessage);
		} catch (IOException e) {
			log.warn("send refresh message error", e);
		}
	}

	@Override
	public void fileChange(Path fullPach) {
		sessions.forEach(this::sendRefreshMessage);
	}

}
