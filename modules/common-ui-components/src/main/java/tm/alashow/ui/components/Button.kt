/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.alashow.ui.theme.*

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(percent = 50),
    backgroundColor: Color = MaterialTheme.colors.secondary,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        modifier = modifier,
        content = content
    )
}

@Composable
fun TextRoundedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    AppButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text)
    }
}

@Preview("buttonList")
@Composable
fun ButtonListPreview() {
    AppTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoundButtonPreview()
            RectangleButtonPreview()
            ThemeShapeButtonPreview()
            Spacer(Modifier.height(8.dp))
            ColoredRoundButtonPreview(Blue)
            ColoredRoundButtonPreview(Orange)
            ColoredRoundButtonPreview(Green)
        }
    }
}

@Composable
fun RoundButtonPreview() {
    TextRoundedButton(onClick = {}, text = "Action")
}

@Composable
fun RectangleButtonPreview() {
    AppButton(onClick = {}, shape = RectangleShape) {
        Text("Action")
    }
}

@Composable
fun ThemeShapeButtonPreview() {
    AppButton(onClick = {}, shape = MaterialTheme.shapes.small) {
        Text("Action", fontSize = 8.sp)
    }
}

@Composable
fun ColoredRoundButtonPreview(color: Color) {
    AppButton(onClick = {}, backgroundColor = color) {
        Text("Action")
    }
}
