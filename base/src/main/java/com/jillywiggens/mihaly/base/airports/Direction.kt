package com.jillywiggens.mihaly.base.airports

import com.jillywiggens.mihaly.base.R

enum class Direction(val resId: Int) {
    N(R.drawable.direction_north),
    NE(R.drawable.direction_north_east),
    E(R.drawable.direction_east),
    SE(R.drawable.direction_south_east),
    S(R.drawable.direction_south),
    SW(R.drawable.direction_south_west),
    W(R.drawable.direction_west),
    NW(R.drawable.direction_north_west);

    companion object {
        @JvmStatic
        fun fromDegrees(degrees: Int) = when (degrees % 360) {
            in 0..22, in 339..359 -> E
            in 23..67 -> NE
            in 68..112 -> N
            in 113..157 -> NW
            in 158..202 -> W
            in 203..247 -> SW
            in 248..293 -> S
            in 294..338 -> SE
            else -> throw ArithmeticException()
        }
    }
}