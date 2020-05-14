package com.senierr.adapter.internal

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat

class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewSparseArray: SparseArray<View> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findView(@IdRes viewId: Int): T? {
        var view: View? = viewSparseArray.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewSparseArray.put(viewId, view)
        }
        return view as T?
    }

    companion object {
        fun create(parent: ViewGroup, @LayoutRes layoutId: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return create(itemView)
        }

        fun create(layoutInflater: LayoutInflater, parent: ViewGroup, @LayoutRes layoutId: Int): ViewHolder {
            val itemView = layoutInflater.inflate(layoutId, parent, false)
            return create(itemView)
        }

        fun create(itemView: View): ViewHolder {
            return ViewHolder(itemView)
        }
    }

    open fun setText(@IdRes viewId: Int, value: CharSequence?): ViewHolder {
        findView<TextView>(viewId)?.text = value
        return this
    }

    open fun setText(@IdRes viewId: Int, @StringRes strId: Int): ViewHolder? {
        findView<TextView>(viewId)?.setText(strId)
        return this
    }

    open fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): ViewHolder {
        findView<TextView>(viewId)?.setTextColor(color)
        return this
    }

    open fun setTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int): ViewHolder {
        findView<TextView>(viewId)?.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
        return this
    }

    open fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): ViewHolder {
        findView<ImageView>(viewId)?.setImageResource(imageResId)
        return this
    }

    open fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): ViewHolder {
        findView<ImageView>(viewId)?.setImageDrawable(drawable)
        return this
    }

    open fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): ViewHolder {
        findView<ImageView>(viewId)?.setImageBitmap(bitmap)
        return this
    }

    open fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): ViewHolder {
        findView<View>(viewId)?.setBackgroundColor(color)
        return this
    }

    open fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): ViewHolder {
        findView<View>(viewId)?.setBackgroundResource(backgroundRes)
        return this
    }

    open fun setVisible(@IdRes viewId: Int, isVisible: Boolean): ViewHolder {
        val view = findView<View>(viewId)
        view?.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        return this
    }

    open fun setGone(@IdRes viewId: Int, isGone: Boolean): ViewHolder {
        val view = findView<View>(viewId)
        view?.visibility = if (isGone) View.GONE else View.VISIBLE
        return this
    }

    open fun setEnabled(@IdRes viewId: Int, isEnabled: Boolean): ViewHolder {
        findView<View>(viewId)?.isEnabled = isEnabled
        return this
    }
}
