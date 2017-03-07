
package com.example.system.chatapp.beans;

import java.util.Date;

public class Message {

    private String content;
    private long timestamp;
    private String sender;
    private String receiver;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Message(){

    }

    public Message(String content,String sender,String receiver){
        this.content=content;
        this.sender=sender;
        this.receiver=receiver;
        timestamp=new Date().getTime();
    }


    /**
     * 
     * @return
     *     The content
     */
    public String getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 
     * @return
     *     The sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * 
     * @param sender
     *     The sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

}
