package pl.rpieja.flat.dto

interface CreateDTO<T> {
    val entityClass: Class<T>
}
