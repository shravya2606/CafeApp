package com.example.cafemenuapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SignupFragment extends Fragment {


    EditText emailText, passwordText;
    Button signupBtn;
    ProgressBar progressBar;
    TextView loginBtn;
    View view;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);
        signupBtn = view.findViewById(R.id.signupBtn);

        loginBtn = view.findViewById(R.id.login_text_view_btn);
        progressBar = view.findViewById(R.id.progress_bar);
        signupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText userNameText = view.findViewById(R.id.emailText);
                EditText pwd = view.findViewById(R.id.passwordText);

                String encryptedPwd =  encrypt(pwd.getText().toString());
                ContentValues values = new ContentValues();
                values.put(MyContentProvider.username,userNameText.getText().toString());
                values.put(MyContentProvider.password,encryptedPwd);
                Uri uri = getContext().getContentResolver().insert(Uri.parse("content://com.example.cafemenuapp.provider/Orders"), values);

                userNameText.setText("");
                pwd.setText("");
                onClickShowDetails(view);

            }
        });

        loginBtn.setOnClickListener((v) -> {
            // Assuming you are working with a FragmentTransaction in the hosting activity
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, new LoginFragment());
            transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
            transaction.commit();
        });


        return view;
    }

    @SuppressLint("Range")
    public void onClickShowDetails(View view) {
// inserting complete table details in this text field
        // creating a cursor object of the
// content URI
        Cursor cursor = requireActivity().getContentResolver().query(MyContentProvider.CONTENT_URI,
                null, null, null, null);
// iteration of the cursor
// to print whole table
        if (cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                strBuild.append("\n").
                        append(cursor.getString(cursor.getColumnIndex(MyContentProvider.username))).
                        append("-").append(cursor.getString(cursor.getColumnIndex(MyContentProvider.password)));

                cursor.moveToNext();
            }
            Toast.makeText(getContext(), "User created", Toast.LENGTH_SHORT).show();
            Log.i("DataBase", String.valueOf(strBuild));
        } else {
            Log.i("DataBase", "No Records Found");
        }
    }


    public String encrypt(String value) {
        try {
            String INIT_VECTOR = "encryptionIntVec";
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            String SECRET_KEY = "aesEncryptionKey";
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}