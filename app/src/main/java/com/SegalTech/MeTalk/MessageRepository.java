package com.SegalTech.MeTalk;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class MessageRepository {
    private MessageDao messageDao;
    private LiveData<List<Message>> allMessages;

    MessageRepository(Application app)
    {
        MessageDatabase dbAccess = MessageDatabase.getDatabase(app);
        messageDao = dbAccess.messageDao();
        allMessages = messageDao.getAll();
    }

    LiveData<List<Message>> getAllMessages()
    {
        return allMessages;
    }

    void insert(Message message)
    {
        new InsertAsyncTask(messageDao).execute(message);
    }

    private static class InsertAsyncTask extends AsyncTask<Message, Void, Void>
    {
        private MessageDao asyncTaskMessageDao;

        InsertAsyncTask(MessageDao dao)
        {
            asyncTaskMessageDao = dao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncTaskMessageDao.insert(messages[0]);
            return null;
        }
    }

    void delete(Message message)
    {
        new DeleteAsyncTask(messageDao).execute(message);
    }

    private static class DeleteAsyncTask extends AsyncTask<Message, Void, Void>
    {
        private MessageDao asyncTaskMessageDao;

        DeleteAsyncTask(MessageDao dao)
        {
            asyncTaskMessageDao = dao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            asyncTaskMessageDao.delete(messages[0]);
            return null;
        }
    }
}
