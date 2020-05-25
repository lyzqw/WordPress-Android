package org.wordpress.android.ui.prefs.homepage

import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.ui.prefs.homepage.HomepageSettingsDataLoader.LoadingResult
import org.wordpress.android.ui.prefs.homepage.HomepageSettingsDataLoader.LoadingResult.Error
import org.wordpress.android.ui.prefs.homepage.HomepageSettingsDataLoader.LoadingResult.Loading
import org.wordpress.android.ui.prefs.homepage.HomepageSettingsDataLoader.LoadingResult.Data

data class HomepageSettingsUiState(
    val isClassicBlogState: Boolean,
    val siteModel: SiteModel,
    val isDisabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: Int? = null,
    val pageOnFrontState: HomepageSettingsSelectorUiState? = null,
    val pageForPostsState: HomepageSettingsSelectorUiState? = null
) {
    fun updateWithLoadingResult(
        loadingResult: LoadingResult,
        pageForPostsId: Long?,
        pageOnFrontId: Long?
    ): HomepageSettingsUiState {
        return when (loadingResult) {
            is Loading -> {
                this.copy(isLoading = true, error = null)
            }
            is Error -> this.copy(isLoading = false, error = loadingResult.message)
            is Data -> this.copy(
                    isLoading = false,
                    error = null,
                    pageForPostsState = HomepageSettingsSelectorUiState.build(loadingResult.pages, pageForPostsId),
                    pageOnFrontState = HomepageSettingsSelectorUiState.build(loadingResult.pages, pageOnFrontId)
            )
        }
    }

    fun updateWithError(message: Int): HomepageSettingsUiState {
        return this.copy(error = message, isLoading = false)
    }

    fun updateWithPageForPosts(pageForPostsId: Int): HomepageSettingsUiState {
        return if (pageForPostsState != null) {
            copy(pageForPostsState = pageForPostsState.selectItem(pageForPostsId))
        } else {
            this
        }
    }

    fun updateWithPageOnFront(pageOnFrontId: Int): HomepageSettingsUiState {
        return if (pageOnFrontState != null) {
            copy(pageOnFrontState = pageOnFrontState.selectItem(pageOnFrontId))
        } else {
            this
        }
    }
}