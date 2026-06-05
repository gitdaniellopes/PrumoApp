package br.com.prumoapp.domain.model.expense

data class InstallmentNumber(val current: Int, val total: Int) {

    init {
        require(total >= 2) {
            "O número total de parcelas deve ser maior ou igual a 2"
        }
        require(current in 1..total) {
            "O número da parcela atual deve estar entre 1 e o número total de parcelas"
        }
    }

    val isFirst: Boolean get() = current == 1
    val isLast: Boolean get() = current == total

    fun next(): InstallmentNumber {
        require(!isLast) {
            "Não há proxima parcela: já está em $current/$total"
        }
        return copy(current = current + 1)
    }
}