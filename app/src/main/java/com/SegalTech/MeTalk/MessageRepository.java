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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageRepository {
    public static final String FIRESTORE_MESSAGES_COLLECTION_NAME = "Messages";
    public static final String FIRESTORE_USERNAME_COLLECTION_NAME = "defaults";
    public static final String FIRESTORE_USERNAME_DOCUMENT_NAME = "username";
    public static final String FIRESTORE_USERNAME_FIELD_NAME = "username";
    public static final String FIRESTORE_LOG_TAG = "Firestore";
    public static final String USERNAME_INSERTED_SUCCESSFULLY_LOG = "Username inserted successfully";
    public static final String FAILED_TO_INSERT_USERNAME_LOG = "Failed to insert username";
    public static final String MESSAGE_INSERTED_SUCCESSFULLY_LOG = "Message inserted successfully";
    public static final String FAILED_TO_INSERT_MESSAGE_LOG = "Failed to insert message";
    public static final String MESSAGE_DELETED_SUCCESSFULLY_LOG = "Message deleted successfully";
    public static final String FAILED_TO_DELETE_MESSAGE_LOG = "Failed to delete message";

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
        return firestoreDB.collection(FIRESTORE_MESSAGES_COLLECTION_NAME);
//        return allMessages;
    }

    DocumentReference getUsername()
    {
        return firestoreDB.collection(FIRESTORE_USERNAME_COLLECTION_NAME).document(
                FIRESTORE_USERNAME_DOCUMENT_NAME);
    }

    int count()
    {
        return messageDao.count();
    }

    void insertUsername(String username)
    {
        new InsertUsernameAsyncTask(firestoreDB).execute(username);
    }

    private static class InsertUsernameAsyncTask extends AsyncTask<String, Void, Void>
    {
        private FirebaseFirestore asyncTaskFirestoreDB;

        InsertUsernameAsyncTask(FirebaseFirestore firestoreDB)
        {
            asyncTaskFirestoreDB = firestoreDB;
        }

        @Override
        protected Void doInBackground(String... username) {
            Map<String, Object> usernameDoc = new HashMap<>();
            usernameDoc.put(FIRESTORE_USERNAME_FIELD_NAME, username[0]);
            DocumentReference firestoreUsername = asyncTaskFirestoreDB.collection(
                    FIRESTORE_USERNAME_COLLECTION_NAME).document(FIRESTORE_USERNAME_DOCUMENT_NAME);
            firestoreUsername.set(usernameDoc)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(FIRESTORE_LOG_TAG, USERNAME_INSERTED_SUCCESSFULLY_LOG);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(FIRESTORE_LOG_TAG, FAILED_TO_INSERT_USERNAME_LOG);
                        }
                    });
            return null;
        }
    }

    void insertMessage(Message message)
    {
        new InsertMessageAsyncTask(messageDao, firestoreDB).execute(message);
    }

    private static class InsertMessageAsyncTask extends AsyncTask<Message, Void, Void>
    {
        private FirebaseFirestore asyncTaskFirestoreDB;
        private MessageDao asyncTaskMessageDao;

        InsertMessageAsyncTask(MessageDao dao, FirebaseFirestore DB)
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
                            Log.i(FIRESTORE_LOG_TAG, MESSAGE_INSERTED_SUCCESSFULLY_LOG);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(FIRESTORE_LOG_TAG, FAILED_TO_INSERT_MESSAGE_LOG);
                        }
                    });
            return null;
        }
    }

    void deleteMessage(Message message)
    {
        new DeleteMessageAsyncTask(messageDao, firestoreDB).execute(message);
    }

    private static class DeleteMessageAsyncTask extends AsyncTask<Message, Void, Void>
    {

        private FirebaseFirestore asyncTaskFirestoreDB;
        private MessageDao asyncTaskMessageDao;
        DeleteMessageAsyncTask(MessageDao dao, FirebaseFirestore DB)
        {
            asyncTaskMessageDao = dao;
            asyncTaskFirestoreDB = DB;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncTaskMessageDao.delete(messages[0]);
            DocumentReference messageToDelete = asyncTaskFirestoreDB.collection(
                    FIRESTORE_MESSAGES_COLLECTION_NAME).document(
                            String.valueOf(messages[0].messageTime.getTime()));
            messageToDelete.delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(FIRESTORE_LOG_TAG, MESSAGE_DELETED_SUCCESSFULLY_LOG);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(FIRESTORE_LOG_TAG, FAILED_TO_DELETE_MESSAGE_LOG);
                }
            });
            return null;
        }

    }

    void insertAllMessages(List<Message> messages)
    {
        new InsertAllMessagesAsyncTask(messageDao).execute(messages);
    }

    private static class InsertAllMessagesAsyncTask extends AsyncTask<List<Message>, Void, Void>
    {

        private MessageDao asyncTaskMessageDao;
        InsertAllMessagesAsyncTask(MessageDao dao)
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
            }
            return null;
        }
    }
}
