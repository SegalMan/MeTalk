package com.SegalTech.MeTalk;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

class MessageViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;
    private LiveData<List<Message>> allMessages;

    MessageViewModel(Application app)
    {
        super(app);
        messageRepository = new MessageRepository(app);
        allMessages = messageRepository.getAllMessages();
    }

    LiveData<List<Message>> getAllMessages()
    {
        return allMessages;
    }

    void insert(Message message)
    {
        messageRepository.insert(message);
    }

    void delete(Message message)
    {
        messageRepository.delete(message);
    }
}
