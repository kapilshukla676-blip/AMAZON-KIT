package com.amazon.mShop.android.shopping

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LicenseGate(ctx: Context, onUnlocked: @Composable () -> Unit) {
    val deviceId = remember { LicenseManager.getDeviceId(ctx) }
    var key by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var unlocked by remember { mutableStateOf(LicenseManager.isLicensed(ctx)) }

    if (unlocked) {
        onUnlocked()
        return
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ARTHUR MODE", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("t.me/STEAMXCHEATS", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(24.dp))
            Text("Device ID", fontSize = 12.sp)
            Text(deviceId, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = key,
                onValueChange = { key = it; error = false },
                label = { Text("License Key (ARTHUR-DR-XXXXX)") },
                isError = error,
                modifier = Modifier.fillMaxWidth()
            )
            if (error) Text("Invalid key", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (LicenseManager.validate(ctx, key.trim())) {
                    LicenseManager.saveLicense(ctx, key.trim())
                    unlocked = true
                } else error = true
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Activate")
            }
        }
    }
}
