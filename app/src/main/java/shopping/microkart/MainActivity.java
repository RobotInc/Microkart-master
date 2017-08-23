package shopping.microkart;


import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
