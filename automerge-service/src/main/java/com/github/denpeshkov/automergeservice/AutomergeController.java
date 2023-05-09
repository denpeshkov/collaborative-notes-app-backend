package com.github.denpeshkov.automergeservice;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AutomergeController {
  @MessageMapping("/{noteId}")
  @SendTo("/topic/automerge/{noteId}")
  public String greeting(String message) throws Exception {
    System.out.println("Message from client: " + message);
    return message;
  }
}
