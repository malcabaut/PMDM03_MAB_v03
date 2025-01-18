package alcaide.bautista.pmdm03_mab_v03;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import alcaide.bautista.pmdm03_mab_v03.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // Instancia de ViewBinding para acceder a las vistas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración de ViewBinding para acceder a las vistas de la actividad
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuración del BottomNavigationView (barra de navegación inferior)
        BottomNavigationView navView = binding.navView;

        // Configuración del NavController que manejará la navegación entre fragmentos
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Vincular el BottomNavigationView con el NavController para manejar la navegación
        NavigationUI.setupWithNavController(navView, navController);
    }
}
