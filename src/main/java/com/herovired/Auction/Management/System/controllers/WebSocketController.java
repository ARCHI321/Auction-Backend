package com.herovired.Auction.Management.System.controllers;

//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
//    @MessageMapping("/message")
//    @SendTo("/topic/messages")
//    public String handleMessage(String message) {
//        System.out.println("webSocket " + message);
//        return message;
//    }
//}


import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketController extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Add the new session to the list
        sessions.add(session);
        System.out.println("Connection established: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Handle incoming messages
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);
        System.out.println("All sessions : " + sessions);

        // Broadcast the message to all connected sessions
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage("Broadcast: " + payload));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the closed session from the list
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    public static void sendBroadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage("Broadcast: " + message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


