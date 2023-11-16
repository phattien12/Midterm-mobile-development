package com.mobile.midterm.dao;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.midterm.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserDAO {
    private static UserDAO instance;
    private FirebaseFirestore firestore;

    private FirebaseStorage storage;
    private StorageReference storageRef;


    private UserDAO() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            synchronized (UserDAO.class) {
                instance = new UserDAO();
            }
        }
        return instance;
    }

    public CompletableFuture<User> addManager(String username, String phone, String email, boolean activated, int age, String fullName, String password) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String formattedDate = dateFormat.format(currentDate);

        User newUser = new User(email, age, phone, fullName, formattedDate, password, "manager", activated, null, username);
        firestore.collection(User.class.getSimpleName().toLowerCase()).add(newUser).addOnCompleteListener(
                task1 -> {
                    if (task1.isSuccessful()) {
                        completableFuture.complete(newUser);
                    } else {
                        completableFuture.completeExceptionally(task1.getException());
                    }
                }
        ).addOnFailureListener(
                completableFuture::completeExceptionally
        );

        return completableFuture;
    }

    public CompletableFuture<User> addStudent(String username, String phone, String email, boolean activated, int age, String fullName, String password) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String formattedDate = dateFormat.format(currentDate);

        User newUser = new User(email, age, phone, fullName, formattedDate, password, "student", activated, null, username);
        firestore.collection(User.class.getSimpleName().toLowerCase()).add(newUser).addOnCompleteListener(
                task1 -> {
                    if (task1.isSuccessful()) {
                        completableFuture.complete(newUser);
                    } else {
                        completableFuture.completeExceptionally(task1.getException());
                    }
                }
        ).addOnFailureListener(
                completableFuture::completeExceptionally
        );

        return completableFuture;
    }

    public CompletableFuture<List<User>> getSystemStudents() {
        CompletableFuture<List<User>> completableFuture = new CompletableFuture<>();
        firestore.collection(User.class.getSimpleName().toLowerCase())
                .whereIn("role", Arrays.asList("student"))
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                ArrayList<User> result = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    User user = documentSnapshot.toObject(User.class);
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

    public CompletableFuture<List<User>> getSystemUsers() {
        CompletableFuture<List<User>> completableFuture = new CompletableFuture<>();
        firestore.collection(User.class.getSimpleName().toLowerCase()).whereIn("role", Arrays.asList("manager", "student")).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        ArrayList<User> result = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = documentSnapshot.toObject(User.class);
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

    public CompletableFuture<String> updateUserImage(Uri selectedImageURI, String userId) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        StorageReference imageRef = storageRef.child("images/" + selectedImageURI.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(selectedImageURI);

        uploadTask.addOnFailureListener(completableFuture::completeExceptionally).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
            downloadUrl.addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("image", imageUrl);
                firestore.collection(User.class.getSimpleName().toLowerCase()).document(userId).update(updates).addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                completableFuture.complete(imageUrl);
                            } else {
                                completableFuture.completeExceptionally(task.getException());
                            }
                        }
                ).addOnFailureListener(
                        completableFuture::completeExceptionally
                );
            }).addOnFailureListener(
                    completableFuture::completeExceptionally
            );
        });

        return completableFuture;
    }

    public CompletableFuture<User> updateManager(String id, String username, String phone, String email, boolean activated, int age, String fullName, String password) {
        HashMap<String, Object> updates = new HashMap<>();
        AtomicBoolean hasExisted = new AtomicBoolean(false);
        updates.put("username", username);
        updates.put("phoneNum", phone);
        updates.put("password", password);
        updates.put("fullName", fullName);
        updates.put("email", email);
        updates.put("age", age);
        updates.put("activated", activated);
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        firestore.collection(User.class.getSimpleName().toLowerCase()).whereEqualTo("email", email).whereEqualTo("role", "manager").get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            User temp = task1.getResult().toObject(User.class);
                                            if (!temp.getEmail().equals(email)) {
                                                completableFuture.completeExceptionally(new Exception("User has already existed"));
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
            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).update(updates).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            completableFuture.complete(task1.getResult().toObject(User.class));
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

    public CompletableFuture<User> updateStudent(String id, String username, String phone, String email, boolean activated, int age, String fullName, String password) {
        HashMap<String, Object> updates = new HashMap<>();
        AtomicBoolean hasExisted = new AtomicBoolean(false);
        updates.put("username", username);
        updates.put("phoneNum", phone);
        updates.put("password", password);
        updates.put("fullName", fullName);
        updates.put("email", email);
        updates.put("age", age);
        updates.put("activated", activated);
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        firestore.collection(User.class.getSimpleName().toLowerCase()).whereEqualTo("email", email)
                .whereEqualTo("role", "student").get().addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                            task1 -> {
                                                if (task1.isSuccessful()) {
                                                    User temp = task1.getResult().toObject(User.class);
                                                    if (!temp.getEmail().equals(email)) {
                                                        completableFuture.completeExceptionally(new Exception("User has already existed"));
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
            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).update(updates).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            completableFuture.complete(task1.getResult().toObject(User.class));
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

    public CompletableFuture<User> updateProfile(String id, String username, String email, int age, String fullName, String phoneNum) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        HashMap<String, Object> updates = new HashMap<>();
        AtomicBoolean hasExisted = new AtomicBoolean(false);
        updates.put("username", username);
        updates.put("phoneNum", phoneNum);
        updates.put("fullName", fullName);
        updates.put("email", email);
        updates.put("age", age);
        firestore.collection(User.class.getSimpleName().toLowerCase()).whereEqualTo("email", email).whereEqualTo("role", "manager").get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            User temp = task1.getResult().toObject(User.class);
                                            if (temp != null && !temp.getEmail().equals(email)) {
                                                completableFuture.completeExceptionally(new Exception("User has already existed"));
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
            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).update(updates).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            firestore.collection(User.class.getSimpleName().toLowerCase()).document(id).get().addOnCompleteListener(
                                    task1 -> {
                                        if (task1.isSuccessful()) {
                                            User temp = task1.getResult().toObject(User.class);
                                            temp.setId(id);
                                            completableFuture.complete(temp);
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
