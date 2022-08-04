package com.example.android.minutelauncher

import android.content.Intent

sealed class UiEvent {
  data class ShowToast(
    val text: String
  ) : UiEvent()

  data class StartActivity(
    val intent: Intent
  ) : UiEvent()

  object HideAppsList : UiEvent()
  data class ShowAppInfo(
    val app: UserApp
  ) : UiEvent()

  object DismissDialog : UiEvent()
}
