package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import org.jetbrains.annotations.NotNull

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(private var reminders: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {

    private var shouldReturnError = false

    fun setReturnError(shouldReturnError: Boolean) {
        this.shouldReturnError = shouldReturnError
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        // Save the reminder
        reminders.add(reminder)
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        // that confirm the correct behavior when the reminders list for some reason can't be loaded
        if (shouldReturnError) {
            return Result.Error("Error occurred")
        }else if (reminders.isEmpty()){
            return Result.Error("Reminder not found")
        }
        reminders.let {
            return Result.Success(it)
        }
    }
    // that confirm the correct behavior when the reminders list for some reason can't be loaded
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error("Error occurred")
        }
        reminders.firstOrNull {
            it.id == id
        }?.let {
            return Result.Success(it)
        }
        return Result.Error("Reminder not found")
    }

    override suspend fun delete(id: String) {
        val remind = reminders.find {
            it.id == id
        }
        reminders.remove(remind)
    }

    override suspend fun deleteAllReminders() {
        // Delete all the reminders
        reminders.clear()
    }
}