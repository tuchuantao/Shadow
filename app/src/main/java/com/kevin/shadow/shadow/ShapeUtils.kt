package com.kuaiest.ui.shadow

import android.graphics.Path
import android.graphics.RectF


object ShapeUtils {

    fun roundedRect(
            left: Float, top: Float, right: Float, bottom: Float,
            tl: Float, tr: Float, br: Float, bl: Float): Path {
        var tl = tl
        var tr = tr
        var br = br
        var bl = bl
        val path = Path()
        if (tl < 0) tl = 0f
        if (tr < 0) tr = 0f
        if (br < 0) br = 0f
        if (bl < 0) bl = 0f
        val width = right - left
        val height = bottom - top
        val min = Math.min(width, height)
        if (tl > min / 2) tl = min / 2
        if (tr > min / 2) tr = min / 2
        if (br > min / 2) br = min / 2
        if (bl > min / 2) bl = min / 2
        if (tl == tr && tr == br && br == bl && tl == min / 2) {
            if (width == height) {
                val radius = min / 2F
                path.addCircle(left + radius, top + radius, radius, Path.Direction.CW)
            } else {
                path.addRoundRect(RectF(left, top, width + left, height), floatArrayOf(tl, tl, tl, tl, tl, tl, tl, tl), Path.Direction.CW)
            }
            return path
        }

        path.moveTo(right, top + tr)
        if (tr > 0)
            path.rQuadTo(0f, -tr, -tr, -tr)//top-right corner
        else {
            path.rLineTo(0f, -tr)
            path.rLineTo(-tr, 0f)
        }
        path.rLineTo(-(width - tr - tl), 0f)
        if (tl > 0)
            path.rQuadTo(-tl, 0f, -tl, tl) //top-left corner
        else {
            path.rLineTo(-tl, 0f)
            path.rLineTo(0f, tl)
        }
        path.rLineTo(0f, height - tl - bl)

        if (bl > 0)
            path.rQuadTo(0f, bl, bl, bl)//bottom-left corner
        else {
            path.rLineTo(0f, bl)
            path.rLineTo(bl, 0f)
        }

        path.rLineTo(width - bl - br, 0f)
        if (br > 0)
            path.rQuadTo(br, 0f, br, -br) //bottom-right corner
        else {
            path.rLineTo(br, 0f)
            path.rLineTo(0f, -br)
        }

        path.rLineTo(0f, -(height - br - tr))

        path.close()//Given close, last line to can be removed.

        return path
    }

}