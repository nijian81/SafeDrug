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

package cn.com.google.zxing1.multi.qrcode;

import cn.com.google.zxing1.ReaderException;
import cn.com.google.zxing1.ResultPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

/**
 * This implementation can detect and decode multiple QR Codes in an image.
 *
 * @author Sean Owen
 * @author Hannes Erven
 */
public final class QRCodeMultiReader extends cn.com.google.zxing1.qrcode.QRCodeReader implements cn.com.google.zxing1.multi.MultipleBarcodeReader {

  private static final cn.com.google.zxing1.Result[] EMPTY_RESULT_ARRAY = new cn.com.google.zxing1.Result[0];
  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

  @Override
  public cn.com.google.zxing1.Result[] decodeMultiple(cn.com.google.zxing1.BinaryBitmap image) throws cn.com.google.zxing1.NotFoundException {
    return decodeMultiple(image, null);
  }

  @Override
  public cn.com.google.zxing1.Result[] decodeMultiple(cn.com.google.zxing1.BinaryBitmap image, Map<cn.com.google.zxing1.DecodeHintType,?> hints) throws cn.com.google.zxing1.NotFoundException {
    List<cn.com.google.zxing1.Result> results = new ArrayList<>();
    cn.com.google.zxing1.common.DetectorResult[] detectorResults = new cn.com.google.zxing1.multi.qrcode.detector.MultiDetector(image.getBlackMatrix()).detectMulti(hints);
    for (cn.com.google.zxing1.common.DetectorResult detectorResult : detectorResults) {
      try {
        cn.com.google.zxing1.common.DecoderResult decoderResult = getDecoder().decode(detectorResult.getBits(), hints);
        ResultPoint[] points = detectorResult.getPoints();
        // If the code was mirrored: swap the bottom-left and the top-right points.
        if (decoderResult.getOther() instanceof cn.com.google.zxing1.qrcode.decoder.QRCodeDecoderMetaData) {
          ((cn.com.google.zxing1.qrcode.decoder.QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(points);
        }
        cn.com.google.zxing1.Result result = new cn.com.google.zxing1.Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
                                   cn.com.google.zxing1.BarcodeFormat.QR_CODE);
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
          result.putMetadata(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String ecLevel = decoderResult.getECLevel();
        if (ecLevel != null) {
          result.putMetadata(cn.com.google.zxing1.ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }
        if (decoderResult.hasStructuredAppend()) {
          result.putMetadata(cn.com.google.zxing1.ResultMetadataType.STRUCTURED_APPEND_SEQUENCE,
                             decoderResult.getStructuredAppendSequenceNumber());
          result.putMetadata(cn.com.google.zxing1.ResultMetadataType.STRUCTURED_APPEND_PARITY,
                             decoderResult.getStructuredAppendParity());
        }
        results.add(result);
      } catch (ReaderException re) {
        // ignore and continue 
      }
    }
    if (results.isEmpty()) {
      return EMPTY_RESULT_ARRAY;
    } else {
      results = processStructuredAppend(results);
      return results.toArray(new cn.com.google.zxing1.Result[results.size()]);
    }
  }

  private static List<cn.com.google.zxing1.Result> processStructuredAppend(List<cn.com.google.zxing1.Result> results) {
    boolean hasSA = false;

    // first, check, if there is at least on SA result in the list
    for (cn.com.google.zxing1.Result result : results) {
      if (result.getResultMetadata().containsKey(cn.com.google.zxing1.ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
        hasSA = true;
        break;
      }
    }
    if (!hasSA) {
      return results;
    }

    // it is, second, split the lists and built a new result list
    List<cn.com.google.zxing1.Result> newResults = new ArrayList<>();
    List<cn.com.google.zxing1.Result> saResults = new ArrayList<>();
    for (cn.com.google.zxing1.Result result : results) {
      newResults.add(result);
      if (result.getResultMetadata().containsKey(cn.com.google.zxing1.ResultMetadataType.STRUCTURED_APPEND_SEQUENCE)) {
        saResults.add(result);
      }
    }
    // sort and concatenate the SA list items
    Collections.sort(saResults, new SAComparator());
    StringBuilder concatedText = new StringBuilder();
    int rawBytesLen = 0;
    int byteSegmentLength = 0;
    for (cn.com.google.zxing1.Result saResult : saResults) {
      concatedText.append(saResult.getText());
      rawBytesLen += saResult.getRawBytes().length;
      if (saResult.getResultMetadata().containsKey(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS)) {
        @SuppressWarnings("unchecked")
        Iterable<byte[]> byteSegments =
            (Iterable<byte[]>) saResult.getResultMetadata().get(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS);
        for (byte[] segment : byteSegments) {
          byteSegmentLength += segment.length;
        }
      }
    }
    byte[] newRawBytes = new byte[rawBytesLen];
    byte[] newByteSegment = new byte[byteSegmentLength];
    int newRawBytesIndex = 0;
    int byteSegmentIndex = 0;
    for (cn.com.google.zxing1.Result saResult : saResults) {
      System.arraycopy(saResult.getRawBytes(), 0, newRawBytes, newRawBytesIndex, saResult.getRawBytes().length);
      newRawBytesIndex += saResult.getRawBytes().length;
      if (saResult.getResultMetadata().containsKey(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS)) {
        @SuppressWarnings("unchecked")
        Iterable<byte[]> byteSegments =
            (Iterable<byte[]>) saResult.getResultMetadata().get(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS);
        for (byte[] segment : byteSegments) {
          System.arraycopy(segment, 0, newByteSegment, byteSegmentIndex, segment.length);
          byteSegmentIndex += segment.length;
        }
      }
    }
    cn.com.google.zxing1.Result newResult = new cn.com.google.zxing1.Result(concatedText.toString(), newRawBytes, NO_POINTS, cn.com.google.zxing1.BarcodeFormat.QR_CODE);
    if (byteSegmentLength > 0) {
      Collection<byte[]> byteSegmentList = new ArrayList<>();
      byteSegmentList.add(newByteSegment);
      newResult.putMetadata(cn.com.google.zxing1.ResultMetadataType.BYTE_SEGMENTS, byteSegmentList);
    }
    newResults.add(newResult);
    return newResults;
  }

  private static final class SAComparator implements Comparator<cn.com.google.zxing1.Result>, Serializable {
    @Override
    public int compare(cn.com.google.zxing1.Result a, cn.com.google.zxing1.Result b) {
      int aNumber = (int) (a.getResultMetadata().get(cn.com.google.zxing1.ResultMetadataType.STRUCTURED_APPEND_SEQUENCE));
      int bNumber = (int) (b.getResultMetadata().get(cn.com.google.zxing1.ResultMetadataType.STRUCTURED_APPEND_SEQUENCE));
      if (aNumber < bNumber) {
        return -1;
      }
      if (aNumber > bNumber) {
        return 1;
      }
      return 0;
    }
  }

}
