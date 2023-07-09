package com.tabarkevych.places_app.presentation

import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
//@Preview(name = "PHONE", device = Devices.PIXEL_4, showBackground = true)
@Preview(
    name = "TERMINAL",
    device = "spec:shape=Normal,width=675,height=1200,unit=dp,dpi=224",
    showBackground = true,
)
annotation class DevicePreviews
