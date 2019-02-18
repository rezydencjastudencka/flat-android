package pl.rpieja.flat.dto

import pl.memleak.flat.fragment.ExpenseFragment
import java.util.*

data class Expense(
        var id: String,
        var name: String,
        var date: Date,
        var to: List<User>,
        var from: User,
        var amount: Double,
        var category: Category?
) : ChargeLike {

    constructor(obj: ExpenseFragment) : this(
            id = obj.id(),
            name = obj.name(),
            date = obj.date(),
            to = obj.toUsers()?.map { User(it.fragments().userFragment()) }.orEmpty(),
            from = User(obj.fromUser().fragments().userFragment()),
            amount = obj.amount()!!,
            category = obj.category()?.fragments()?.categoryFragment()?.let { Category(it) }
    )

    override val chargeAmount: Double
        get() = amount
    override val chargeName: String
        get() = name
    override val chargeCategory: String
        get() = category?.name ?: ""
    override val fromUsers: List<User>
        get() = listOf(from)
    override val toUsers: List<User>
        get() = to
}
