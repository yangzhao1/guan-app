package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/11.
 */

public class MessageData {
    private List<WarningMessage> messages;

    public List<WarningMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<WarningMessage> messages) {
        this.messages = messages;
    }
}
