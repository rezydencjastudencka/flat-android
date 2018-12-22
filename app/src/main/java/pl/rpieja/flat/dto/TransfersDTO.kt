package pl.rpieja.flat.dto

import pl.memleak.flat.TransfersQuery

data class TransfersDTO(
        var incoming: List<Transfer>,
        var outgoing: List<Transfer>,
        var summary: List<Summary>
) {
    constructor(obj: TransfersQuery.Data) : this(
            incoming = obj.transfers()
                    ?.asSequence()
                    ?.filter { it.toUser().fragments().userFragment().id() == obj.me()!!.id() }
                    ?.map { Transfer(it) }
                    ?.toList().orEmpty(),
            outgoing = obj.transfers()
                    ?.asSequence()
                    ?.filter { it.fromUser().fragments().userFragment().id() == obj.me()!!.id() }
                    ?.map { Transfer(it) }
                    ?.toList().orEmpty(),
            summary = obj.summary()?.monthly()?.map { Summary(it) }.orEmpty()
    )
}

