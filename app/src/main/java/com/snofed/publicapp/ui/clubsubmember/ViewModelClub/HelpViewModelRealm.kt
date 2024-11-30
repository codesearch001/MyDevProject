package com.snofed.publicapp.ui.clubsubmember.ViewModelClub


import RealmRepository
import com.snofed.publicapp.models.realmModels.HelpArticle

open class HelpViewModelRealm(private val realmRepository: RealmRepository) {

    fun getHelpArticleById(articleId: String): HelpArticle? {
        return realmRepository.getById(HelpArticle::class.java, articleId)
    }

    fun addOrUpdateHelpArticle(article: HelpArticle) {
        realmRepository.insertOrUpdate(article)
    }

    fun addOrUpdateHelpArticles(articles: List<HelpArticle>) {
        realmRepository.insertOrUpdateAll(articles)
    }

    fun getAllHelpArticles(): List<HelpArticle> {
        return realmRepository.getAll(HelpArticle::class.java)
    }

    fun deleteHelpArticleById(articleId: String) {
        realmRepository.deleteById(HelpArticle::class.java, articleId)
    }

    fun deleteAllHelpArticles() {
        realmRepository.deleteAll(HelpArticle::class.java)
    }

    fun countAllHelpArticles(): Long {
        return realmRepository.count(HelpArticle::class.java)
    }

    fun getHelpArticlesByField(fieldName: String, value: String): List<HelpArticle> {
        return realmRepository.query(HelpArticle::class.java) {
            equalTo(fieldName, value)
        }
    }
}
