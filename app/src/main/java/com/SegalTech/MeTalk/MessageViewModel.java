package com.SegalTech.MeTalk;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;


public class MessageViewModel extends AndroidViewModel {

    public static String ERROR_RETRIEVING_MESSAGES_LOG = "LIsten failed";
    public static String ERROR_RETRIEVING_USERNAME_LOG = "Error retrieving username";
    public static String USERNAME_NOT_FOUND_LOG = "Username not found";

    private MessageRepository messageRepository;
    private MutableLiveData<List<Message>> allMessages;
    private MutableLiveData<String> username;
    private Executor executor = Executors.newCachedThreadPool();

    MessageViewModel(Application app) {
        super(app);
        messageRepository = new MessageRepository(app);
        allMessages = new MutableLiveData<>();
        username = new MutableLiveData<>();
        messageRepository.getAllMessages().addSnapshotListener(
                new EventListener<QuerySnapshot>()
            {
                @Override
                public void onEvent(@Nullable final QuerySnapshot queryDocumentSnapshots,
                                    @Nullable final FirebaseFirestoreException e) {

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (e != null) {
                                allMessages.postValue(null);
                                Log.e(MessageRepository.FIRESTORE_LOG_TAG,
                                        ERROR_RETRIEVING_MESSAGES_LOG, e);
                            }

                            List<Message> messageList = new ArrayList<>();
                            if (queryDocumentSnapshots != null) {
                                for (DocumentSnapshot document : queryDocumentSnapshots) {
                                    if (document.getData() != null) {
                                        messageList.add(new Message(document.getId(),
                                                document.getData()));
                                    }
                                }
                                allMessages.postValue(messageList);
                            }
                        }
                    });
                }
            });

        messageRepository.getUsername().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            if ((doc != null) && (doc.exists()) && (doc.getData() != null) &&
                                    (doc.getData().containsKey(MessageRepository.
                                            FIRESTORE_USERNAME_FIELD_NAME)))
                            {
                                username.postValue((String)doc.getData().get(
                                        MessageRepository.FIRESTORE_USERNAME_FIELD_NAME));
                            }
                            else {
                                username.postValue("");
                                Log.d(MessageRepository.FIRESTORE_LOG_TAG,
                                        USERNAME_NOT_FOUND_LOG);
                            }
                        }
                        else {
                            Log.d(MessageRepository.FIRESTORE_LOG_TAG,
                                    ERROR_RETRIEVING_USERNAME_LOG);
                        }
                    }
                });
            }
        });
    }

    LiveData<List<Message>> getAllMessages()
    {
        return allMessages;
    }

    LiveData<String> getUsername()
    {
        return username;
    }

    void insertUsername(String username) {
        messageRepository.insertUsername(username);
        this.username.postValue(username);
    }

    void insertAllMessages(List<Message> messages)
    {
        messageRepository.insertAllMessages(messages);
    }

    void insertMessage(Message message)
    {
        messageRepository.insertMessage(message);
    }

    void deleteMessage(Message message)
    {
        messageRepository.deleteMessage(message);
    }

    int countMessages() {return messageRepository.count(); }
}
