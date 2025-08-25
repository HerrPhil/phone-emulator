package com.phone.emulator.ui

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phone.emulator.ui.transformation.PhoneNumberTransformation
import com.phone.emulator.ui.viewmodels.PhoneInfoUiState
import com.phone.emulator.ui.viewmodels.PhoneInfoViewModel
import kotlinx.coroutines.delay
import kotlin.math.min

private const val TAG: String = "The phone app"

@Composable
fun PhoneApp(
    viewModel: PhoneInfoViewModel = viewModel()
) {

    // maintain state at the app level, then pass it down
    val phoneInfoUiState by viewModel.phoneInfoUiState.collectAsState()

    // maintain state change lambda expression, then pass it down
    val onPhoneInfoChanged: (String) -> Unit = {

        Log.i(TAG, "on phone info changed $it")

        viewModel.onPhoneInfoChanged(it)

    }

    val ctx = LocalContext.current

    val onDial: () -> Unit = {

        Log.i(TAG, "TODO on dial the phone - delegate to view model")

        viewModel.onDial { number ->

            val phoneUri = "tel: $number".toUri()

            val phoneIntent = Intent(Intent.ACTION_DIAL, phoneUri)

            try {

                // Launch the Phone app's dialer with a phone number to dial a call

                ctx.startActivity(phoneIntent)

            } catch (ex: SecurityException) {

                Toast.makeText(ctx, "An error occurred", Toast.LENGTH_LONG).show()

            }
        }

    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding: PaddingValues ->

        PhoneFeature(
            phoneInfoUiState,
            onPhoneInfoChanged,
            onDial,
            modifier = Modifier.padding(innerPadding)
        )

    }
}

@Composable
fun PhoneFeature(
    phoneInfoUiState: PhoneInfoUiState,
    onValueChanged: (String) -> Unit,
    onDial: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier,
        propagateMinConstraints = true
    ) {
        PhoneNumberValue(
            phoneInfoUiState,
            onValueChanged,
            onDial,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )
    }

}

@Composable
fun PhoneNumberValue(
    phoneInfoUiState: PhoneInfoUiState,
    onValueChanged: (String) -> Unit,
    onDial: () -> Unit,
    modifier: Modifier = Modifier
) {

    val phoneValue = TextFieldValue(
        text = phoneInfoUiState.number,
        selection = TextRange(phoneInfoUiState.number.length)
    )

    val maxLength = 10

    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
    ) {

        TextField(
            value = phoneValue,
            onValueChange = { selectedTextFieldValue ->
                onValueChanged(
                    selectedTextFieldValue.text.substring(
                        0..min(
                            selectedTextFieldValue.text.length - 1,
                            maxLength - 1
                        )
                    )
                )
            },
            label = {
                Text(text = "Phone Number")
            },
            placeholder = {
                Text(text = "+1 (xxx) xxx-xxxx")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done,
                platformImeOptions = PlatformImeOptions("")
            ),
            visualTransformation = PhoneNumberTransformation(),
            singleLine = true,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDial,
            modifier = Modifier,
            enabled = phoneValue.text.isNotEmpty() && phoneValue.text.length == 10
        ) {
            Text(text = "Dial")
        }

        LaunchedEffect(Unit) {
            delay(20) // Adjust delay as needed
            focusRequester.requestFocus()
        }

    }

}
