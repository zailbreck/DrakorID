package id.co.drakorid.tv.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.co.drakorid.tv.ui.components.PhoneLoadingIndicator
import id.co.drakorid.tv.ui.theme.TvColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoggedIn: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoggedIn()
    }

    var isRegisterMode by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF020617)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("DRAKORID", color = TvColors.primary, fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
            Spacer(modifier = Modifier.height(32.dp))

            if (isRegisterMode) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nama") },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = fieldColors(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                colors = fieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                colors = fieldColors(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            uiState.error?.let { error ->
                Text(error, color = TvColors.error, fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
            }

            if (uiState.isLoading) {
                PhoneLoadingIndicator(modifier = Modifier.padding(top = 20.dp))
            } else {
                Button(
                    onClick = { if (isRegisterMode) viewModel.register() else viewModel.login() },
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TvColors.focusBackground),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isRegisterMode) "Daftar" else "Masuk",
                        color = TvColors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            TextButton(onClick = { isRegisterMode = !isRegisterMode; viewModel.clearError() }) {
                Text(
                    text = if (isRegisterMode) "Sudah punya akun? Masuk" else "Belum punya akun? Daftar",
                    color = TvColors.textSecondary,
                    fontSize = 13.sp
                )
            }

            TextButton(onClick = onBack) {
                Text("← Kembali ke beranda", color = TvColors.textMuted, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TvColors.textPrimary,
    unfocusedTextColor = TvColors.textPrimary,
    focusedBorderColor = TvColors.primary,
    unfocusedBorderColor = TvColors.cardBorder,
    focusedLabelColor = TvColors.primary,
    unfocusedLabelColor = TvColors.textMuted,
    cursorColor = TvColors.primary
)
