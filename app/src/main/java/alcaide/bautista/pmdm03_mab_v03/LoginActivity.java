package alcaide.bautista.pmdm03_mab_v03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        binding.login.setEnabled(false);

        binding.login.setOnClickListener(v -> manualLogin());
        binding.googleSignInButton.setOnClickListener(v -> launchFirebaseUI());

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

    private void validateInput() {
        String username = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        binding.login.setEnabled(!username.isEmpty() && !password.isEmpty());
    }

    private void manualLogin() {
        String email = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        binding.loading.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            binding.loading.setVisibility(View.GONE);
            if (task.isSuccessful()) redirectToMain();
            else createAccount(email, password);
        });
    }

    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) redirectToMain();
            else Toast.makeText(this, "Error al crear cuenta.", Toast.LENGTH_SHORT).show();
        });
    }

    private void launchFirebaseUI() {
        try {
            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_launcher_foreground)
                    .build();
            startActivityForResult(intent, RC_SIGN_IN);
        } catch (Exception e) {
            Log.e("LoginActivity", "Error al iniciar FirebaseUI", e);
        }
    }

    private void redirectToMain() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_id", user.getUid());
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) redirectToMain();
            else Toast.makeText(this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show();
        }
    }
}
