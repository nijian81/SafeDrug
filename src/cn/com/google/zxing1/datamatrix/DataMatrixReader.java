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

package cn.com.google.zxing1.datamatrix;

import java.util.List;
import java.util.Map;

/**
 * This implementation can detect and decode Data Matrix codes in an image.
 *
 * @author bbrown@google.com (Brian Brown)
 */
public final class DataMatrixReader implements cn.com.google.zxing1.Reader {

  private static final cn.com.google.zxing1.ResultPoint[] NO_POINTS = new cn.com.google.zxing1.ResultPoint[0];

  private final cn.com.google.zxing1.datamatrix.decoder.Decoder decoder = new cn.com.google.zxing1.datamatrix.decoder.Decoder();

  /**
   * Locates and decodes a Data Matrix code in an image.
   *
   * @return a String representing the content encoded by the Data Matrix code
   * @throws cn.com.google.zxing1.NotFoundException if a Data Matrix code cannot be found
   * @throws cn.com.google.zxing1.FormatException if a Data Matrix code cannot be decoded
   * @throws cn.com.google.zxing1.ChecksumException if error correction fails
   */
  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image) throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.ChecksumException, cn.com.google.zxing1.FormatException {
    return decode(image, null);
  }

  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image, Map<cn.com.google.zxing1.DecodeHintType,?> hints)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.ChecksumException, cn.com.google.zxing1.FormatException {
    cn.com.google.zxing1.common.DecoderResult decoderResult;
    cn.com.google.zxing1.ResultPoint[] points;
    if (hints != null && hints.containsKey(cn.com.google.zxing1.DecodeHintType.PURE_BARCODE)) {
      cn.com.google.zxing1.common.BitMatrix bits = extractPureBits(image.getBlackMatrix());
      decoderResult = decoder.decode(bits);
      points = NO_POINTS;
    } else {
      cn.com.google.zxing1.common.DetectorResult detectorResult = new cn.com.google.zxing1.datamatrix.detector.Detector(image.getBlackMatrix()).detect();
      decoderResult = decoder.decode(detectorResult.getBits());
      points = detectorResult.getPoints();
    }
    cn.com.google.zxing1.Result result = new cn.com.google.zxing1.Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
        cn.com.google.zxing1.BarcodeFormat.DATA_MATRIX);
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

  /**
   * This method detects a code in a "pure" image -- that is, pure monochrome image
   * which contains only an unrotated, unskewed, image of a code, with some white border
   * around it. This is a specialized method that works exceptionally fast in this special
   * case.
   *
   * @see cn.com.google.zxing1.qrcode.QRCodeReader#extractPureBits(cn.com.google.zxing1.common.BitMatrix)
   */
  private static cn.com.google.zxing1.common.BitMatrix extractPureBits(cn.com.google.zxing1.common.BitMatrix image) throws cn.com.google.zxing1.NotFoundException {

    int[] leftTopBlack = image.getTopLeftOnBit();
    int[] rightBottomBlack = image.getBottomRightOnBit();
    if (leftTopBlack == null || rightBottomBlack == null) {
      throw cn.com.google.zxing1.NotFoundException.getNotFoundInstance();
    }

    int moduleSize = moduleSize(leftTopBlack, image);

    int top = leftTopBlack[1];
    int bottom = rightBottomBlack[1];
    int left = leftTopBlack[0];
    int right = rightBottomBlack[0];

    int matrixWidth = (right - left + 1) / moduleSize;
    int matrixHeight = (bottom - top + 1) / moduleSize;
    if (matrixWidth <= 0 || matrixHeight <= 0) {
      throw cn.com.google.zxing1.NotFoundException.getNotFoundInstance();
    }

    // Push in the "border" by half the module width so that we start
    // sampling in the middle of the module. Just in case the image is a
    // little off, this will help recover.
    int nudge = moduleSize / 2;
    top += nudge;
    left += nudge;

    // Now just read off the bits
    cn.com.google.zxing1.common.BitMatrix bits = new cn.com.google.zxing1.common.BitMatrix(matrixWidth, matrixHeight);
    for (int y = 0; y < matrixHeight; y++) {
      int iOffset = top + y * moduleSize;
      for (int x = 0; x < matrixWidth; x++) {
        if (image.get(left + x * moduleSize, iOffset)) {
          bits.set(x, y);
        }
      }
    }
    return bits;
  }

  private static int moduleSize(int[] leftTopBlack, cn.com.google.zxing1.common.BitMatrix image) throws cn.com.google.zxing1.NotFoundException {
    int width = image.getWidth();
    int x = leftTopBlack[0];
    int y = leftTopBlack[1];
    while (x < width && image.get(x, y)) {
      x++;
    }
    if (x == width) {
      throw cn.com.google.zxing1.NotFoundException.getNotFoundInstance();
    }

    int moduleSize = x - leftTopBlack[0];
    if (moduleSize == 0) {
      throw cn.com.google.zxing1.NotFoundException.getNotFoundInstance();
    }
    return moduleSize;
  }

}