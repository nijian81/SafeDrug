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

import cn.com.google.zxing1.BarcodeFormat;
import cn.com.google.zxing1.DecodeHintType;
import cn.com.google.zxing1.Reader;
import cn.com.google.zxing1.ReaderException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * <p>A reader that can read all available UPC/EAN formats. If a caller wants to try to
 * read all such formats, it is most efficient to use this implementation rather than invoke
 * individual readers.</p>
 *
 * @author Sean Owen
 */
public final class MultiFormatUPCEANReader extends OneDReader {

  private final cn.com.google.zxing1.oned.UPCEANReader[] readers;

  public MultiFormatUPCEANReader(Map<DecodeHintType,?> hints) {
    @SuppressWarnings("unchecked")
    Collection<BarcodeFormat> possibleFormats = hints == null ? null :
        (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
    Collection<cn.com.google.zxing1.oned.UPCEANReader> readers = new ArrayList<>();
    if (possibleFormats != null) {
      if (possibleFormats.contains(BarcodeFormat.EAN_13)) {
        readers.add(new cn.com.google.zxing1.oned.EAN13Reader());
      } else if (possibleFormats.contains(BarcodeFormat.UPC_A)) {
        readers.add(new cn.com.google.zxing1.oned.UPCAReader());
      }
      if (possibleFormats.contains(BarcodeFormat.EAN_8)) {
        readers.add(new cn.com.google.zxing1.oned.EAN8Reader());
      }
      if (possibleFormats.contains(BarcodeFormat.UPC_E)) {
        readers.add(new cn.com.google.zxing1.oned.UPCEReader());
      }
    }
    if (readers.isEmpty()) {
      readers.add(new cn.com.google.zxing1.oned.EAN13Reader());
      // UPC-A is covered by EAN-13
      readers.add(new cn.com.google.zxing1.oned.EAN8Reader());
      readers.add(new cn.com.google.zxing1.oned.UPCEReader());
    }
    this.readers = readers.toArray(new cn.com.google.zxing1.oned.UPCEANReader[readers.size()]);
  }

  @Override
  public cn.com.google.zxing1.Result decodeRow(int rowNumber,
                          cn.com.google.zxing1.common.BitArray row,
                          Map<DecodeHintType,?> hints) throws cn.com.google.zxing1.NotFoundException {
    // Compute this location once and reuse it on multiple implementations
    int[] startGuardPattern = cn.com.google.zxing1.oned.UPCEANReader.findStartGuardPattern(row);
    for (cn.com.google.zxing1.oned.UPCEANReader reader : readers) {
      cn.com.google.zxing1.Result result;
      try {
        result = reader.decodeRow(rowNumber, row, startGuardPattern, hints);
      } catch (ReaderException ignored) {
        continue;
      }
      // Special case: a 12-digit code encoded in UPC-A is identical to a "0"
      // followed by those 12 digits encoded as EAN-13. Each will recognize such a code,
      // UPC-A as a 12-digit string and EAN-13 as a 13-digit string starting with "0".
      // Individually these are correct and their readers will both read such a code
      // and correctly call it EAN-13, or UPC-A, respectively.
      //
      // In this case, if we've been looking for both types, we'd like to call it
      // a UPC-A code. But for efficiency we only run the EAN-13 decoder to also read
      // UPC-A. So we special case it here, and convert an EAN-13 result to a UPC-A
      // result if appropriate.
      //
      // But, don't return UPC-A if UPC-A was not a requested format!
      boolean ean13MayBeUPCA =
          result.getBarcodeFormat() == BarcodeFormat.EAN_13 &&
              result.getText().charAt(0) == '0';
      @SuppressWarnings("unchecked")      
      Collection<BarcodeFormat> possibleFormats =
          hints == null ? null : (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
      boolean canReturnUPCA = possibleFormats == null || possibleFormats.contains(BarcodeFormat.UPC_A);

      if (ean13MayBeUPCA && canReturnUPCA) {
        // Transfer the metdata across
        cn.com.google.zxing1.Result resultUPCA = new cn.com.google.zxing1.Result(result.getText().substring(1),
                                       result.getRawBytes(),
                                       result.getResultPoints(),
                                       BarcodeFormat.UPC_A);
        resultUPCA.putAllMetadata(result.getResultMetadata());
        return resultUPCA;
      }
      return result;
    }

    throw cn.com.google.zxing1.NotFoundException.getNotFoundInstance();
  }

  @Override
  public void reset() {
    for (Reader reader : readers) {
      reader.reset();
    }
  }

}