/*
 * Copyright 2010 ZXing authors
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

package cn.com.google.zxing1.oned;

import cn.com.google.zxing1.Writer;
import cn.com.google.zxing1.common.BitMatrix;

import java.util.Map;

/**
 * This object renders a UPC-A code as a {@link BitMatrix}.
 *
 * @author qwandor@google.com (Andrew Walbran)
 */
public final class UPCAWriter implements Writer {

  private final EAN13Writer subWriter = new EAN13Writer();

  @Override
  public BitMatrix encode(String contents, cn.com.google.zxing1.BarcodeFormat format, int width, int height)
      throws cn.com.google.zxing1.WriterException {
    return encode(contents, format, width, height, null);
  }

  @Override
  public BitMatrix encode(String contents,
                          cn.com.google.zxing1.BarcodeFormat format,
                          int width,
                          int height,
                          Map<cn.com.google.zxing1.EncodeHintType,?> hints) throws cn.com.google.zxing1.WriterException {
    if (format != cn.com.google.zxing1.BarcodeFormat.UPC_A) {
      throw new IllegalArgumentException("Can only encode UPC-A, but got " + format);
    }
    return subWriter.encode(preencode(contents), cn.com.google.zxing1.BarcodeFormat.EAN_13, width, height, hints);
  }

  /**
   * Transform a UPC-A code into the equivalent EAN-13 code, and add a check digit if it is not
   * already present.
   */
  private static String preencode(String contents) {
    int length = contents.length();
    if (length == 11) {
      // No check digit present, calculate it and add it
      int sum = 0;
      for (int i = 0; i < 11; ++i) {
        sum += (contents.charAt(i) - '0') * (i % 2 == 0 ? 3 : 1);
      }
      contents += (1000 - sum) % 10;
    } else if (length != 12) {
      throw new IllegalArgumentException(
          "Requested contents should be 11 or 12 digits long, but got " + contents.length());
    }
    return '0' + contents;
  }
}
