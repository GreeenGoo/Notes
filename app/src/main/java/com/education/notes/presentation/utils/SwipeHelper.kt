package com.education.notes.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.education.notes.R
import java.util.*
import kotlin.math.abs
import kotlin.math.max

@SuppressLint("ClickableViewAccessibility")
abstract class SwipeHelper(
    private val recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT
) {
    private var swipedPosition = -1
    private val buttonsBuffer: MutableMap<Int, List<UnderlayButton>> = mutableMapOf()
    private val recoverQueue = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            if (contains(element)) return false
            return super.add(element)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, event ->
        if (swipedPosition < 0) return@OnTouchListener false
        buttonsBuffer[swipedPosition]?.forEach { it.handle(event) }
        recoverQueue.add(swipedPosition)
        swipedPosition = -1
        recoverSwipedItem()
        true
    }

    init {
        recyclerView.setOnTouchListener(touchListener)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val position = viewHolder.absoluteAdapterPosition
        var maxDX = dX
        val itemView = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                if (!buttonsBuffer.containsKey(position)) {
                    buttonsBuffer[position] = instantiateUnderlayButton(position)
                }

                val buttons = buttonsBuffer[position] ?: return
                if (buttons.isEmpty()) return
                maxDX = max(-buttons.intrinsicWidth(), dX)
                drawButtons(c, buttons, itemView, maxDX)
            }
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            maxDX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        if (swipedPosition != position) recoverQueue.add(swipedPosition)
        swipedPosition = position
        recoverSwipedItem()
    }

    private fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val position = recoverQueue.poll() ?: return
            recyclerView.adapter?.notifyItemChanged(position)
        }
    }

    private fun drawButtons(
        canvas: Canvas,
        buttons: List<UnderlayButton>,
        itemView: View,
        dX: Float
    ) {
        var right = itemView.right
        buttons.forEach { button ->
            val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(dX)
            val left = right - width + DESTINATION_BETWEEN_BUTTONS
            button.draw(
                canvas,
                RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat())
            )

            right = left.toInt()
        }
    }

    abstract fun instantiateUnderlayButton(position: Int): List<UnderlayButton>

    interface UnderlayButtonClickListener {
        fun onClick()
    }

    class UnderlayButton(
        private val context: Context,
        private val clickListener: UnderlayButtonClickListener
    ) {
        @ColorRes private val colorRes = android.R.color.holo_red_light
        private var clickableRegion: RectF? = null
        val intrinsicWidth: Float

        init {
            val titleBounds = Rect()
            intrinsicWidth = titleBounds.width() + WIDTH_FACTOR * HORIZONTAL_PADDING
        }

        fun draw(canvas: Canvas, rect: RectF) {
            val paint = Paint()
            // Draw background
            paint.color = ContextCompat.getColor(context, colorRes)
            canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint)

            try {
                VectorDrawableCompat.create(
                    context.resources,
                    R.drawable.delete_icon,
                    null
                )?.let { drawable ->
                    canvas.save()
                    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                    canvas.translate(
                        rect.centerX() - drawable.intrinsicWidth / 2F,
                        rect.centerY() - drawable.intrinsicHeight / 2F
                    )
                    drawable.draw(canvas)
                    canvas.restore()

                }
            } catch (ex: Exception) {
                drawText(canvas, rect, paint)
            }
            clickableRegion = rect
        }

        private fun drawText(c: Canvas, button: RectF, p: Paint) {
            val textSize = TEXT_SIZE
            p.color = Color.WHITE
            p.isAntiAlias = true
            p.textSize = textSize
            val textWidth = p.measureText(TEXT_OF_BUTTON)
            c.drawText(
                TEXT_OF_BUTTON,
                button.centerX() - textWidth / 2,
                button.centerY() + textSize / 2,
                p
            )
        }

        fun handle(event: MotionEvent) {
            clickableRegion?.let {
                if (it.contains(event.x, event.y)) {
                    clickListener.onClick()
                }
            }
        }
    }

    companion object {
        private const val TEXT_OF_BUTTON = "DELETE"
        private const val DESTINATION_BETWEEN_BUTTONS = 20
        private const val CORNER_RADIUS = 25f
        private const val TEXT_SIZE = 50f
        private const val HORIZONTAL_PADDING = 50f
        private const val WIDTH_FACTOR = 4
    }
}

private fun List<SwipeHelper.UnderlayButton>.intrinsicWidth(): Float {
    if (isEmpty()) return 0.0f
    return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
}
