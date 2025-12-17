package DAY6.WhatsApp.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import DAY6.WhatsApp.services.*;
import DAY6.WhatsApp.models.*;
import DAY6.WhatsApp.enums.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

public class WhatsAppServer {
    private static final UserService userService = UserService.getInstance();
    private static final ContactService contactService = ContactService.getInstance();
    private static final ChatService chatService = ChatService.getInstance();
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/api/users/register", new RegisterHandler());
        server.createContext("/api/users/login", new LoginHandler());
        server.createContext("/api/contacts", new ContactsHandler());
        server.createContext("/api/chats", new ChatsHandler());
        server.createContext("/api/messages", new MessagesHandler());
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("WhatsApp Server started on http://localhost:8080");
    }
    
    static class RegisterHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = readRequestBody(exchange);
                String name = extractJsonValue(body, "name");
                String phone = extractJsonValue(body, "phone");
                
                try {
                    User user = userService.registerUser(name, phone);
                    String json = "{\"success\":true,\"userId\":\"" + user.getUserID() + 
                                "\",\"name\":\"" + user.getUserName() + 
                                "\",\"phone\":\"" + user.getUserMobileNumber() + "\"}";
                    sendResponse(exchange, 200, json);
                } catch (Exception e) {
                    String json = "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}";
                    sendResponse(exchange, 400, json);
                }
            }
        }
    }
    
    static class LoginHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = readRequestBody(exchange);
                String phone = extractJsonValue(body, "phone");
                
                try {
                    User user = userService.getUserByMobileNumber(phone);
                    String json = "{\"success\":true,\"userId\":\"" + user.getUserID() + 
                                "\",\"name\":\"" + user.getUserName() + 
                                "\",\"phone\":\"" + user.getUserMobileNumber() + "\"}";
                    sendResponse(exchange, 200, json);
                } catch (Exception e) {
                    String json = "{\"success\":false,\"error\":\"User not found\"}";
                    sendResponse(exchange, 401, json);
                }
            }
        }
    }
    
    static class ContactsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            String query = exchange.getRequestURI().getQuery();
            String userId = getQueryParam(query, "userId");
            
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Contact> contacts = contactService.getContactsForUser(userId);
                StringBuilder json = new StringBuilder("{\"contacts\":[");
                for (int i = 0; i < contacts.size(); i++) {
                    Contact c = contacts.get(i);
                    json.append("{\"contactId\":\"").append(c.getContactID())
                        .append("\",\"name\":\"").append(c.getContactFirstName()).append(" ").append(c.getContactLastName())
                        .append("\",\"phone\":\"").append(c.getContactMobileNumber()).append("\"}");
                    if (i < contacts.size() - 1) json.append(",");
                }
                json.append("]}");
                sendResponse(exchange, 200, json.toString());
            } else if ("POST".equals(exchange.getRequestMethod())) {
                String body = readRequestBody(exchange);
                String phone = extractJsonValue(body, "phone");
                String firstName = extractJsonValue(body, "firstName");
                String lastName = extractJsonValue(body, "lastName");
                
                Contact contact = contactService.addContact(userId, phone, firstName, lastName, "", "");
                String json = "{\"success\":true,\"contactId\":\"" + contact.getContactID() + "\"}";
                sendResponse(exchange, 200, json);
            }
        }
    }
    
    static class ChatsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            String query = exchange.getRequestURI().getQuery();
            String userId = getQueryParam(query, "userId");
            
            List<ChatConversation> chats = chatService.getChatsForUser(userId);
            StringBuilder json = new StringBuilder("{\"chats\":[");
            for (int i = 0; i < chats.size(); i++) {
                ChatConversation chat = chats.get(i);
                User otherUser = chat.getUser1().getUserID().equals(userId) ? chat.getUser2() : chat.getUser1();
                String lastMsg = chat.getLastMessage() != null ? chat.getLastMessage().getMessageContent() : "";
                json.append("{\"chatId\":\"").append(chat.getChatID())
                    .append("\",\"otherUserId\":\"").append(otherUser.getUserID())
                    .append("\",\"otherUserName\":\"").append(otherUser.getUserName())
                    .append("\",\"lastMessage\":\"").append(lastMsg.replace("\"", "\\\"")).append("\"}");
                if (i < chats.size() - 1) json.append(",");
            }
            json.append("]}");
            sendResponse(exchange, 200, json.toString());
        }
    }
    
    static class MessagesHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String chatId = getQueryParam(query, "chatId");
                
                Map<LocalDateTime, Message> messages = chatService.getMessages(chatId);
                StringBuilder json = new StringBuilder("{\"messages\":[");
                if (messages != null) {
                    int i = 0;
                    for (Message m : messages.values()) {
                        json.append("{\"messageId\":\"").append(m.getMessageID())
                            .append("\",\"content\":\"").append(m.getMessageContent().replace("\"", "\\\""))
                            .append("\",\"senderId\":\"").append(m.getSender().getUserID())
                            .append("\",\"senderName\":\"").append(m.getSender().getUserName())
                            .append("\",\"time\":\"").append(m.getMessageTime().toString()).append("\"}");
                        if (i++ < messages.size() - 1) json.append(",");
                    }
                }
                json.append("]}");
                sendResponse(exchange, 200, json.toString());
            } else if ("POST".equals(exchange.getRequestMethod())) {
                String body = readRequestBody(exchange);
                String senderId = extractJsonValue(body, "senderId");
                String receiverId = extractJsonValue(body, "receiverId");
                String content = extractJsonValue(body, "content");
                
                ChatConversation chat = chatService.getOrCreateChat(senderId, receiverId);
                Message message = chatService.sendMessage(chat.getChatID(), senderId, content, MessageType.TEXT);
                
                String json = "{\"success\":true,\"messageId\":\"" + message.getMessageID() + "\"}";
                sendResponse(exchange, 200, json);
            }
        }
    }
    
    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String html = getIndexHtml();
            byte[] bytes = html.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }
    
    private static void enableCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }
    
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes());
    }
    
    private static void sendResponse(HttpExchange exchange, int code, String json) throws IOException {
        byte[] bytes = json.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
    
    private static String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }
    
    private static String getQueryParam(String query, String key) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals(key)) return pair.length > 1 ? pair[1] : null;
        }
        return null;
    }
    
    private static String getIndexHtml() {
        return "<!DOCTYPE html><html><head><title>WhatsApp Chat</title><style>*{margin:0;padding:0;box-sizing:border-box}body{font-family:Arial,sans-serif;background:#e5ddd5}.container{max-width:1200px;margin:0 auto;height:100vh;display:flex}.sidebar{width:350px;background:#fff;border-right:1px solid #ddd}.main{flex:1;display:flex;flex-direction:column;background:#e5ddd5}.header{padding:15px;background:#075e54;color:#fff;font-size:18px;display:flex;justify-content:space-between}.header button{padding:5px 10px;background:#128c7e;color:#fff;border:none;border-radius:5px;cursor:pointer;font-size:12px}.chat-list{overflow-y:auto;height:calc(100vh - 60px)}.chat-item{padding:15px;border-bottom:1px solid #eee;cursor:pointer}.chat-item:hover{background:#f5f5f5}.chat-item.active{background:#ebebeb}.chat-header{padding:15px;background:#075e54;color:#fff}.messages{flex:1;overflow-y:auto;padding:20px}.message{margin:10px 0;padding:10px 15px;border-radius:8px;max-width:60%;clear:both;float:left}.message.sent{background:#dcf8c6;float:right}.message.received{background:#fff}.input-area{padding:15px;background:#f0f0f0;display:flex;gap:10px;clear:both}.input-area input{flex:1;padding:10px;border:1px solid #ddd;border-radius:20px}.input-area button{padding:10px 20px;background:#075e54;color:#fff;border:none;border-radius:20px;cursor:pointer}.login-screen{position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);display:flex;justify-content:center;align-items:center;z-index:1000}.login-box{background:#fff;padding:30px;border-radius:10px;width:350px}.login-box h2{margin-bottom:20px;color:#075e54}.login-box input{width:100%;padding:10px;margin:10px 0;border:1px solid #ddd;border-radius:5px}.login-box button{width:100%;padding:12px;background:#075e54;color:#fff;border:none;border-radius:5px;cursor:pointer;margin:5px 0}.hidden{display:none!important}</style></head><body><div id='loginScreen' class='login-screen'><div class='login-box'><h2>WhatsApp Chat</h2><input id='nameInput' placeholder='Your Name'/><input id='phoneInput' placeholder='Phone Number'/><button onclick='register()'>Register</button><button onclick='login()'>Login</button></div></div><div id='appContainer' class='container hidden'><div class='sidebar'><div class='header'>Contacts<button onclick='showNewContact()'>+ Add</button></div><div id='chatList' class='chat-list'></div></div><div class='main'><div class='chat-header'><span id='chatTitle'>Select a contact to start chatting</span></div><div id='messages' class='messages'></div><div class='input-area'><input id='messageInput' placeholder='Type a message' onkeypress='if(event.key===\"Enter\")sendMessage()'/><button onclick='sendMessage()'>Send</button></div></div></div><script>const API='http://localhost:8080/api';let currentUser=null;let currentChat=null;let currentReceiver=null;let allContacts=[];window.onload=function(){const saved=localStorage.getItem('whatsappUser');if(saved){currentUser=JSON.parse(saved);document.getElementById('loginScreen').classList.add('hidden');document.getElementById('appContainer').classList.remove('hidden');loadContacts()}};async function register(){const name=document.getElementById('nameInput').value;const phone=document.getElementById('phoneInput').value;if(!name||!phone){alert('Please enter name and phone');return}const res=await fetch(API+'/users/register',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({name,phone})});const data=await res.json();if(data.success){currentUser=data;localStorage.setItem('whatsappUser',JSON.stringify(data));document.getElementById('loginScreen').classList.add('hidden');document.getElementById('appContainer').classList.remove('hidden');loadContacts()}else{alert('Registration failed: '+data.error)}}async function login(){const phone=document.getElementById('phoneInput').value;if(!phone){alert('Please enter phone number');return}const res=await fetch(API+'/users/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({phone})});const data=await res.json();if(data.success){currentUser=data;localStorage.setItem('whatsappUser',JSON.stringify(data));document.getElementById('loginScreen').classList.add('hidden');document.getElementById('appContainer').classList.remove('hidden');loadContacts()}else{alert('Login failed: User not found')}}async function loadContacts(){const res=await fetch(API+'/contacts?userId='+currentUser.userId);const data=await res.json();allContacts=data.contacts;renderContactList()}function renderContactList(){const chatList=document.getElementById('chatList');chatList.innerHTML='';if(allContacts.length===0){chatList.innerHTML='<div style=\"padding:20px;text-align:center;color:#999\">No contacts yet. Click + Add to add someone!</div>';return}allContacts.forEach(contact=>{const div=document.createElement('div');div.className='chat-item';div.textContent=contact.name;div.onclick=()=>startChatWithContact(contact);chatList.appendChild(div)})}async function startChatWithContact(contact){try{const loginRes=await fetch(API+'/users/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({phone:contact.phone})});const loginData=await loginRes.json();if(loginData.success){currentReceiver=loginData.userId;currentChat=null;document.getElementById('chatTitle').textContent=contact.name;document.querySelectorAll('.chat-item').forEach(el=>el.classList.remove('active'));event.target.classList.add('active');loadMessages()}else{alert('Contact \"'+contact.name+'\" has not registered yet. Ask them to sign up first!')}}catch(e){alert('Could not connect to contact')}}async function loadMessages(){if(!currentReceiver){document.getElementById('messages').innerHTML='';return}const existingRes=await fetch(API+'/chats?userId='+currentUser.userId);const existingData=await existingRes.json();const existing=existingData.chats.find(c=>c.otherUserId===currentReceiver);if(existing){currentChat=existing.chatId}if(!currentChat){document.getElementById('messages').innerHTML='<div style=\"text-align:center;color:#999;padding:20px\">No messages yet. Start the conversation!</div>';return}const res=await fetch(API+'/messages?chatId='+currentChat);const data=await res.json();const messagesDiv=document.getElementById('messages');messagesDiv.innerHTML='';data.messages.forEach(msg=>{const div=document.createElement('div');div.className='message '+(msg.senderId===currentUser.userId?'sent':'received');div.textContent=msg.content;messagesDiv.appendChild(div)});messagesDiv.scrollTop=messagesDiv.scrollHeight}async function sendMessage(){const input=document.getElementById('messageInput');const content=input.value.trim();if(!content||!currentReceiver)return;await fetch(API+'/messages',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({senderId:currentUser.userId,receiverId:currentReceiver,content})});input.value='';setTimeout(loadMessages,100)}function showNewContact(){const phone=prompt('Enter contact phone number:');if(!phone)return;const firstName=prompt('Enter first name:');if(!firstName)return;const lastName=prompt('Enter last name:')||'';addContact(phone,firstName,lastName)}async function addContact(phone,firstName,lastName){if(phone===currentUser.phone){alert('Cannot add yourself as contact!');return}await fetch(API+'/contacts?userId='+currentUser.userId,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({phone,firstName,lastName})});loadContacts()}</script></body></html>";
    }
}
