package com.github.denpeshkov.automergeservice;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
  List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    session.sendMessage(new TextMessage("Hello from server " + message.getPayload() + " !"));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // the messages will be broadcasted to all users.
    sessions.add(session);
  }
}
