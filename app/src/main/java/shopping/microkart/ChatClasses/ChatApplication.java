package shopping.microkart.ChatClasses;


import com.google.firebase.database.FirebaseDatabase;

/**
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class ChatApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
          FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
