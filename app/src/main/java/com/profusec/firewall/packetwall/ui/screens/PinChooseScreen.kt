package com.profusec.firewall.packetwall.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.profusec.firewall.packetwall.ui.buttons.PinButtonView
import com.profusec.firewall.packetwall.ui.components.PinInputView
import com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme
import com.profusec.firewall.packetwall.viewmodel.PinChooseViewModel
import com.profusec.firewall.packetwall.R
import kotlinx.coroutines.flow.collectLatest

@Composable
@Preview
fun PinChooseScreen(
    modifier: Modifier = Modifier, onBack: (() -> Unit)? = null, onPinChoose: (() -> Unit)? = null
) {
    val context: Context = LocalContext.current
    val mViewModel: PinChooseViewModel = viewModel()
    val pin by mViewModel.pin.collectAsStateWithLifecycle()
    val selectedPin by mViewModel.selectedPin.collectAsStateWithLifecycle()
    val pinTitle = if (selectedPin == null) "Enter a 4 digit PIN" else "Retype your PIN"

    BackHandler {
        onBack?.invoke()
    }

    LaunchedEffect(Unit) {
        mViewModel.onPinChoose = onPinChoose
        mViewModel.snackMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = {
                        onBack?.invoke()
                    }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(R.drawable.ic_launcher_foreground), contentDescription = null
                )

                Text(
                    text = pinTitle,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                PinInputView(
                    modifier = Modifier.fillMaxWidth(), pin = pin.toTypedArray()
                )

                PinButtonView(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                        .widthIn(max = 100.dp), onPinButtonClicked = {
                        mViewModel.addChar(it.toString())
                    }, onClearClicked = {
                        mViewModel.removeLastChar()
                    }, onOkClicked = {
                        mViewModel.processPin()
                    })
            }
        }
    }
}

@Composable
@Preview
fun PinChooseScreenPreview() {
    PacketSnifferTheme(
        darkTheme = true
    ) {
        PinChooseScreen {

        }
    }
}