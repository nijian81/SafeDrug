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

package cn.com.google.zxing1.multi;

import java.util.Map;

/**
 * This class attempts to decode a barcode from an image, not by scanning the whole image,
 * but by scanning subsets of the image. This is important when there may be multiple barcodes in
 * an image, and detecting a barcode may find parts of multiple barcode and fail to decode
 * (e.g. QR Codes). Instead this scans the four quadrants of the image -- and also the center
 * 'quadrant' to cover the case where a barcode is found in the center.
 *
 * @see GenericMultipleBarcodeReader
 */
public final class ByQuadrantReader implements cn.com.google.zxing1.Reader {

  private final cn.com.google.zxing1.Reader delegate;

  public ByQuadrantReader(cn.com.google.zxing1.Reader delegate) {
    this.delegate = delegate;
  }

  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.ChecksumException, cn.com.google.zxing1.FormatException {
    return decode(image, null);
  }

  @Override
  public cn.com.google.zxing1.Result decode(cn.com.google.zxing1.BinaryBitmap image, Map<cn.com.google.zxing1.DecodeHintType,?> hints)
      throws cn.com.google.zxing1.NotFoundException, cn.com.google.zxing1.ChecksumException, cn.com.google.zxing1.FormatException {

    int width = image.getWidth();
    int height = image.getHeight();
    int halfWidth = width / 2;
    int halfHeight = height / 2;

    try {
      // No need to call makeAbsolute as results will be relative to original top left here
      return delegate.decode(image.crop(0, 0, halfWidth, halfHeight), hints);
    } catch (cn.com.google.zxing1.NotFoundException re) {
      // continue
    }

    try {
      cn.com.google.zxing1.Result result = delegate.decode(image.crop(halfWidth, 0, halfWidth, halfHeight), hints);
      makeAbsolute(result.getResultPoints(), halfWidth, 0);
      return result;
    } catch (cn.com.google.zxing1.NotFoundException re) {
      // continue
    }

    try {
      cn.com.google.zxing1.Result result = delegate.decode(image.crop(0, halfHeight, halfWidth, halfHeight), hints);
      makeAbsolute(result.getResultPoints(), 0, halfHeight);
      return result;
    } catch (cn.com.google.zxing1.NotFoundException re) {
      // continue
    }

    try {
      cn.com.google.zxing1.Result result = delegate.decode(image.crop(halfWidth, halfHeight, halfWidth, halfHeight), hints);
      makeAbsolute(result.getResultPoints(), halfWidth, halfHeight);
      return result;
    } catch (cn.com.google.zxing1.NotFoundException re) {
      // continue
    }

    int quarterWidth = halfWidth / 2;
    int quarterHeight = halfHeight / 2;
    cn.com.google.zxing1.BinaryBitmap center = image.crop(quarterWidth, quarterHeight, halfWidth, halfHeight);
    cn.com.google.zxing1.Result result = delegate.decode(center, hints);
    makeAbsolute(result.getResultPoints(), quarterWidth, quarterHeight);
    return result;
  }

  @Override
  public void reset() {
    delegate.reset();
  }

  private static void makeAbsolute(cn.com.google.zxing1.ResultPoint[] points, int leftOffset, int topOffset) {
    if (points != null) {
      for (int i = 0; i < points.length; i++) {
        cn.com.google.zxing1.ResultPoint relative = points[i];
        points[i] = new cn.com.google.zxing1.ResultPoint(relative.getX() + leftOffset, relative.getY() + topOffset);
      }
    }
  }

}