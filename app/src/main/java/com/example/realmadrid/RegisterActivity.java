package com.example.realmadrid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    //creando variables para edittext y textview, firebase auth, button y barra de progreso.
    private TextInputEditText userNameEdt, passwordEdt, confirmPwdEdt;
    private TextView loginTV;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // inicializando todas nuestras variables.
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        loadingPB = findViewById(R.id.idPBLoading);
        confirmPwdEdt = findViewById(R.id.idEdtConfirmPassword);
        loginTV = findViewById(R.id.idTVLoginUser);
        registerBtn = findViewById(R.id.idBtnRegister);
        mAuth = FirebaseAuth.getInstance();

        //agragando click para login tv.
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abriendo el login activity con clicking login text.
                Intent i = new Intent(RegisterActivity.this, com.example.realmadrid.LoginActivity.class);
                startActivity(i);
            }
        });
        //agregando click listener para register button.
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ocultar nuestra barra de progreso.
                loadingPB.setVisibility(View.VISIBLE);
                // obteniendo datos  fro =m  su edit text..
                String userName = userNameEdt.getText().toString();
                String pwd = passwordEdt.getText().toString();
                String cnfPwd = confirmPwdEdt.getText().toString();
                //comprobando si la contrase??a y la contrase??a de confirmaci??n son iguales o no.
                if (!pwd.equals(cnfPwd)) {
                    Toast.makeText(RegisterActivity.this, "Porfavor compruebe si ambos contrase??as son iguales..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd)) {
                    //comprobando si los campos de texto est??n vac??os o no.
                    Toast.makeText(RegisterActivity.this, "Por favor ingrese sus credenciales..", Toast.LENGTH_SHORT).show();
                } else {
                    // en la l??nea de abajo estamos creando un nuevo usuario al pasar el correo electr??nico y la contrase??a.
                    mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // en la l??nea de abajo estamos comprobando si la tarea es exitosa o no.
                            if (task.isSuccessful()) {
                                // en el m??todo de ??xito estamos ocultando nuestra barra de progreso y abriendo una actividad de inicio de sesi??n.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, com.example.realmadrid.LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                //en otra condici??n, estamos mostrando un toast mesaje de falla.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Fail to register user..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}