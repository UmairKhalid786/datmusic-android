/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.audios

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import tm.alashow.datmusic.ui.media.R

private val defaultMenuActionLabels = listOf(
    R.string.audio_menu_play,
    R.string.audio_menu_playNext,
    R.string.audio_menu_download,
    R.string.audio_menu_copyLink
)

val currentPlayingMenuActionLabels = listOf(
    R.string.audio_menu_download,
    R.string.audio_menu_copyLink
)

@Composable
fun AudioDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    actionLabels: List<Int> = defaultMenuActionLabels,
    onDropdownSelect: (Int) -> Unit = {}
) {
    IconButton(
        onClick = { onExpandedChange(true) },
        modifier = modifier
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Default.MoreVert),
            contentDescription = stringResource(R.string.audio_menu_cd),
        )
    }

    Box {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .align(Alignment.Center)
        ) {
            actionLabels.forEach { item ->
                val label = stringResource(item)
                DropdownMenuItem(
                    onClick = {
                        onExpandedChange(false)
                        onDropdownSelect(item)
                    }
                ) {
                    Text(text = label)
                }
            }
        }
    }
}
