package id.ac.arraniry.payment

import java.util.UUID

class GlobalConstants {
    companion object {
        val MAHASISWA_CAT_ID: UUID = UUID.fromString("4a5a9886-04ce-4cec-ab9a-79fbe66c8157")
        const val PAYMENT_STATUS_BELUM_BAYAR = 0
        const val PAYMENT_STATUS_SUDAH_BAYAR = 1
        const val PAYMENT_STATUS_REVERSAL = 2
    }
}