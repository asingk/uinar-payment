package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepo : CrudRepository<Role, String>