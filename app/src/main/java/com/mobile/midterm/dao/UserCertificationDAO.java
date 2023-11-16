package com.mobile.midterm.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.midterm.model.Certification;
import com.mobile.midterm.model.UserCertification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserCertificationDAO {
    private static UserCertificationDAO instance;
    private FirebaseFirestore firestore;

    private FirebaseStorage storage;
    private StorageReference storageRef;


    private UserCertificationDAO() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static UserCertificationDAO getInstance() {
        if (instance == null) {
            synchronized (UserCertificationDAO.class) {
                instance = new UserCertificationDAO();
            }
        }
        return instance;
    }

    public CompletableFuture<UserCertification> addCertification(String certificationId, String userId, String detail) {
        CompletableFuture<UserCertification> completableFuture = new CompletableFuture<>();
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String formattedDate = dateFormat.format(currentDate);

        UserCertification newUserCertification = new UserCertification(certificationId, userId, detail, formattedDate, true);
        firestore.collection(UserCertification.class.getSimpleName().toLowerCase()).add(newUserCertification).addOnCompleteListener(
                task1 -> {
                    if (task1.isSuccessful()) {
                        completableFuture.complete(newUserCertification);
                    } else {
                        completableFuture.completeExceptionally(task1.getException());
                    }
                }
        ).addOnFailureListener(
                completableFuture::completeExceptionally
        );

        return completableFuture;
    }

    public CompletableFuture<List<Certification>> getUserCertificationByUserId(String userId) {
        CompletableFuture<List<Certification>> completableFuture = new CompletableFuture<>();
        firestore.collection(Certification.class.getSimpleName().toLowerCase())
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                ArrayList<Certification> result = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Certification user = documentSnapshot.toObject(Certification.class);
                                    user.setId(documentSnapshot.getId());
                                    result.add(user);
                                }
                                completableFuture.complete(result);
                            } else {
                                completableFuture.completeExceptionally(task.getException());
                            }
                        }
                ).addOnFailureListener(
                        completableFuture::completeExceptionally
                );
        return completableFuture;
    }


    public CompletableFuture<UserCertification> updateUserCertification(String id, String certificationId, String userId, String detail) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("certificationId", certificationId);
        updates.put("userId", userId);
        updates.put("detail", detail);
        CompletableFuture<UserCertification> completableFuture = new CompletableFuture<>();
        firestore.collection(UserCertification.class.getSimpleName().toLowerCase()).document(id).update(updates).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        firestore.collection(UserCertification.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                task1 -> {
                                    if (task1.isSuccessful()) {
                                        completableFuture.complete(task1.getResult().toObject(UserCertification.class));
                                    } else {
                                        completableFuture.completeExceptionally(task1.getException());
                                    }
                                }
                        ).addOnFailureListener(
                                completableFuture::completeExceptionally
                        );
                    } else {
                        completableFuture.completeExceptionally(task.getException());
                    }
                }
        ).addOnFailureListener(
                completableFuture::completeExceptionally
        );
        return completableFuture;
    }

    public CompletableFuture<UserCertification> deleteUserCertification(String id) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("activated", false);
        CompletableFuture<UserCertification> completableFuture = new CompletableFuture<>();
        firestore.collection(UserCertification.class.getSimpleName().toLowerCase()).document(id).update(updates).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        firestore.collection(UserCertification.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                task1 -> {
                                    if (task1.isSuccessful()) {
                                        completableFuture.complete(task1.getResult().toObject(UserCertification.class));
                                    } else {
                                        completableFuture.completeExceptionally(task1.getException());
                                    }
                                }
                        ).addOnFailureListener(
                                completableFuture::completeExceptionally
                        );
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
