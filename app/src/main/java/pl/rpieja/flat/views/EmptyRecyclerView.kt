/*
 * Copyright (C) 2018 Mateusz Nowotyński
 * Copyright (C) 2015 Glowworm Software
 * Copyright (C) 2014 Nizamutdinov Adel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.rpieja.flat.views


import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class EmptyRecyclerView : RecyclerView {

    var emptyView: View? = null
        set(value) {
            field = value
            checkIfEmpty()
        }

    private val observer: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() = checkIfEmpty()
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkIfEmpty()
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkIfEmpty()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            : super(context, attrs, defStyle)

    fun checkIfEmpty() {
        if (emptyView != null) {
            emptyView!!.visibility =
                    if (adapter!!.itemCount > 0) View.GONE else View.VISIBLE
            visibility = if (adapter!!.itemCount > 0) View.VISIBLE else View.GONE
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)
        checkIfEmpty()
    }
}