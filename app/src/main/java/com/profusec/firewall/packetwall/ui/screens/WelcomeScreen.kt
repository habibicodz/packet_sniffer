package com.profusec.firewall.packetwall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen() {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                modifier = Modifier.weight(1f),
                userScrollEnabled = false,
                pageSize = PageSize.Fill,
                state = pagerState,
            ) { index ->

                when (index) {
                    0 -> {
                        WelcomePart(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Page 1")
                        }
                    }

                    1 -> {
                        WelcomePart(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Page 2")
                        }
                    }

                    2 -> {
                        WelcomePart(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Page 3")
                        }
                    }
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically), onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(0)
                        }
                    }) { Text("Previous") }

                Button(
                    modifier = Modifier.align(Alignment.CenterVertically), onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(2)
                        }
                    }) { Text("Next") }
            }
        }
    }
}

@Composable
private fun WelcomePart(
    modifier: Modifier = Modifier, content: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally), text = "Welcome Screen"
        )

        content?.invoke()
    }
}

@Composable
@Preview
fun WelcomeScreenPreview() {
    _root_ide_package_.com.profusec.firewall.packetwall.ui.theme.PacketSnifferTheme {
        WelcomeScreen()
    }
}