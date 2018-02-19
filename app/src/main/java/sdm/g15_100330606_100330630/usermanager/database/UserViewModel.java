package sdm.g15_100330606_100330630.usermanager.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;

import java.util.List;

import sdm.g15_100330606_100330630.usermanager.Constant;
import sdm.g15_100330606_100330630.usermanager.R;

public class UserViewModel extends AndroidViewModel {

    private UserDatabase db;

    public UserViewModel(Application application) {
        super(application);

        db = Room.databaseBuilder( application, UserDatabase.class, Constant.DB_NAME).build();
    }

    public void insertAll( List<User> newUsers ) {
        db.userDao().insertAll( newUsers );
    }

    public LiveData<List<User>> getAll() {
        return db.userDao().getAll();
    }
}
