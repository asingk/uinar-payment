package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Channel
import id.ac.arraniry.payment.repo.ChannelRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChannelService(
    private val channelRepo: ChannelRepo
) {
    fun findAll(): List<Channel> = channelRepo.findAll().toList()
    fun findById(id: UUID): Channel? = channelRepo.findByIdOrNull(id)
    fun save(channel: Channel): Channel = channelRepo.save(channel)
    fun delete(id: UUID) = channelRepo.deleteById(id)
    fun findByIdAndDisabled(id: UUID, disabled: Boolean): Channel? = channelRepo.findByIdAndDisabled(id, disabled)
}