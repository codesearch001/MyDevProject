package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.TaskCategories

open class TaskCategoryViewModelRealm(private val realmRepository: RealmRepository) {

    fun getTaskCategoryById(categoryId: String): TaskCategories? {
        return realmRepository.getById(TaskCategories::class.java, categoryId)
    }

    fun addOrUpdateTaskCategory(category: TaskCategories) {
        realmRepository.insertOrUpdate(category)
    }

    fun addOrUpdateTaskCategories(categories: List<TaskCategories>) {
        realmRepository.insertOrUpdateAll(categories)
    }

    fun getAllTaskCategories(): List<TaskCategories> {
        return realmRepository.getAll(TaskCategories::class.java)
    }

    fun deleteTaskCategoryById(categoryId: String) {
        realmRepository.deleteById(TaskCategories::class.java, categoryId)
    }

    fun deleteAllTaskCategories() {
        realmRepository.deleteAll(TaskCategories::class.java)
    }

    fun countAllTaskCategories(): Long {
        return realmRepository.count(TaskCategories::class.java)
    }

    fun getTaskCategoriesByField(fieldName: String, value: String): List<TaskCategories> {
        return realmRepository.query(TaskCategories::class.java) {
            equalTo(fieldName, value)
        }
    }
}
