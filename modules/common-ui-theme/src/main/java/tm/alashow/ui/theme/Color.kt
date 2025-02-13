/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.ui.theme

import android.graphics.Color as AndroidColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.material.Colors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import kotlin.random.Random

fun parseColor(hexColor: String) = Color(AndroidColor.parseColor(hexColor))
fun Int.toColor() = Color(this)

val Primary = Color(0xFF16053D)
val PrimaryVariant = Color(0xFF221652)
val Secondary = Color(0xFFFF3C8F)
val SecondaryVariant = Color(0xFFef0076)

val WhiteTransparent = Color(0x80FFFFFF)

val Red = Color(0xFFFF3B30)
val Red700 = Color(0xFFC0392b)
val Orange = Color(0xFFFF9500)
val Yellow = Color(0xFFFFCC00)
val Yellow500 = Color(0xFFFBBC04)
val Green = Color(0xFF4CD964)
val Blue300 = Color(0xFF5AC8FA)
val Blue = Color(0xFF007AFF)
val Purple = Color(0xFF5856D6)
val Asphalt = Color(0xFF2c3e50)

internal val DarkAppColors = appDarkColors(Primary, Secondary, PrimaryVariant, SecondaryVariant)
internal val LightAppColors = appLightColors(Primary, Secondary, PrimaryVariant, SecondaryVariant)

fun appDarkColors(
    primary: Color,
    secondary: Color,
    primaryVariant: Color = primary,
    secondaryVariant: Color = secondary,
    _onSurfaceInputBackground: Color = Color(0x45706d86)
) = AppColors(
    _onSurfaceInputBackground = _onSurfaceInputBackground,
    darkColors(
        primary = primary,
        onPrimary = Color.White,
        primaryVariant = primaryVariant,
        secondary = secondary,
        onSecondary = Color.White,
        secondaryVariant = secondaryVariant,
        background = primary,
        surface = primary,
        onSurface = Color.White,
    )
)

fun appLightColors(
    primary: Color,
    secondary: Color,
    primaryVariant: Color = primary,
    secondaryVariant: Color = secondary,
    _onSurfaceInputBackground: Color = Color(0x45c1bbc0)
) = AppColors(
    _onSurfaceInputBackground = _onSurfaceInputBackground,
    lightColors(
        primary = primary,
        onPrimary = Color.White,
        primaryVariant = primaryVariant,
        secondary = secondary,
        onSecondary = Color.White,
        secondaryVariant = secondaryVariant,
        background = Color.White,
        surface = Color.White,
        onSurface = Color.Black,
    )
)

@Composable
fun Colors.plainBackground() = if (MaterialTheme.colors.isLight) Color.White else Color.Black

@Composable
fun Colors.plainGrayBackground() = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray

@Composable
fun Color.disabledAlpha(condition: Boolean): Color = copy(alpha = if (condition) alpha else ContentAlpha.disabled)

@Composable
fun Color.contrastComposite(alpha: Float = 0.1f) = contentColorFor(this).copy(alpha = alpha).compositeOver(this)

@Composable
internal fun animate(colors: Colors): Colors {
    val animationSpec = remember { spring<Color>() }

    @Composable
    fun animateColor(color: Color): Color = animateColorAsState(targetValue = color, animationSpec = animationSpec).value

    return Colors(
        primary = animateColor(colors.primary),
        primaryVariant = animateColor(colors.primaryVariant),
        secondary = animateColor(colors.secondary),
        secondaryVariant = animateColor(colors.secondaryVariant),
        background = animateColor(colors.background),
        surface = animateColor(colors.surface),
        error = animateColor(colors.error),
        onPrimary = animateColor(colors.onPrimary),
        onSecondary = animateColor(colors.onSecondary),
        onBackground = animateColor(colors.onBackground),
        onSurface = animateColor(colors.onSurface),
        onError = animateColor(colors.onError),
        isLight = colors.isLight,
    )
}

@Composable
fun translucentSurfaceColor() = MaterialTheme.colors.surface.copy(alpha = AppBarAlphas.translucentBarAlpha())

fun Modifier.translucentSurface() = composed { background(translucentSurfaceColor()) }

@Composable
fun Modifier.randomBackground(memoize: Boolean = true) = background(if (memoize) remember { randomColor() } else randomColor())

fun randomColor() = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
