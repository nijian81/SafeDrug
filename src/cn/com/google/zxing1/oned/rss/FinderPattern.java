/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.google.zxing1.oned.rss;

public final class FinderPattern {

  private final int value;
  private final int[] startEnd;
  private final cn.com.google.zxing1.ResultPoint[] resultPoints;

  public FinderPattern(int value, int[] startEnd, int start, int end, int rowNumber) {
    this.value = value;
    this.startEnd = startEnd;
    this.resultPoints = new cn.com.google.zxing1.ResultPoint[] {
        new cn.com.google.zxing1.ResultPoint((float) start, (float) rowNumber),
        new cn.com.google.zxing1.ResultPoint((float) end, (float) rowNumber),
    };
  }

  public int getValue() {
    return value;
  }

  public int[] getStartEnd() {
    return startEnd;
  }

  public cn.com.google.zxing1.ResultPoint[] getResultPoints() {
    return resultPoints;
  }

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof FinderPattern)) {
      return false;
    }
    FinderPattern that = (FinderPattern) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return value;
  }

}
