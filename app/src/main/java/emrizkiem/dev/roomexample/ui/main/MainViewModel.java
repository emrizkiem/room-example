package emrizkiem.dev.roomexample.ui.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import emrizkiem.dev.roomexample.database.Note;
import emrizkiem.dev.roomexample.repository.NoteRepository;

public class MainViewModel extends ViewModel {
    private NoteRepository mNoteRepository;

    public MainViewModel(Application application) {
        mNoteRepository = new NoteRepository(application);
    }

    LiveData<List<Note>> getAllNotes() {
        return mNoteRepository.getAllNotes();
    }
}
