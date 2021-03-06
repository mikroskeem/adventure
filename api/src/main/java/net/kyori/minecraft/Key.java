/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.minecraft;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A key.
 */
public interface Key extends Comparable<Key> {
  /**
   * The namespace for Minecraft.
   */
  String MINECRAFT_NAMESPACE = "minecraft";

  /**
   * Creates a key.
   *
   * @param string the string
   * @return the key
   * @throws ParseException if the namespace contains an invalid character
   * @throws ParseException if the value contains an invalid character
   */
  static @NonNull Key of(final @NonNull String string) {
    return of(string, ':');
  }

  /**
   * Creates a key.
   *
   * @param string the string
   * @param character the character
   * @return the key
   * @throws ParseException if the namespace contains an invalid character
   * @throws ParseException if the value contains an invalid character
   */
  static @NonNull Key of(final @NonNull String string, final char character) {
    final int index = string.indexOf(character);
    final String namespace = index >= 1 ? string.substring(0, index) : MINECRAFT_NAMESPACE;
    final String value = index >= 0 ? string.substring(index + 1) : string;
    return of(namespace, value);
  }

  /**
   * Creates a key.
   *
   * @param namespace the namespace
   * @param value the value
   * @return the key
   * @throws ParseException if the namespace contains an invalid character
   * @throws ParseException if the value contains an invalid character
   */
  static @NonNull Key of(final @NonNull String namespace, final @NonNull String value) {
    return new KeyImpl(namespace, value);
  }

  /**
   * Gets the namespace.
   *
   * @return the namespace
   */
  @NonNull String namespace();

  /**
   * Gets the value.
   *
   * @return the value
   */
  @NonNull String value();

  /**
   * Returns the string representation of this key.
   *
   * @return the string representation
   */
  @NonNull String asString();

  /**
   * An exception thrown when there is an error parsing a key.
   */
  @SuppressWarnings("serial")
  final class ParseException extends RuntimeException {
    ParseException(final String message) {
      super(message);
    }
  }
}
