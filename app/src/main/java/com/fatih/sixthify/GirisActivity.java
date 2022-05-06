package com.fatih.sixthify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisActivity extends AppCompatActivity {

    private EditText Ad, Soyad, Telefon, Mail, Sifre, TekrarSifre;
    private String txtAd, txtSoyad, txtTelefon, txtMail, txtSifre, txtTekrarSifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    Button activity_giris_kayitbutonu;
    Button activity_giris_girisbutonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        Mail = (EditText)findViewById(R.id.giris_yap_Mail);
        Sifre = (EditText)findViewById(R.id.giris_yap_Sifre);

        mAuth = FirebaseAuth.getInstance();

        initView();
        initEvent();
    }

    private void initView() {
        activity_giris_kayitbutonu = (Button)findViewById(R.id.kayitbutonu);
        activity_giris_girisbutonu = (Button)findViewById(R.id.girisbutonu);
    }

    private void initEvent() {
        activity_giris_kayitbutonu.setOnClickListener(this::kayitSayfasi);
        activity_giris_girisbutonu.setOnClickListener(this::girisYap);

    }


    public void girisYap(View v){

        txtMail = Mail.getText().toString();
        txtSifre = Sifre.getText().toString();

        if (!TextUtils.isEmpty(txtMail) && !TextUtils.isEmpty(txtSifre)){
            mAuth.signInWithEmailAndPassword(txtMail, txtSifre)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(GirisActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();

                        Intent anaAktiviteyeGec = new Intent(GirisActivity.this, AnaActivity.class);
                        startActivity(anaAktiviteyeGec);

                         /* System.out.println("Kullanıcı Adı:" + mUser.getDisplayName());
                        System.out.println("Kullanıcı ID:" + mUser.getUid());
                        System.out.println("Kullanıcı Telefonu:" + mUser.getPhoneNumber());
                        System.out.println("Kullanıcı Maili:" + mUser.getEmail()); */

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GirisActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        }
        else
            Toast.makeText(this, "Alanlar boş bırakalamaz.", Toast.LENGTH_SHORT).show();
    }

    public void kayitSayfasi(View v){
        Intent kaydaGec = new Intent(GirisActivity.this, KayitActivity.class);
        startActivity(kaydaGec);
    }
}