/*
 * Copyright 2013 ZXing authors
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

package cn.com.google.zxing1.pdf417.decoder;

/**
 * @author Guenther Grau
 */
final class BoundingBox {

  private cn.com.google.zxing1.common.BitMatrix image;
  private cn.com.google.zxing1.ResultPoint topLeft;
  private cn.com.google.zxing1.ResultPoint bottomLeft;
  private cn.com.google.zxing1.ResultPoint topRight;
  private cn.com.google.zxing1.ResultPoint bottomRight;
  private int minX;
  private int maxX;
  private int minY;
  private int maxY;

  BoundingBox(cn.com.google.zxing1.common.BitMatrix image,
              cn.com.google.zxing1.ResultPoint topLeft,
              cn.com.google.zxing1.ResultPoint bottomLeft,
              cn.com.google.zxing1.ResultPoint topRight,
              cn.com.google.zxing1.ResultPoint bottomRight) throws cn.com.google.zxing1.NotFoundException {
    if ((topLeft == null && topRight == null) ||
        (bottomLeft == null && bottomRight == null) ||
        (topLeft != null && bottomLeft == null) ||
        (topRight != null && bottomRight == null)) {
      throw cn.com.google.zxing1.NotFoundException.getNotFoundInstance();
    }
    init(image, topLeft, bottomLeft, topRight, bottomRight);
  }

  BoundingBox(BoundingBox boundingBox) {
    init(boundingBox.image, boundingBox.topLeft, boundingBox.bottomLeft, boundingBox.topRight, boundingBox.bottomRight);
  }

  private void init(cn.com.google.zxing1.common.BitMatrix image,
                    cn.com.google.zxing1.ResultPoint topLeft,
                    cn.com.google.zxing1.ResultPoint bottomLeft,
                    cn.com.google.zxing1.ResultPoint topRight,
                    cn.com.google.zxing1.ResultPoint bottomRight) {
    this.image = image;
    this.topLeft = topLeft;
    this.bottomLeft = bottomLeft;
    this.topRight = topRight;
    this.bottomRight = bottomRight;
    calculateMinMaxValues();
  }

  static BoundingBox merge(BoundingBox leftBox, BoundingBox rightBox) throws cn.com.google.zxing1.NotFoundException {
    if (leftBox == null) {
      return rightBox;
    }
    if (rightBox == null) {
      return leftBox;
    }
    return new BoundingBox(leftBox.image, leftBox.topLeft, leftBox.bottomLeft, rightBox.topRight, rightBox.bottomRight);
  }

  BoundingBox addMissingRows(int missingStartRows, int missingEndRows, boolean isLeft) throws cn.com.google.zxing1.NotFoundException {
    cn.com.google.zxing1.ResultPoint newTopLeft = topLeft;
    cn.com.google.zxing1.ResultPoint newBottomLeft = bottomLeft;
    cn.com.google.zxing1.ResultPoint newTopRight = topRight;
    cn.com.google.zxing1.ResultPoint newBottomRight = bottomRight;

    if (missingStartRows > 0) {
      cn.com.google.zxing1.ResultPoint top = isLeft ? topLeft : topRight;
      int newMinY = (int) top.getY() - missingStartRows;
      if (newMinY < 0) {
        newMinY = 0;
      }
      // TODO use existing points to better interpolate the new x positions
      cn.com.google.zxing1.ResultPoint newTop = new cn.com.google.zxing1.ResultPoint(top.getX(), newMinY);
      if (isLeft) {
        newTopLeft = newTop;
      } else {
        newTopRight = newTop;
      }
    }

    if (missingEndRows > 0) {
      cn.com.google.zxing1.ResultPoint bottom = isLeft ? bottomLeft : bottomRight;
      int newMaxY = (int) bottom.getY() + missingEndRows;
      if (newMaxY >= image.getHeight()) {
        newMaxY = image.getHeight() - 1;
      }
      // TODO use existing points to better interpolate the new x positions
      cn.com.google.zxing1.ResultPoint newBottom = new cn.com.google.zxing1.ResultPoint(bottom.getX(), newMaxY);
      if (isLeft) {
        newBottomLeft = newBottom;
      } else {
        newBottomRight = newBottom;
      }
    }

    calculateMinMaxValues();
    return new BoundingBox(image, newTopLeft, newBottomLeft, newTopRight, newBottomRight);
  }

  private void calculateMinMaxValues() {
    if (topLeft == null) {
      topLeft = new cn.com.google.zxing1.ResultPoint(0, topRight.getY());
      bottomLeft = new cn.com.google.zxing1.ResultPoint(0, bottomRight.getY());
    } else if (topRight == null) {
      topRight = new cn.com.google.zxing1.ResultPoint(image.getWidth() - 1, topLeft.getY());
      bottomRight = new cn.com.google.zxing1.ResultPoint(image.getWidth() - 1, bottomLeft.getY());
    }

    minX = (int) Math.min(topLeft.getX(), bottomLeft.getX());
    maxX = (int) Math.max(topRight.getX(), bottomRight.getX());
    minY = (int) Math.min(topLeft.getY(), topRight.getY());
    maxY = (int) Math.max(bottomLeft.getY(), bottomRight.getY());
  }

  /*
  void setTopRight(ResultPoint topRight) {
    this.topRight = topRight;
    calculateMinMaxValues();
  }

  void setBottomRight(ResultPoint bottomRight) {
    this.bottomRight = bottomRight;
    calculateMinMaxValues();
  }
   */

  int getMinX() {
    return minX;
  }

  int getMaxX() {
    return maxX;
  }

  int getMinY() {
    return minY;
  }

  int getMaxY() {
    return maxY;
  }

  cn.com.google.zxing1.ResultPoint getTopLeft() {
    return topLeft;
  }

  cn.com.google.zxing1.ResultPoint getTopRight() {
    return topRight;
  }

  cn.com.google.zxing1.ResultPoint getBottomLeft() {
    return bottomLeft;
  }

  cn.com.google.zxing1.ResultPoint getBottomRight() {
    return bottomRight;
  }

}
