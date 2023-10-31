package com.example.android.minutelauncher.db

import com.example.android.minutelauncher.GestureDirection
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class LauncherRepository @Inject constructor(private val dao: LauncherDAO) {

  fun appList() = dao.getAllApps()
  fun gestureApps() =
    dao.getGestureApps().map { it.associate { gApp -> gApp.swipeApp.swipeDirection to gApp.app } }

  fun favoriteApps() = dao.getFavoriteApps()
  fun insertApp(app: App) = dao.insertApp(app)
  fun toggleFavorite(app: App) = dao.toggleFavoriteApp(app)
  fun insertGestureApp(swipeApp: SwipeApp) {
    dao.removeAppForGesture(swipeApp.swipeDirection.toString())
    dao.insertGestureApp(swipeApp)
  }
  fun removeAppForGesture(gesture: GestureDirection) = dao.removeAppForGesture(gesture.toString())
  fun getAppForGesture(gesture: GestureDirection) = dao.getAppForGesture(gesture.toString())
  fun updateFavoritesOrder(new: List<FavoriteAppWithApp>) {
    new.forEachIndexed { index, app ->
      Timber.d("Updating order for: ${app.app.appTitle} from ${dao.getOrderForFavoriteById(app.app.id)} to $index")
      dao.insertFavoriteApp(FavoriteApp(app.favoriteApp.appId, index))
    }
  }
}
