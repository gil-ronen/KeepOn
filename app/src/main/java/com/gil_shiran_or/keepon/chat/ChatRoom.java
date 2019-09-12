package com.gil_shiran_or.keepon.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoom
{
    private String userId1;
    private String userId2;
    private List<Message> messages = new ArrayList<>();

    public ChatRoom() {
    }

    public ChatRoom(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public boolean isUsersIdExistInThisChatRoom(String userId1, String userId2)
    {
        return (userId1.equals(this.userId1) && userId2.equals(this.userId2) || userId1.equals(this.userId2) && userId2.equals(this.userId1));
    }

    public void addMessage(Message message)
    {
        this.messages.add(message);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId1", userId1);
        result.put("userId2", userId2);

        return result;
    }
}
