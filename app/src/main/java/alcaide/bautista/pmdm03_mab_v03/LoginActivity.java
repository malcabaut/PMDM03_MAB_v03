package alcaide.bautista.pmdm03_mab_v03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    // Lista de proveedores de inicio de sesión (Email y Google)
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    // Lanzador de la actividad para el inicio de sesión (en lugar de startActivityForResult)
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<androidx.activity.result.ActivityResult>() {
                @Override
                public void onActivityResult(androidx.activity.result.ActivityResult result) {
                    // Manejar el resultado del inicio de sesión
                    if (result.getResultCode() == RESULT_OK) {
                        redirectToMain();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración de la vista y la autenticación
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        // Deshabilitar el botón de login hasta que se validen los campos
        binding.loginButton.setEnabled(false);

        // Configurar los eventos de clic para los botones
        binding.loginButton.setOnClickListener(v -> manualLogin());
        binding.googleSignInButton.setOnClickListener(v -> launchFirebaseUI());

        // Agregar TextWatchers para validar los campos de usuario y contraseña
        binding.username.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable editable) { validateInput(); }
        });

        binding.password.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable editable) { validateInput(); }
        });
    }

    /**
     * Método para validar si los campos de usuario y contraseña están llenos.
     * Si están completos, habilita el botón de login.
     */
    private void validateInput() {
        String username = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        binding.loginButton.setEnabled(!username.isEmpty() && !password.isEmpty());
    }

    /**
     * Método para iniciar sesión de forma manual usando el email y la contraseña.
     */
    private void manualLogin() {
        String email = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        // Mostrar el progreso mientras se intenta iniciar sesión
        binding.loading.setVisibility(View.VISIBLE);

        // Intentar iniciar sesión con las credenciales proporcionadas
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            binding.loading.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                redirectToMain();
            } else {
                createAccount(email, password);  // Si el inicio de sesión falla, intenta crear una cuenta
            }
        });
    }

    /**
     * Si la cuenta no existe, intenta crear una nueva cuenta.
     */
    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                redirectToMain();
            } else {
                Toast.makeText(this, "Error al crear cuenta.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Método para lanzar el flujo de inicio de sesión de Firebase UI (Google).
     */
    private void launchFirebaseUI() {
        try {
            // Crear el Intent para el flujo de inicio de sesión
            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)  // Proveedores disponibles (Email y Google)
                    .setLogo(R.drawable.ic_launcher_foreground)  // Logo personalizado
                    .build();
            // Usar el launcher para iniciar la actividad
            signInLauncher.launch(intent);
        } catch (Exception e) {
            Log.e("LoginActivity", "Error al iniciar FirebaseUI", e);
        }
    }

    /**
     * Método para redirigir al usuario a la actividad principal después de un inicio de sesión exitoso.
     */
    private void redirectToMain() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_id", user.getUid());  // Pasar el ID del usuario
            startActivity(intent);
            finish();  // Finalizar esta actividad
        }
    }
}
