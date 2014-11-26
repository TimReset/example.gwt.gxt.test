package ru.timreset.example.gxt.server.util;

import org.jetbrains.annotations.NotNull;

/**
 * Immutable not null реализация Pair.
 *
 * @param <A>
 * @param <B>
 */
public class Pair<A, B> {
  @NotNull private final A a;
  @NotNull private final B b;

  public Pair(@NotNull A a, @NotNull B b) {
    this.a = a;
    this.b = b;
  }

  @NotNull
  public A getA() {
    return a;
  }

  @NotNull
  public B getB() {
    return b;
  }
}
