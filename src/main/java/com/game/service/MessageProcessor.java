package com.game.service;

import com.game.entity.Message;

import java.io.Serializable;

public interface MessageProcessor extends Serializable {

    void processMessage(Message message);
}
