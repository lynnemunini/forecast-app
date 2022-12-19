package com.grayseal.forecastapp.location


import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.*
import com.grayseal.forecastapp.navigation.WeatherNavigation


/*request permission from user, if user already denied, show simple alert dialog info
message otherwise show custom full screen dialog.*/

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permission: String,
) {
    val permissionState = rememberPermissionState(permission)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                shouldShowRationale = shouldShowRationale
            ) { permissionState.launchPermissionRequest() }
        },
        content = {

        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
            content()
        }
    }
}

@Composable
fun Content(showButton: Boolean = true, onClick: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialogLocation(
                title = "Turn On Location Service",
                desc = "We understand that your privacy is important, and we only request access to your location in order to provide you with the best possible weather experience.\n\n" +
                        "Without this permission you will have to manually enter your location.",
                enableLocation,
                onClick
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current

    if (shouldShowRationale) {
        Toast.makeText(context, "Permission to access location denied", Toast.LENGTH_LONG).show()
    } else {
        Content(onClick = onRequestPermission)
    }

}