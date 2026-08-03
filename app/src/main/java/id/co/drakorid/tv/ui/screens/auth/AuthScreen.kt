package id.co.drakorid.tv.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.co.drakorid.tv.ui.components.TvLoadingIndicator
import androidx.tv.material3.Text
import id.co.drakorid.tv.ui.theme.TvColors

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(520.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DRAKORID TV",
                color = TvColors.primary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            var isRegisterMode by remember { mutableStateOf(false) }

            // Name field (register only)
            if (isRegisterMode) {
                AuthTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Nama",
                    isFirst = true
                )
            }

            AuthTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "Email",
                isFirst = !isRegisterMode
            )

            AuthTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "Password",
                isPassword = true,
                isFirst = false
            )

            // Error
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = TvColors.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            // Submit button
            if (uiState.isLoading) {
                TvLoadingIndicator(
                    modifier = Modifier.padding(top = 20.dp)
                )
            } else {
                AuthActionButton(
                    text = if (isRegisterMode) "Daftar" else "Masuk",
                    onClick = {
                        if (isRegisterMode) viewModel.register() else viewModel.login()
                    }
                )
            }

            // Toggle mode
            AuthLinkButton(
                text = if (isRegisterMode) "Sudah punya akun? Masuk" else "Belum punya akun? Daftar",
                onClick = {
                    isRegisterMode = !isRegisterMode
                    viewModel.clearError()
                }
            )

            // Back
            AuthLinkButton(
                text = "← Kembali ke beranda",
                onClick = onBack
            )
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isFirst: Boolean
) {
    var focused by remember { mutableStateOf(false) }
    val requester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .then(if (isFirst) Modifier.focusRequester(requester) else Modifier)
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .background(
                color = if (focused) TvColors.focusBackground else TvColors.cardBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (focused) {
                    Modifier.border(2.dp, TvColors.focusBorder, RoundedCornerShape(8.dp))
                } else {
                    Modifier.border(1.dp, TvColors.cardBorder, RoundedCornerShape(8.dp))
                }
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = TvColors.textMuted,
                fontSize = 16.sp
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = TvColors.textPrimary,
                fontSize = 16.sp
            ),
            cursorBrush = SolidColor(TvColors.focusBorder),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AuthActionButton(
    text: String,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .background(
                color = if (focused) TvColors.primary else TvColors.focusBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (focused) TvColors.primary else TvColors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AuthLinkButton(
    text: String,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (focused) TvColors.focusBorder else TvColors.textSecondary,
            fontSize = 13.sp
        )
    }
}
