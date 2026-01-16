package com.example.voyage_map.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.voyage_map.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val showLanguageDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showLanguageDialog.value) {
        LanguageSelectionDialog(
            onLanguageSelected = { languageCode ->
                changeLanguage(context, languageCode)
                showLanguageDialog.value = false
            },
            onDismiss = { showLanguageDialog.value = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_settings)) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 16.dp)
        ) {
            SettingsGroup(stringResource(R.string.settings_appearance)) {
                SwitchSettingItem(icon = Icons.Default.DarkMode, title = stringResource(R.string.settings_dark_mode), initialChecked = false)
                SettingItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.settings_language),
                    value = getCurrentLanguage(),
                    onClick = { showLanguageDialog.value = true }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SettingsGroup(stringResource(R.string.settings_about)) {
                SettingItem(icon = Icons.Default.Info, title = stringResource(R.string.settings_app_version), value = "1.0.0", onClick = {})
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(onLanguageSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val languages = mapOf("English" to "en", "Français" to "fr", "हिन्दी" to "hi")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_language)) },
        text = {
            Column {
                languages.forEach { (languageName, languageCode) ->
                    Text(
                        text = languageName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(languageCode) }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        },
        confirmButton = {}
    )
}

fun changeLanguage(context: Context, languageCode: String) {
    val appLocale = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)

    // Restart activity to apply language change
    val activity = context.findActivity()
    activity?.recreate()
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun getCurrentLanguage(): String {
    val languageMap = mapOf("en" to "English", "fr" to "Français", "hi" to "हिन्दी")
    val currentLocale = AppCompatDelegate.getApplicationLocales()[0]
    val languageCode = currentLocale?.language
    return languageMap[languageCode] ?: currentLocale?.displayName ?: stringResource(R.string.settings_language_value)
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(icon: ImageVector, title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f))
        Text(text = value, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SwitchSettingItem(icon: ImageVector, title: String, initialChecked: Boolean) {
    val (checked, setChecked) = remember { mutableStateOf(initialChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = setChecked)
    }
}
