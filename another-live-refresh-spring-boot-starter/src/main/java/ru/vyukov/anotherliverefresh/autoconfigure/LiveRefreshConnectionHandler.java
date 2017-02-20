package ru.vyukov.anotherliverefresh.autoconfigure;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.devtools.classpath.ClassPathChangedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiveRefreshConnectionHandler extends TextWebSocketHandler {

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

	@EventListener
	public void onContextRefreshed(ContextRefreshedEvent event) {
		log.debug("context refreshed");
		sessions.forEach(this::sendRefreshMessage);
	}

	@EventListener
	public void onClassPathChanged(ClassPathChangedEvent event) {
		log.debug("classpath resources changed");
		sessions.forEach(this::sendRefreshMessage);
	}

	private void sendRefreshMessage(WebSocketSession session) {
		try {
			session.sendMessage(refreshMessage);
		} catch (IOException e) {
			log.warn("send refresh message error", e);
		}
	}

}
