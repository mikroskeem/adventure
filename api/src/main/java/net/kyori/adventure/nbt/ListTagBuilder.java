/*
 * This file is part of adventure, licensed under the MIT License.
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
package net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;

final class ListTagBuilder<T extends BinaryTag> implements ListBinaryTag.Builder<T> {
  private final List<BinaryTag> tags = new ArrayList<>();
  private BinaryTagType<? extends BinaryTag> type;

  ListTagBuilder() {
    this(BinaryTagTypes.END);
  }

  ListTagBuilder(final BinaryTagType<? extends BinaryTag> type) {
    this.type = type;
  }

  @Override
  public ListBinaryTag.@NonNull Builder<T> add(final BinaryTag tag) {
    // don't allow an end tag to be added
    if(tag.type() == BinaryTagTypes.END) {
      throw new IllegalArgumentException(String.format("Cannot add a '%s' to a '%s'", EndBinaryTag.class.getSimpleName(), ListBinaryTag.class.getSimpleName()));
    }
    // set the type if it has not yet been set
    if(this.type == BinaryTagTypes.END) {
      this.type = tag.type();
    }
    this.tags.add(tag);
    return this;
  }

  @Override
  public @NonNull ListBinaryTag build() {
    return new ListBinaryTagImpl(this.type, new ArrayList<>(this.tags));
  }
}
