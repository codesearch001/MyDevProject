package com.snofed.publicapp.ui.clubsubmember.ViewModelClub


import RealmRepository
import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.HelpArticle
import io.realm.Realm

open class HelpViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
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
