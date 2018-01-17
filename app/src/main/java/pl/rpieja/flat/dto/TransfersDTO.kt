package pl.rpieja.flat.dto

/**
 * Created by radix on 13.01.18.
 */

data class TransfersDTO(
        var incoming: List<Transfer>,
        var summary: List<Summary>,
        var outgoing: List<Transfer>
)

