package com.example.proyectomastermartini.ui.screens.profile
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectomastermartini.ui.theme.MasterMartiniTheme
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomastermartini.model.ContentCatalog
import com.example.proyectomastermartini.ui.components.AppTextField
import com.example.proyectomastermartini.ui.components.AppTopBar
import com.example.proyectomastermartini.ui.components.ChipSelector
import com.example.proyectomastermartini.ui.components.ConfirmDialog
import com.example.proyectomastermartini.ui.components.DropdownField
import com.example.proyectomastermartini.ui.components.PrimaryButton
import com.example.proyectomastermartini.ui.components.SectionHeader
import com.example.proyectomastermartini.ui.components.UserAvatar
import com.example.proyectomastermartini.ui.theme.MartiniSuccess
import com.example.proyectomastermartini.util.LocationHelper
import com.example.proyectomastermartini.util.PhotoStorage
import com.example.proyectomastermartini.viewmodel.ProfileViewModel
import java.io.File


@Composable
fun ProfileScreen(
    largeText: Boolean,
    onLargeTextChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focus = LocalFocusManager.current

    var message by remember { mutableStateOf<String?>(null) }
    var showSettingsButton by remember { mutableStateOf(false) }
    var locating by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    // Ruta de la foto en curso: se guarda para sobrevivir a una recreación de la Activity
    var pendingPhotoPath by rememberSaveable { mutableStateOf<String?>(null) }

    // ---------- Recurso nativo 1: CÁMARA ----------
    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val path = pendingPhotoPath
        if (success && path != null) {
            viewModel.onPhotoChanged(path)
            message = null
        } else if (path != null) {
            File(path).delete()
        }
        pendingPhotoPath = null
    }

    fun launchCamera() {
        val file = PhotoStorage.newPhotoFile(context)
        pendingPhotoPath = file.absolutePath
        takePicture.launch(PhotoStorage.uriFor(context, file))
    }

    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            showSettingsButton = false
            launchCamera()
        } else {
            message = "Sin permiso de cámara no podemos tomar la foto. Puedes elegir una imagen de la galería."
            showSettingsButton = true
        }
    }

    // Galería: el selector del sistema no requiere permisos
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val path = PhotoStorage.importFromGallery(context, uri)
            if (path != null) {
                viewModel.onPhotoChanged(path)
                message = null
            } else {
                message = "No pudimos leer la imagen seleccionada."
            }
        }
    }

    // ---------- Recurso nativo 2: UBICACIÓN ----------
    fun fetchLocation() {
        locating = true
        LocationHelper.getCurrentLocation(context) { location ->
            locating = false
            if (location != null) {
                viewModel.onLocationChanged(location.latitude, location.longitude)
                message = null
            } else {
                message = "No pudimos obtener tu ubicación. Revisa que el GPS esté activado e inténtalo de nuevo."
            }
        }
    }

    val locationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        if (result.values.any { it }) {
            showSettingsButton = false
            fetchLocation()
        } else {
            message = "Sin permiso de ubicación no podemos registrar la zona de tu negocio."
            showSettingsButton = true
        }
    }

    fun onCameraClick() {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (granted) launchCamera() else cameraPermission.launch(Manifest.permission.CAMERA)
    }

    fun onLocationClick() {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (granted) fetchLocation()
        else locationPermission.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
    }

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Mi perfil")
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---- Foto ----
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                UserAvatar(photoPath = state.photoPath, name = state.name, size = 112.dp)
                Text(state.name.ifBlank { "Tu perfil" }, style = MaterialTheme.typography.titleLarge)
                Text(state.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = ::onCameraClick, modifier = Modifier.weight(1f).height(52.dp)) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Cámara", style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = { pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        modifier = Modifier.weight(1f).height(52.dp)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Galería", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            AnimatedVisibility(
                visible = message != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Warning, null, tint = MaterialTheme.colorScheme.error)
                            Spacer(Modifier.width(8.dp))
                            Text(message.orEmpty(), color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.weight(1f))
                        }
                        if (showSettingsButton) {
                            TextButton(onClick = {
                                context.startActivity(
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                                )
                            }) { Text("Abrir ajustes de la app", style = MaterialTheme.typography.labelLarge) }
                        }
                    }
                }
            }

            HorizontalDivider()
            SectionHeader("Datos personales")
            AppTextField(state.name, viewModel::onName, "Nombre completo", Icons.Filled.Person, error = state.nameError)
            AppTextField(
                state.phone, viewModel::onPhone, "Celular", Icons.Filled.Phone,
                error = state.phoneError, keyboardType = KeyboardType.Phone
            )

            HorizontalDivider()
            SectionHeader("Tu negocio", subtitle = "Nos ayuda a mostrarte contenido útil")
            DropdownField(
                value = state.businessType, onSelected = viewModel::onBusinessType,
                options = ContentCatalog.businessTypes, label = "Tipo de negocio",
                leadingIcon = Icons.Filled.Business, error = state.typeError
            )
            DropdownField(
                value = state.businessSize, onSelected = viewModel::onBusinessSize,
                options = ContentCatalog.businessSizes, label = "Tamaño aproximado",
                leadingIcon = Icons.Filled.Groups, error = state.sizeError
            )
            var extra by rememberSaveable { mutableStateOf(false) }
            val showExtra = extra || state.rutError != null || state.addressError != null
            TextButton(onClick = { extra = !extra }) {
                Text(
                    if (showExtra) "Ocultar datos opcionales" else "Agregar datos opcionales (RUT, dirección, ubicación)",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            AnimatedVisibility(visible = showExtra, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppTextField(
                        state.companyRut, viewModel::onRut, "RUT de empresa (opcional)", Icons.Filled.Badge,
                        error = state.rutError, hint = "Dato ficticio. Ej: 76.123.456-0"
                    )
                    AppTextField(
                        state.address, viewModel::onAddress, "Dirección (opcional)", Icons.Filled.Home,
                        error = state.addressError, imeAction = ImeAction.Done, onDone = { focus.clearFocus() }
                    )

                    // ---- Ubicación ----
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.LocationOn, null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("Ubicación del negocio", style = MaterialTheme.typography.titleMedium)
                                    val lat = state.latitude
                                    val lon = state.longitude
                                    Text(
                                        if (lat != null && lon != null) LocationHelper.format(lat, lon) else "Aún no registrada",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            OutlinedButton(onClick = ::onLocationClick, enabled = !locating, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                                Icon(Icons.Filled.MyLocation, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(if (locating) "Buscando ubicación…" else "Usar mi ubicación actual", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
            SectionHeader("Intereses", subtitle = "Elige los temas que más te interesan")
            ChipSelector(
                options = ContentCatalog.interests,
                selected = state.interests,
                onToggle = viewModel::toggleInterest,
                error = state.interestsError
            )

            PrimaryButton(
                text = "Guardar cambios",
                onClick = { focus.clearFocus(); viewModel.save() },
                loading = state.saving
            )
            AnimatedVisibility(visible = state.saved, enter = fadeIn() + expandVertically(), exit = fadeOut()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, null, tint = MartiniSuccess)
                    Spacer(Modifier.width(8.dp))
                    Text("Perfil guardado correctamente", color = MartiniSuccess, style = MaterialTheme.typography.bodyLarge)
                }
            }

            HorizontalDivider()
            SectionHeader("Accesibilidad")
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.TextFields, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Texto grande", style = MaterialTheme.typography.titleMedium)
                    Text("Aumenta el tamaño de las letras en toda la app", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = largeText, onCheckedChange = onLargeTextChange)
            }

            HorizontalDivider()
            OutlinedButton(onClick = { showLogoutDialog = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = "Cerrar sesión",
            message = "¿Seguro que quieres salir de tu cuenta?",
            confirmText = "Cerrar sesión",
            onConfirm = { showLogoutDialog = false; onLogout() },
            onDismiss = { showLogoutDialog = false }
        )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    var isLargeText by remember { mutableStateOf(false) }
    MasterMartiniTheme {
        ProfileScreen(
            largeText = isLargeText,
            onLargeTextChange = {isLargeText = it},
            onLogout = {},
        )
    }
}
