package pl.rpieja.flat.dto

data class TransfersDTO(
        var incoming: List<Transfer>,
        var summary: List<Summary>,
        var outgoing: List<Transfer>
)

