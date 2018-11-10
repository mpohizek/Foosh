package foosh.air.foi.hr.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeDatabase {

    private DatabaseReference mDatabase;

    public RealtimeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void updateUser(final User userToUpdate, Context context) {
        final onDataListener dataListener = (onDataListener) context; /*
        mDatabase.child("users").child(userToUpdate.uid).setValue(userToUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataListener.updateUI(userToUpdate);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                }); */
    }

}
