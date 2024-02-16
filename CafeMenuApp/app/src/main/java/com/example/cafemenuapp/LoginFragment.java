package com.example.cafemenuapp;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class LoginFragment extends Fragment {


    EditText emailText1, passwordText1;
    Button login_Btn1;
    ProgressBar progressBar1;
    TextView signup_Btn;
    private String SECRET_KEY = "aesEncryptionKey";
    private String INIT_VECTOR = "encryptionIntVec";


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailText1 = view.findViewById(R.id.emailText1);
        passwordText1 = view.findViewById(R.id.passwordText2);
        login_Btn1 = view.findViewById(R.id.login_Btn1);
        signup_Btn = view.findViewById(R.id.signup_text_view_btn);


        progressBar1 = view.findViewById(R.id.progress_bar1);
        login_Btn1.setOnClickListener(v -> {
            String enteredEmail = emailText1.getText().toString();
            String enteredPwd = encrypt(passwordText1.getText().toString());


            if(checkLoginCredentials(enteredEmail, enteredPwd))
            {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, new CafeMenu());
                transaction.commit();
                // Login successful, perform necessary actions
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show();




            } else {
                // Login failed, show an error message
                Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
            }

        });
        signup_Btn.setOnClickListener((v) -> {
            // Assuming you are working with a FragmentTransaction in the hosting activity
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, new SignupFragment());
            transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
            transaction.commit();
        });



        return view;

    }
    private boolean checkLoginCredentials(String enteredEmail, String enteredPwd) {
        Cursor cursor = requireActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI,
                null,
                MyContentProvider.username + "=? AND " + MyContentProvider.password+ "=?",
                new String[]{enteredEmail, enteredPwd},
                null
        );
        boolean loginSuccessful = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return loginSuccessful;
    }

    public String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

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
