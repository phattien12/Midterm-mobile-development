package com.mobile.midterm.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobile.midterm.model.User;

import java.util.concurrent.CompletableFuture;


public class AuthenticationDAO {
    private static AuthenticationDAO instance;
    private FirebaseFirestore firestore;

    private AuthenticationDAO() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static AuthenticationDAO getInstance() {
        if (instance == null) {
            synchronized (AuthenticationDAO.class) {
                instance = new AuthenticationDAO();
            }
        }
        return instance;
    }

    public CompletableFuture<User> login(String email, String password) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        firestore.collection(User.class.getSimpleName().toLowerCase())
                .whereEqualTo("email", email).whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshots = task.getResult();
                                if (snapshots.size() == 0 || snapshots.size() > 1) {
                                    completableFuture.completeExceptionally(new Exception("User does not exist"));
                                } else {
                                    for (QueryDocumentSnapshot doc : snapshots) {
                                        User user = doc.toObject(User.class);
                                        user.setId(doc.getId());
                                        completableFuture.complete(user);
                                    }
                                }
                            } else {
                                completableFuture.completeExceptionally(task.getException());
                            }
                        }
                ).addOnFailureListener(
                        completableFuture::completeExceptionally
                );
        return completableFuture;
    }
}
