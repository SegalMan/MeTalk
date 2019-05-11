package com.SegalTech.MeTalk;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static com.SegalTech.MeTalk.MessageRepository.FIRESTORE_LOG_TAG;

public class MessageViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;
    private MutableLiveData<List<Message>> allMessages;

    MessageViewModel(Application app)
    {
        super(app);
        messageRepository = new MessageRepository(app);
        allMessages = new MutableLiveData<>();
    }

    LiveData<List<Message>> getAllMessages()
    {
        messageRepository.getAllMessages().addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e)
                    {
                        if (e != null)
                        {
                            allMessages.postValue(null);
                            Log.e(FIRESTORE_LOG_TAG, "Listen failed", e);
                        }

                        List<Message> messageList = new ArrayList<>();
                        if (queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots)
                            {
                                if (document.getData() != null)
                                {
                                    messageList.add(new Message(document.getId(),
                                                    document.getData()));
                                }
                            }
                            allMessages.postValue(messageList);
                        }
                    }
                });
        return allMessages;
    }

    void insertAll(List<Message> messages)
    {
        messageRepository.insertAll(messages);
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
