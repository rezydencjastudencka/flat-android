package pl.rpieja.flat.dto

/**
 * Created by radix on 13.01.18.
 */

class TransfersDTO {

    var incoming: List<Transfer>? = null
        internal set
    var summary: List<Summary>? = null
        internal set
    var outgoing: List<Transfer>? = null
        internal set
}

