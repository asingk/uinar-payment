package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Role
import id.ac.arraniry.payment.repo.RoleRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepo: RoleRepo
) {
    fun findAll(): List<Role> = roleRepo.findAll().toList()
    fun findById(id: String) = roleRepo.findByIdOrNull(id)
}