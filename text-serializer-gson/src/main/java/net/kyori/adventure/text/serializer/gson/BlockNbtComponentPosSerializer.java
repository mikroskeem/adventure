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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.BlockNBTComponent;

final class BlockNbtComponentPosSerializer implements JsonDeserializer<BlockNBTComponent.Pos>, JsonSerializer<BlockNBTComponent.Pos> {
  private static final Pattern LOCAL_PATTERN = Pattern.compile("^\\^(\\d+(\\.\\d+)?) \\^(\\d+(\\.\\d+)?) \\^(\\d+(\\.\\d+)?)$");
  private static final Pattern WORLD_PATTERN = Pattern.compile("^(~?)(\\d+) (~?)(\\d+) (~?)(\\d+)$");

  private static final String LOCAL_SYMBOL = "^";
  private static final String RELATIVE_SYMBOL = "~";
  private static final String ABSOLUTE_SYMBOL = "";

  @Override
  public BlockNBTComponent.Pos deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final String string = json.getAsString();

    final Matcher localMatch = LOCAL_PATTERN.matcher(string);
    if(localMatch.matches()) {
      return BlockNBTComponent.LocalPos.of(
        Double.parseDouble(localMatch.group(1)),
        Double.parseDouble(localMatch.group(3)),
        Double.parseDouble(localMatch.group(5))
      );
    }

    final Matcher worldMatch = WORLD_PATTERN.matcher(string);
    if(worldMatch.matches()) {
      return BlockNBTComponent.WorldPos.of(
        deserializeCoordinate(worldMatch.group(1), worldMatch.group(2)),
        deserializeCoordinate(worldMatch.group(3), worldMatch.group(4)),
        deserializeCoordinate(worldMatch.group(5), worldMatch.group(6))
      );
    }

    throw new JsonParseException("Don't know how to turn " + string + " into a Position");
  }

  @Override
  public JsonElement serialize(final BlockNBTComponent.Pos src, final Type typeOfSrc, final JsonSerializationContext context) {
    if(src instanceof BlockNBTComponent.LocalPos) {
      final BlockNBTComponent.LocalPos local = (BlockNBTComponent.LocalPos) src;
      return new JsonPrimitive(serializeLocal(local.left()) + ' ' + serializeLocal(local.up()) + ' ' + serializeLocal(local.forwards()));
    } else if(src instanceof BlockNBTComponent.WorldPos) {
      final BlockNBTComponent.WorldPos world = (BlockNBTComponent.WorldPos) src;
      return new JsonPrimitive(serializeCoordinate(world.x()) + ' ' + serializeCoordinate(world.y()) + ' ' + serializeCoordinate(world.z()));
    } else {
      throw new IllegalArgumentException("Don't know how to serialize " + src + " as a Position");
    }
  }

  private static BlockNBTComponent.WorldPos.Coordinate deserializeCoordinate(final String prefix, final String value) {
    final int i = Integer.parseInt(value);
    if(prefix.equals(ABSOLUTE_SYMBOL)) {
      return BlockNBTComponent.WorldPos.Coordinate.absolute(i);
    } else if(prefix.equals(RELATIVE_SYMBOL)) {
      return BlockNBTComponent.WorldPos.Coordinate.relative(i);
    } else {
      throw new AssertionError(); // regex does not allow any other value for prefix.
    }
  }

  private static String serializeLocal(final double value) {
    return LOCAL_SYMBOL + value;
  }

  private static String serializeCoordinate(final BlockNBTComponent.WorldPos.Coordinate coordinate) {
    return (coordinate.type() == BlockNBTComponent.WorldPos.Coordinate.Type.RELATIVE ? RELATIVE_SYMBOL : ABSOLUTE_SYMBOL) + coordinate.value();
  }
}
