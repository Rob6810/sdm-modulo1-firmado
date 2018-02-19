package sdm.g15_100330606_100330630.usermanager.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users")
    LiveData<List<User>> getAll();

    @Insert
    void insertAll(List<User> users);

    @Delete
    void delete(User user);
}
