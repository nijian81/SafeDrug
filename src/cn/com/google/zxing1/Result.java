/*
 * Copyright 2007 ZXing authors
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

package cn.com.google.zxing1;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>Encapsulates the result of decoding a barcode within an image.</p>
 *
 * @author Sean Owen
 */
public final class Result {

  private final String text;
  private final byte[] rawBytes;
  private cn.com.google.zxing1.ResultPoint[] resultPoints;
  private final BarcodeFormat format;
  private Map<cn.com.google.zxing1.ResultMetadataType,Object> resultMetadata;
  private final long timestamp;

  public Result(String text,
                byte[] rawBytes,
                cn.com.google.zxing1.ResultPoint[] resultPoints,
                BarcodeFormat format) {
    this(text, rawBytes, resultPoints, format, System.currentTimeMillis());
  }

  public Result(String text,
                byte[] rawBytes,
                cn.com.google.zxing1.ResultPoint[] resultPoints,
                BarcodeFormat format,
                long timestamp) {
    this.text = text;
    this.rawBytes = rawBytes;
    this.resultPoints = resultPoints;
    this.format = format;
    this.resultMetadata = null;
    this.timestamp = timestamp;
  }

  /**
   * @return raw text encoded by the barcode
   */
  public String getText() {
    return text;
  }

  /**
   * @return raw bytes encoded by the barcode, if applicable, otherwise {@code null}
   */
  public byte[] getRawBytes() {
    return rawBytes;
  }

  /**
   * @return points related to the barcode in the image. These are typically points
   *         identifying finder patterns or the corners of the barcode. The exact meaning is
   *         specific to the type of barcode that was decoded.
   */
  public cn.com.google.zxing1.ResultPoint[] getResultPoints() {
    return resultPoints;
  }

  /**
   * @return {@link BarcodeFormat} representing the format of the barcode that was decoded
   */
  public BarcodeFormat getBarcodeFormat() {
    return format;
  }

  /**
   * @return {@link java.util.Map} mapping {@link cn.com.google.zxing1.ResultMetadataType} keys to values. May be
   *   {@code null}. This contains optional metadata about what was detected about the barcode,
   *   like orientation.
   */
  public Map<cn.com.google.zxing1.ResultMetadataType,Object> getResultMetadata() {
    return resultMetadata;
  }

  public void putMetadata(cn.com.google.zxing1.ResultMetadataType type, Object value) {
    if (resultMetadata == null) {
      resultMetadata = new EnumMap<>(cn.com.google.zxing1.ResultMetadataType.class);
    }
    resultMetadata.put(type, value);
  }

  public void putAllMetadata(Map<cn.com.google.zxing1.ResultMetadataType,Object> metadata) {
    if (metadata != null) {
      if (resultMetadata == null) {
        resultMetadata = metadata;
      } else {
        resultMetadata.putAll(metadata);
      }
    }
  }

  public void addResultPoints(cn.com.google.zxing1.ResultPoint[] newPoints) {
    cn.com.google.zxing1.ResultPoint[] oldPoints = resultPoints;
    if (oldPoints == null) {
      resultPoints = newPoints;
    } else if (newPoints != null && newPoints.length > 0) {
      cn.com.google.zxing1.ResultPoint[] allPoints = new cn.com.google.zxing1.ResultPoint[oldPoints.length + newPoints.length];
      System.arraycopy(oldPoints, 0, allPoints, 0, oldPoints.length);
      System.arraycopy(newPoints, 0, allPoints, oldPoints.length, newPoints.length);
      resultPoints = allPoints;
    }
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return text;
  }

}
