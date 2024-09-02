package com.game.factory;

import com.game.enums.ResponseOption;
import com.game.service.MessageProcessor;
import com.game.service.NoResponseProcessor;
import com.game.service.ResponseBackProcessor;

public class MessageProcessorFactory {
    private MessageProcessorFactory() { }

    public static MessageProcessor createProcessor(ResponseOption responseOption) {

        if (responseOption == ResponseOption.JUST_PRINT) {
            return new NoResponseProcessor();
        } else if (responseOption == ResponseOption.RESPOND_BACK) {
            return new ResponseBackProcessor();
        }
        throw new IllegalArgumentException("Invalid response option");
    }
}
