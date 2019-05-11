package com.SegalTech.MeTalk;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class MessageRepository {
    public static final String FIRESTORE_MESSAGES_COLLECTION_NAME = "Messages";
    public static final String FIRESTORE_LOG_TAG = "Firestore";
    public static final String APPLICATION_LOG_TAG = "Application";
    private MessageDao messageDao;
//    private LiveData<List<Message>> allMessages;
    private FirebaseFirestore firestoreDB;

    MessageRepository(Application app)
    {
        MessageDatabase dbAccess = MessageDatabase.getDatabase(app);
        messageDao = dbAccess.messageDao();
//        allMessages = messageDao.getAll();
        firestoreDB = FirebaseFirestore.getInstance();
    }

    CollectionReference getAllMessages()
    {
        return firestoreDB.collection("Messages");
//        return allMessages;
    }

    void insert(Message message)
    {
        new InsertAsyncTask(messageDao, firestoreDB).execute(message);
    }

    private static class InsertAsyncTask extends AsyncTask<Message, Void, Void>
    {
        private FirebaseFirestore asyncTaskFirestoreDB;
        private MessageDao asyncTaskMessageDao;

        InsertAsyncTask(MessageDao dao, FirebaseFirestore DB)
        {
            asyncTaskMessageDao = dao;
            asyncTaskFirestoreDB = DB;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncTaskMessageDao.insert(messages[0]);
            DocumentReference firestoreMessage = asyncTaskFirestoreDB.collection(
                    FIRESTORE_MESSAGES_COLLECTION_NAME).document(
                            String.valueOf(messages[0].messageTime.getTime()));
            firestoreMessage.set(messages[0])
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(FIRESTORE_LOG_TAG, "Message inserted successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(FIRESTORE_LOG_TAG, "Failed to insert message");
                        }
                    });
            return null;
        }
    }

    void delete(Message message)
    {
        new DeleteAsyncTask(messageDao, firestoreDB).execute(message);
    }

    private static class DeleteAsyncTask extends AsyncTask<Message, Void, Void>
    {

        private FirebaseFirestore asyncTaskFirestoreDB;
        private MessageDao asyncTaskMessageDao;
        DeleteAsyncTask(MessageDao dao, FirebaseFirestore DB)
        {
            asyncTaskMessageDao = dao;
            asyncTaskFirestoreDB = DB;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncTaskMessageDao.delete(messages[0]);
            DocumentReference messageToDelete = asyncTaskFirestoreDB.collection(
                    "Messages").document(
                            String.valueOf(messages[0].messageTime.getTime()));
            messageToDelete.delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(FIRESTORE_LOG_TAG, "Message deleted successfully");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(FIRESTORE_LOG_TAG, "Failed to delete message");
                }
            });
            return null;
        }

    }

    void insertAll(List<Message> messages)
    {
        new InsertAllAsyncTask(messageDao).execute(messages);
    }

    private static class InsertAllAsyncTask extends AsyncTask<List<Message>, Void, Void>
    {

        private MessageDao asyncTaskMessageDao;
        InsertAllAsyncTask(MessageDao dao)
        {
            asyncTaskMessageDao = dao;
        }

        @Override
        protected Void doInBackground(List<Message>... lists) {
            List<Message> messages = lists[0];
            if (messages != null)
            {
                for (Message m : messages)
                {
                    asyncTaskMessageDao.insert(m);
                }
                Log.i(APPLICATION_LOG_TAG, "Current count is " + messages.size() + " messages");
            }
            else
            {
                Log.i(APPLICATION_LOG_TAG, "No messages in local database");
            }
            return null;
        }

    }
}
