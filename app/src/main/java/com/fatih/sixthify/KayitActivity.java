package com.fatih.sixthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class KayitActivity extends AppCompatActivity {

    private EditText Ad, Soyad, Telefon, Mail, Sifre, TekrarSifre;
    private String txtAd, txtSoyad, txtTelefon, txtMail, txtSifre, txtTekrarSifre;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        Ad = (EditText)findViewById(R.id.kayit_ol_Ad);
        Soyad = (EditText)findViewById(R.id.kayit_ol_Soyad);
        Telefon = (EditText)findViewById(R.id.kayit_ol_Telefon);
        Mail = (EditText)findViewById(R.id.kayit_ol_Mail);
        Sifre = (EditText)findViewById(R.id.kayit_ol_Sifre);
        TekrarSifre = (EditText)findViewById(R.id.kayit_ol_tekrarSifre);

        mAuth = FirebaseAuth.getInstance();


    }

    public void kayitOl(View v){
        txtAd = Ad.getText().toString();
        txtSoyad = Soyad.getText().toString();
        txtTelefon = Telefon.getText().toString();
        txtMail = Mail.getText().toString();
        txtSifre = Sifre.getText().toString();
        txtTekrarSifre = TekrarSifre.getText().toString();

        if (!TextUtils.isEmpty(txtAd) && !TextUtils.isEmpty(txtSoyad) && !TextUtils.isEmpty(txtMail) && !TextUtils.isEmpty(txtSifre) && !TextUtils.isEmpty(txtTekrarSifre)){
            if (txtSifre.matches(txtTekrarSifre)){

                mAuth.createUserWithEmailAndPassword(txtMail, txtSifre)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(KayitActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(KayitActivity.this, task.getException().getLocalizedMessage(),  Toast.LENGTH_SHORT).show();

                            }
                        });

            }
            else {
                Toast.makeText(this, "Şifreler aynı olmalıdır.", Toast.LENGTH_SHORT).show();
            }



        }
        else {

            Toast.makeText(this, "Ad, Soyad, Email ve Şifre boş bırakılamaz.", Toast.LENGTH_SHORT).show();

        }


    }

}