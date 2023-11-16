package com.mobile.midterm.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.midterm.model.Certification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class CertificationDAO {
    private static CertificationDAO instance;
    private FirebaseFirestore firestore;

    private FirebaseStorage storage;
    private StorageReference storageRef;


    private CertificationDAO() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static CertificationDAO getInstance() {
        if (instance == null) {
            synchronized (CertificationDAO.class) {
                instance = new CertificationDAO();
            }
        }
        return instance;
    }

    public CompletableFuture<Certification> addCertification(String name, String detail, boolean activated) {
        CompletableFuture<Certification> completableFuture = new CompletableFuture<>();
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        Certification newCertification = new Certification(name, detail, formattedDate, activated);
        firestore.collection(Certification.class.getSimpleName().toLowerCase()).add(newCertification).addOnCompleteListener(
                task1 -> {
                    if (task1.isSuccessful()) {
                        completableFuture.complete(newCertification);
                    } else {
                        completableFuture.completeExceptionally(task1.getException());
                    }
                }
        ).addOnFailureListener(
                completableFuture::completeExceptionally
        );
        return completableFuture;
    }

    public CompletableFuture<List<Certification>> getAllCertification() {
        CompletableFuture<List<Certification>> completableFuture = new CompletableFuture<>();
        firestore.collection(Certification.class.getSimpleName().toLowerCase())
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

    public CompletableFuture<List<Certification>> getCertificationByIds(List<String> ids) {
        CompletableFuture<List<Certification>> completableFuture = new CompletableFuture<>();

        firestore.collection(Certification.class.getSimpleName().toLowerCase())
                .whereIn("id", ids)
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

    public CompletableFuture<Certification> updateCertification(String id, String name, String detail, boolean activated) {
        HashMap<String, Object> updates = new HashMap<>();
        AtomicBoolean hasExisted = new AtomicBoolean(false);
        updates.put("name", name);
        updates.put("detail", detail);
        updates.put("activated", activated);
        CompletableFuture<Certification> completableFuture = new CompletableFuture<>();
        firestore.collection(Certification.class.getSimpleName().toLowerCase()).whereEqualTo("id", id).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            firestore.collection(Certification.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            Certification temp = task1.getResult().toObject(Certification.class);
                                            if (!temp.getName().equals(name)) {
                                                completableFuture.completeExceptionally(new Exception("Certification has already existed"));
                                                hasExisted.set(true);
                                            }
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
        if (!hasExisted.get()) {
            firestore.collection(Certification.class.getSimpleName().toLowerCase()).document(id).update(updates).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            firestore.collection(Certification.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            completableFuture.complete(task1.getResult().toObject(Certification.class));
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
        }
        return completableFuture;
    }

}
