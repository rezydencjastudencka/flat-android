package pl.rpieja.flat.views


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View


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
                    if (adapter.itemCount > 0) View.GONE else View.VISIBLE
            visibility = if (adapter.itemCount > 0) View.VISIBLE else View.GONE
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