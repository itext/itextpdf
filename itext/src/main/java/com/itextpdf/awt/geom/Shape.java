/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  This code was originally part of the Apache Harmony project.
 *  The Apache Harmony project has been discontinued.
 *  That's why we imported the code into iText.
 */
/**
 * @author Alexey A. Petrenko
 */
package com.itextpdf.awt.geom;

/**
 * Shape
 *
 */
public interface Shape {
    public boolean contains(double x, double y);

    public boolean contains(double x, double y, double w, double h);

    public boolean contains(Point2D point);

    public boolean contains(Rectangle2D r);

    public Rectangle getBounds();

    public Rectangle2D getBounds2D();

    public PathIterator getPathIterator(AffineTransform at);

    public PathIterator getPathIterator(AffineTransform at, double flatness);

    public boolean intersects(double x, double y, double w, double h);

    public boolean intersects(Rectangle2D r);
}
