package com.profusec.firewall.packetwall.ui.dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.profusec.firewall.packetwall.util.AppThemeMode
import com.profusec.firewall.packetwall.util.ThemeUtil

@Composable
@Preview
fun ThemeSelectorDialog(
    modifier: Modifier = Modifier,
    onSelect: ((AppThemeMode) -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    val context: Context = LocalContext.current
    val defaultTheme = ThemeUtil.getAppTheme(context.applicationContext)
    var selectedTheme by remember { mutableStateOf(defaultTheme) }

    Dialog(
        onDismissRequest = { onDismissRequest?.invoke() },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        20.dp
                    )
                )
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Theme",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 22.sp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {
                                selectedTheme = AppThemeMode.DARK
                            }
                        )
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RadioButton(
                        modifier = Modifier.padding(0.dp),
                        selected = selectedTheme == AppThemeMode.DARK, onClick = null
                    )
                    Text(
                        "Dark",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {
                                selectedTheme = AppThemeMode.LIGHT
                            }
                        )
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RadioButton(
                        modifier = Modifier.padding(0.dp),
                        selected = selectedTheme == AppThemeMode.LIGHT, onClick = null
                    )
                    Text(
                        "Light",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {
                                selectedTheme = AppThemeMode.SYSTEM
                            }
                        )
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RadioButton(
                        modifier = Modifier.padding(0.dp),
                        selected = selectedTheme == AppThemeMode.SYSTEM, onClick = null
                    )
                    Text (
                        "System",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        onSelect?.invoke(selectedTheme)
                    }
                ) {
                    Text(
                        text = "SET"
                    )
                }
            }
        }
    }
}