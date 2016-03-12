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

package cn.com.google.zxing1.aztec;

import java.util.List;
import java.util.Map;

/**
 * This implementation can detect and decode Aztec codes in an image.
 *
 * @author David Olivier
 */
public final class AztecReader implements cn.com.google.zxing1.Reader {

  /**
   * Locates and decodes a Data Matrix code in an image.
   *
   * @return a String representing the content encoded by the Data Matrix code
   * @throws cn.com.google.zxing1.NotFoundException if a Data Matrix code cannot be found
   * @throws cn.com.google.zxing1.FormatException if a Data Matrix code cannot be decoded
   */
  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image) throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.FormatException {
    return decode(image, null);
  }

  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image, Map<cn.com.google.zxing1.DecodeHintType,?> hints)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.FormatException {

    cn.com.google.zxing1.NotFoundException notFoundException = null;
    cn.com.google.zxing1.FormatException formatException = null;
    cn.com.google.zxing1.aztec.detector.Detector detector = new cn.com.google.zxing1.aztec.detector.Detector(image.getBlackMatrix());
    cn.com.google.zxing1.ResultPoint[] points = null;
    cn.com.google.zxing1.common.DecoderResult decoderResult = null;
    try {
      AztecDetectorResult detectorResult = detector.detect(false);
      points = detectorResult.getPoints();
      decoderResult = new cn.com.google.zxing1.aztec.decoder.Decoder().decode(detectorResult);
    } catch (cn.com.google.zxing1.NotFoundException e) {
      notFoundException = e;
    } catch (cn.com.google.zxing1.FormatException e) {
      formatException = e;
    }
    if (decoderResult == null) {
      try {
        AztecDetectorResult detectorResult = detector.detect(true);
        points = detectorResult.getPoints();
        decoderResult = new cn.com.google.zxing1.aztec.decoder.Decoder().decode(detectorResult);
      } catch (cn.com.google.zxing1.NotFoundException | cn.com.google.zxing1.FormatException e) {
        if (notFoundException != null) {
          throw notFoundException;
        }
        if (formatException != null) {
          throw formatException;
        }
        throw e;
      }
    }

    if (hints != null) {
      cn.com.google.zxing1.ResultPointCallback rpcb = (cn.com.google.zxing1.ResultPointCallback) hints.get(cn.com.google.zxing1.DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      if (rpcb != null) {
        for (cn.com.google.zxing1.ResultPoint point : points) {
          rpcb.foundPossibleResultPoint(point);
        }
      }
    }

    cn.com.google.zxing1.Result result = new cn.com.google.zxing1.Result(decoderResult.getText(), decoderResult.getRawBytes(), points, cn.com.google.zxing1.BarcodeFormat.AZTEC);
    
    List<byte[]> byteSegments = decoderResult.getByteSegments();
    if (byteSegments != null) {
      result.putMetadata(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS, byteSegments);
    }
    String ecLevel = decoderResult.getECLevel();
    if (ecLevel != null) {
      result.putMetadata(cn.com.google.zxing1.ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
    }
    
    return result;
  }

  @Override
  public void reset() {
    // do nothing
  }

}
