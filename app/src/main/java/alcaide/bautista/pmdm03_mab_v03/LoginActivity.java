package alcaide.bautista.pmdm03_mab_v03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity"; // Etiqueta de log para esta actividad
    private static final int RC_SIGN_IN = 1; // Código de solicitud para el inicio de sesión

    private ActivityLoginBinding binding; // Instancia de ViewBinding para acceder a las vistas
    private FirebaseAuth firebaseAuth; // Instancia de FirebaseAuth para manejar la autenticación

    // Lista de proveedores de autenticación que soporta FirebaseUI (Email y Google)
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),  // Autenticación por correo electrónico
            new AuthUI.IdpConfig.GoogleBuilder().build()  // Autenticación por Google
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar ViewBinding para acceder a las vistas de la actividad
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Deshabilitar el botón de login inicialmente
        binding.login.setEnabled(false);

        // Configurar el botón de inicio de sesión manual
        binding.login.setOnClickListener(v -> manualLogin());

        // Configurar el botón de inicio de sesión con Google
        binding.googleSignInButton.setOnClickListener(v -> {
            Log.d(TAG, "Botón de Google Sign-In clicado");
            launchFirebaseUI();
        });

        // Validación de los campos de entrada (usuario y contraseña)
        binding.username.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable editable) {
                validateInput(); // Validar la entrada después de cada cambio
            }
        });

        binding.password.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable editable) {
                validateInput(); // Validar la entrada después de cada cambio
            }
        });
    }

    // Método para validar la entrada del usuario y habilitar/deshabilitar el botón de login
    private void validateInput() {
        String username = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        // Habilitar el botón de login solo si ambos campos no están vacíos
        binding.login.setEnabled(!username.isEmpty() && !password.isEmpty());
    }

    // Método para realizar el inicio de sesión manual con correo y contraseña
    private void manualLogin() {
        String email = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        binding.loading.setVisibility(View.VISIBLE); // Mostrar indicador de carga
        Log.d(TAG, "Intentando iniciar sesión con el email: " + email);

        // Intentar iniciar sesión con correo y contraseña
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    binding.loading.setVisibility(View.GONE); // Ocultar indicador de carga
                    if (task.isSuccessful()) {
                        // Si el inicio de sesión es exitoso, redirigir a la actividad principal
                        Log.d(TAG, "Inicio de sesión exitoso para: " + email);
                        redirectToMain();
                    } else {
                        // Si el inicio de sesión falla, intentar crear una nueva cuenta
                        Log.e(TAG, "Error de inicio de sesión: " + task.getException().getMessage());
                        Toast.makeText(this, "Error de inicio de sesión. Intentando crear cuenta.", Toast.LENGTH_SHORT).show();
                        createAccount(email, password);
                    }
                });
    }

    // Método para crear una nueva cuenta de usuario
    private void createAccount(String email, String password) {
        Log.d(TAG, "Creando cuenta para: " + email);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si la cuenta se crea exitosamente, redirigir a la actividad principal
                        Log.d(TAG, "Cuenta creada exitosamente para: " + email);
                        redirectToMain();
                    } else {
                        // Si ocurre un error al crear la cuenta
                        Log.e(TAG, "Error al crear cuenta: " + task.getException().getMessage());
                        Toast.makeText(this, "Error al crear cuenta.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para lanzar el flujo de autenticación de FirebaseUI para Google Sign-In
    private void launchFirebaseUI() {
        Log.d(TAG, "launchFirebaseUI: Iniciando FirebaseUI para Google Sign-In");

        try {
            // Crear un Intent para iniciar el flujo de autenticación de FirebaseUI
            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers) // Proveedores de autenticación (Email y Google)
                    .setLogo(R.drawable.ic_launcher_foreground) // Opcional: Añadir logo
                    .build();
            startActivityForResult(intent, RC_SIGN_IN); // Iniciar el flujo de autenticación
        } catch (Exception e) {
            Log.e(TAG, "launchFirebaseUI: Error al iniciar FirebaseUI", e);
        }
    }

    // Método para redirigir a la pantalla principal después de un inicio de sesión exitoso
    private void redirectToMain() {
        Intent intent = new Intent(this, MainActivity.class); // Crear un intent hacia MainActivity
        startActivity(intent);
        finish(); // Finalizar la actividad actual (LoginActivity)
    }

    // Manejar el resultado del inicio de sesión de FirebaseUI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Si el inicio de sesión es exitoso con Google, redirigir a la pantalla principal
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "Inicio de sesión exitoso con Google para: " + user.getEmail());
                redirectToMain();
            } else {
                // Si el inicio de sesión falla, mostrar el error correspondiente
                if (response == null) {
                    Log.w(TAG, "onActivityResult: El usuario canceló el inicio de sesión");
                    Toast.makeText(this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onActivityResult: Error en el inicio de sesión", response.getError());
                    Toast.makeText(this, "Error al iniciar sesión: " + response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
