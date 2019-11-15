package emrizkiem.dev.roomexample.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import emrizkiem.dev.roomexample.database.Note;
import emrizkiem.dev.roomexample.database.NoteDao;
import emrizkiem.dev.roomexample.database.NoteDatabase;

public class NoteRepository {
    private NoteDao mNoteDao;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();

        NoteDatabase db = NoteDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mNoteDao.getAllNotes();
    }

    public void insert(final Note note) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.insert(note);
            }
        });
    }

    public void update(final Note note) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.update(note);
            }
        });
    }

    public void delete(final Note note) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao.delete(note);
            }
        });
    }
}
