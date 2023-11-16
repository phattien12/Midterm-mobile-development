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
import java.util.concurrent.atomic.AtomicBoolean;

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

    public CompletableFuture<UserCertification> addUserCertification(String certificationId, String userId, String detail) {
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

    public CompletableFuture<List<UserCertification>> getAllUserCertification() {
        CompletableFuture<List<UserCertification>> completableFuture = new CompletableFuture<>();
        firestore.collection(UserCertification.class.getSimpleName().toLowerCase())
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                ArrayList<UserCertification> result = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    UserCertification user = documentSnapshot.toObject(UserCertification.class);
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

    public CompletableFuture<List<UserCertification>> getUserCertificationByUserId(String userId) {
        CompletableFuture<List<UserCertification>> completableFuture = new CompletableFuture<>();
        firestore.collection(UserCertification.class.getSimpleName().toLowerCase())
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                ArrayList<UserCertification> result = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    UserCertification user = documentSnapshot.toObject(UserCertification.class);
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


    public CompletableFuture<UserCertification> updateUserCertification(String id, String certificationId, String userId, String detail, boolean activated) {
        HashMap<String, Object> updates = new HashMap<>();
        AtomicBoolean hasExisted = new AtomicBoolean(false);
        updates.put("certificationId", certificationId);
        updates.put("userId", userId);
        updates.put("detail", detail);
        updates.put("activated", activated);
        CompletableFuture<UserCertification> completableFuture = new CompletableFuture<>();
        firestore.collection(Certification.class.getSimpleName().toLowerCase()).whereEqualTo("id", id).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            firestore.collection(Certification.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            Certification temp = task1.getResult().toObject(Certification.class);
                                            
                                        } else {
                                            completableFuture.completeExceptionally(task1.getException());
                                        }
                                    }
                            ).addOnFailureListener(
                                    completableFuture::completeExceptionally
                            );
                        }
                    } else {
                        completableFuture.completeExceptionally(task.getException());
                    }
                }
        ).addOnFailureListener(
                completableFuture::completeExceptionally
        );
        if (!hasExisted.get())
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
