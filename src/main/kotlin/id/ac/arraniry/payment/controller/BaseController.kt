package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.GlobalConstants
import id.ac.arraniry.payment.entity.Customer
import java.util.*

open class BaseController {
    fun generateRandomInvoice(customer: Customer): String {
        val leftLimit = 48 // numeral '0'
        val rightLimit = 57 // numeral '9'
        val random = Random()
        if (customer.category.id == GlobalConstants.MAHASISWA_CAT_ID) {
            val rand = random.ints(leftLimit, rightLimit + 1)
                .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
                .limit(3)
                .collect(
                    { StringBuilder() },
                    { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }
                ) { obj: StringBuilder, s: StringBuilder? ->
                    obj.append(
                        s
                    )
                }
                .toString()
            return customer.id + rand
        } else {
            return random.ints(leftLimit, rightLimit + 1)
                .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
                .limit(12)
                .collect(
                    { StringBuilder() },
                    { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }
                ) { obj: StringBuilder, s: StringBuilder? ->
                    obj.append(
                        s
                    )
                }
                .toString()
        }
    }
}