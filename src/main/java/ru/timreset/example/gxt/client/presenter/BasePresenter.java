package ru.timreset.example.gxt.client.presenter;

import org.jetbrains.annotations.NotNull;

import com.google.gwt.user.client.ui.IsWidget;

import ru.timreset.example.gxt.client.activity.BaseActivity;

/**
 * Общий интерфейс для presenter'ов. Сделан по <a href="http://www.gwtproject.org/articles/mvp-architecture.html">офф
 * дока по MVP от Google</a> и <a href="http://www.gwtproject.org/doc/latest/DevGuideMvpActivitiesAndPlaces.html">офф
 * дока по Activity и Place</a>. А так же с учётом статьи Фаулера про MVP. Но отличается от канонической реализации тем,
 * что View имеет ссылку на интерфейс Presenter, что бы сообщать ему об изменениях (например, в случае нажатия на кнопку
 * в View). Это удобно тем, что не нужно делать передачу callback'ов в View, что бы она могла сообщить Presenter'у о
 * своих изменениях.
 * <p/>
 * <p/>
 * Use case использования Presenter'ов:
 * <p/>
 * Presenter'ы являются максимально независимой единицей - всё взаимодействие с другими Presenter'ами должно происходить
 * через EventBus, который устанавливается Presenter'у явно (не через inject). Нужно это, т.к. EventBus может быть
 * локальным и работать только с этим Presenter'ов. Свять Presenter'ов между собой происходит в Activity. ({@link
 * BaseActivity ).
 */
public interface BasePresenter<V extends BasePresenter.View> {

  /**
   * Возвращает {@link View} для отображения. Повторный вызов этого метода должен возвратить тот же View. Инициализацией
   * View Presenter должен заниматься в другом месте, но не в этом методе. Это нужно, чтобы можно было вернуть к
   * отображению этого View, если был переход на другой View. Возвращает именно {@link View}, а не {@link IsWidget}, что
   * бы можно использовать типизированные View в других View.
   *
   * @return View.
   */
  @NotNull
  V getWidget();

  /**
   * Интерфейс View для Presenter'ов. Нужен, т.к. содержит шаблонный метод {@link #setPresenter(BasePresenter)} который
   * должна вызывать каждая реализация Presenter'а чтобы сообщить View с каким Presenter'ом он общается.
   */
  public interface View<T extends BasePresenter> extends IsWidget {

    /**
     * View имеет ссылку на Presenter, чтобы в случае событий со стороны View сообщать об этом Presenter'у. Ссылка
     * передаётся напрямую через set метод, а не с помощью Inject, т.к. 1) GIN не поддерживает циклические ссылки -
     * когда View ссылается на Presenter и Presenter ссылается на View. Поэтому ссылку нужно передавать каким-то другим
     * способом. 2) Нужно, что бы ссылка View Presenter и наоборот были один к одному, т.к. View ссылался на тот же
     * Presenter, который ссылается на этот же View, поэтому Presenter должен сам инициализировать View, который есть у
     * него.
     *
     * @param presenter Ссылка на Presenter для этого View.
     */
    void setPresenter(@NotNull T presenter);
  }
}
