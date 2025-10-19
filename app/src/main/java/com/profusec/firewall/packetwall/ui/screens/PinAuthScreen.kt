package com.profusec.firewall.packetwall.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.profusec.firewall.packetwall.R
import com.profusec.firewall.packetwall.ui.buttons.PinButtonView
import com.profusec.firewall.packetwall.ui.components.PinInputView
import com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme
import com.profusec.firewall.packetwall.viewmodel.PinAuthViewModel

@Composable
fun PinAuthScreen(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onAuthenticated: (() -> Unit)? = null
) {
    val mViewModel: PinAuthViewModel = viewModel()
    val pin by mViewModel.pin.collectAsStateWithLifecycle()

    BackHandler {
        onBack?.invoke()
    }

    LaunchedEffect(Unit) {
        mViewModel.onAuthenticated = onAuthenticated
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(R.drawable.ic_launcher_foreground), contentDescription = null
                )

                Text(
                    text = "Enter your pin",
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
fun PinAuthScreenPreview() {
    PacketSnifferTheme(
        darkTheme = true
    ) {
        PinAuthScreen { }
    }
}