package ru.timreset.example.test.base;

import org.jetbrains.annotations.Nullable;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Нужно для отслеживания, какой Widget сейчас отображается.
 */
public class AcceptsOneWidget implements com.google.gwt.user.client.ui.AcceptsOneWidget {

  private IsWidget currentWidget;

  @Override
  public void setWidget(IsWidget w) {
    this.currentWidget = w;
  }

  /**
   * Текущий Widget.
   *
   * @return Текущий Widget.
   */
  @Nullable
  public IsWidget getCurrentWidget() {
    return currentWidget;
  }
}
