package com.yeh35.channelopenapi.util

import java.awt.Color

class ColorUtil {

    companion object {

        /**
         * ex: converterRadix16(Color.RED) -> #ff0000
         */
        fun converterRadix16(color: Color) : String {
            return color.run {
                String.format("#%02x%02x%02x", red, green, blue)
            }
        }
    }
}