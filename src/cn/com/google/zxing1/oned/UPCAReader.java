/*
 * Copyright 2008 ZXing authors
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

import cn.com.google.zxing1.DecodeHintType;

import java.util.Map;

/**
 * <p>Implements decoding of the UPC-A format.</p>
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class UPCAReader extends UPCEANReader {

  private final UPCEANReader ean13Reader = new EAN13Reader();

  @Override
  public cn.com.google.zxing1.Result decodeRow(int rowNumber,
                          cn.com.google.zxing1.common.BitArray row,
                          int[] startGuardRange,
                          Map<DecodeHintType,?> hints)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.FormatException, cn.com.google.zxing1.ChecksumException {
    return maybeReturnResult(ean13Reader.decodeRow(rowNumber, row, startGuardRange, hints));
  }

  @Override
  public cn.com.google.zxing1.Result decodeRow(int rowNumber, cn.com.google.zxing1.common.BitArray row, Map<DecodeHintType,?> hints)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.FormatException, cn.com.google.zxing1.ChecksumException {
    return maybeReturnResult(ean13Reader.decodeRow(rowNumber, row, hints));
  }

  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image) throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.FormatException {
    return maybeReturnResult(ean13Reader.decode(image));
  }

  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.FormatException {
    return maybeReturnResult(ean13Reader.decode(image, hints));
  }

  @Override
  cn.com.google.zxing1.BarcodeFormat getBarcodeFormat() {
    return cn.com.google.zxing1.BarcodeFormat.UPC_A;
  }

  @Override
  protected int decodeMiddle(cn.com.google.zxing1.common.BitArray row, int[] startRange, StringBuilder resultString)
      throws cn.com.google.zxing1.NotFoundException {
    return ean13Reader.decodeMiddle(row, startRange, resultString);
  }

  private static cn.com.google.zxing1.Result maybeReturnResult(cn.com.google.zxing1.Result result) throws cn.com.google.zxing1.FormatException {
    String text = result.getText();
    if (text.charAt(0) == '0') {
      return new cn.com.google.zxing1.Result(text.substring(1), null, result.getResultPoints(), cn.com.google.zxing1.BarcodeFormat.UPC_A);
    } else {
      throw cn.com.google.zxing1.FormatException.getFormatInstance();
    }
  }

}
