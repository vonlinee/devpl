/*
Copyright (c) 2022, Hervé Girod
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Alternatively if you have any questions about this project, you can visit
the project website at the project page on https://github.com/hervegirod/fxsvgimage
 */
package io.fxtras.svg.tosvg.converters;

import io.fxtras.svg.tosvg.utils.Utilities;
import io.fxtras.svg.tosvg.xml.XMLNode;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Iterator;

/**
 * A converter which convert Shapes.
 *
 * @since 1.0
 */
public class ShapeConverter extends AbstractConverter implements DefaultStrokeValues {
    private Shape shape = null;

    public ShapeConverter(ConverterDelegate delegate, Shape shape, XMLNode xmlParent) {
        super(delegate, shape, xmlParent);
        this.shape = shape;
    }

    private boolean hasStrokeProperty() {
        return properties.containsKey(STROKE_WIDTH) || properties.containsKey(STROKE_LINECAP) || properties.containsKey(STROKE_LINEJOIN)
            || properties.containsKey(STROKE_MITERLIMIT) || properties.containsKey(STROKE_DASHARRAY) || properties.containsKey(STROKE_DASHOFFSET);
    }

    private boolean hasFontProperty() {
        return properties.containsKey(FONT_FAMILY) || properties.containsKey(FONT_SIZE) || properties.containsKey(FONT_STYLE)
            || properties.containsKey(FONT_WEIGHT);
    }

    /**
     * Return the Arc Width of a Rectangle.
     *
     * @param rec the Rectangle
     * @return the Arc Width
     */
    private double getArcWidth(Rectangle rec) {
        if (properties.containsKey(ARC_WIDTH)) {
            return (double) (Double) properties.get(ARC_WIDTH);
        } else {
            return rec.getArcWidth();
        }
    }

    /**
     * Return the Arc Height of a Rectangle.
     *
     * @param rec the Rectangle
     * @return the Arc Height
     */
    private double getArcHeight(Rectangle rec) {
        if (properties.containsKey(ARC_HEIGHT)) {
            return (double) (Double) properties.get(ARC_HEIGHT);
        } else {
            return rec.getArcWidth();
        }
    }

    private Font getFont(Text text) {
        if (properties.containsKey(FONT)) {
            Font font = (Font) properties.get(FONT);
            if (font != Font.getDefault()) {
                return font;
            }
        }
        if (hasFontProperty()) {
            Font font = text.getFont();
            double size = font.getSize();
            String family = font.getFamily();
            FontWeight weight = Utilities.getWeight(font);
            FontPosture posture = Utilities.getPosture(font);
            font = Font.font(family, weight, posture, size);
            return font;
        } else {
            return text.getFont();
        }
    }

    /**
     * Return the opacity of the Node Fill. The way it work for a Shape is slightly different from a Region, because
     * contrary to a Region, a Shape can set its own Stroke and Fill. Hence foe example if the Fill of the Shape is
     * transparent or null, the opacity will be considered 0 even if the opacity value in the CSS is not 0.
     *
     * @return the opacity
     */
    protected double getOpacityFill() {
        if (properties.containsKey(OPACITY)) {
            Number opacity = (Number) properties.get(OPACITY);
            return opacity.doubleValue();
        } else {
            double opacity = shape.getOpacity();
            Paint paint = shape.getFill();
            if (paint == null) {
                opacity = 0;
            } else if (paint instanceof Color color) {
                double _opacity = color.getOpacity();
                if (_opacity < opacity) {
                    opacity = _opacity;
                }
            }
            return opacity;
        }
    }

    /**
     * Return the opacity of the Node Stroke.
     *
     * @return the opacity
     */
    protected double getOpacityStroke() {
        if (properties.containsKey(OPACITY)) {
            Number opacity = (Number) properties.get(OPACITY);
            return opacity.doubleValue();
        } else {
            double opacity = shape.getOpacity();
            Paint paint = shape.getStroke();
            if (paint == null) {
                opacity = 0;
            } else if (paint instanceof Color color) {
                double _opacity = color.getOpacity();
                if (_opacity < opacity) {
                    opacity = _opacity;
                }
            }
            return opacity;
        }
    }

    /**
     * Return the Awt Paint corresponing to the JavaFX fill.
     *
     * @return the Awt Paint
     */
    private Paint getFillPaint() {
        double opacity = this.getOpacityFill();
        if (opacity <= 0) {
            return null;
        }
        if (properties.containsKey(FILL_PAINT)) {
            return (Paint) properties.get(FILL_PAINT);
        } else {
            // we must use the fillProperty() and to the getFill() method, because getFill() will return a Color.BLACK for a null fill
            return shape.fillProperty().get();
        }
    }

    /**
     * Return the Awt Paint corresponing to the JavaFX Stroke.
     *
     * @return the Awt Paint
     * @see CSSProperties#STROKE_PAINT
     */
    private Paint getStrokePaint() {
        double opacity = this.getOpacityStroke();
        if (opacity <= 0) {
            return null;
        }
        if (properties.containsKey(STROKE_PAINT)) {
            return (Paint) properties.get(STROKE_PAINT);
        } else {
            // we must use the strokeProperty() and to the getFill() method, because getStroke() will return a Color.BLACK for a null stroke
            return shape.strokeProperty().get();
        }
    }

    private void setOpacity(Paint paint, XMLNode node) {
        if (paint instanceof Color color) {
            if (color.getOpacity() != 1) {
                node.addAttribute("opacity", color.getOpacity());
            }
        }
    }

    @Override
    public void applyStyle(XMLNode node, String clipID) {
        StringBuilder buf = new StringBuilder();
        if (shape instanceof Line) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Rectangle) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Circle) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Ellipse) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Polyline) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Polygon) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof SVGPath) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Path) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Arc) {
            Paint paint = getStrokePaint();
            setClip(buf, clipID);
            addStroke(paint, buf);
            setLineStroke(shape, buf);
            setStrokeOpacity(paint, node);
            paint = getFillPaint();
            addFill(paint, buf);
            setFillOpacity(paint, node);
            String style = buf.toString();
            node.addAttribute("style", style);
        } else if (shape instanceof Text text) {
            Paint paint = getFillPaint();
            setClip(buf, clipID);
            addFill(paint, buf);
            setOpacity(paint, node);
            addFontStyle(text, buf);
            String style = buf.toString();
            node.addAttribute("style", style);
        }
    }

    private void addFontStyle(Text text, StringBuilder buf) {
        Font font = text.getFont();
        FontWeight weight = Utilities.getWeight(font);
        FontPosture posture = Utilities.getPosture(font);
        if (null == weight) {
            buf.append("font-weight:normal;");
        } else {
            switch (weight) {
                case BOLD:
                    buf.append("font-weight:bold;");
                    break;
                case EXTRA_BOLD:
                    buf.append("font-weight:bold;");
                    break;
                case LIGHT:
                    buf.append("font-weight:lighter;");
                    break;
                default:
                    buf.append("font-weight:normal;");
                    break;
            }
        }
        if (posture == FontPosture.ITALIC) {
            buf.append("font-style:italic;");
        } else {
            buf.append("font-style:normal;");
        }
        double size = font.getSize();
        buf.append("font-size:").append(size).append(";");
        String family = font.getFamily();
        if (family.equals("System")) {
            family = "Arial";
        }
        buf.append("font-family:").append(family).append(";");
    }

    private void appendPoints(Polygon polygon, XMLNode node) {
        StringBuilder buf = new StringBuilder();
        ObservableList<Double> points = polygon.getPoints();
        for (int i = 0; i < points.size(); i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);
            buf.append(x).append(",").append(y);
            if (i < points.size() - 1) {
                buf.append(" ");
            }
        }
        node.addAttribute("points", buf.toString());
    }

    private void appendPoints(Polyline polyline, XMLNode node) {
        StringBuilder buf = new StringBuilder();
        ObservableList<Double> points = polyline.getPoints();
        for (int i = 0; i < points.size(); i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);
            buf.append(x).append(",").append(y);
            if (i < points.size() - 1) {
                buf.append(" ");
            }
        }
        node.addAttribute("points", buf.toString());
    }

    private void setLineStroke(Shape shape, StringBuilder buf) {
        StrokeLineJoin lineJoin = shape.getStrokeLineJoin();
        if (lineJoin != DEFAULT_STROKE_LINE_JOIN) {
            if (lineJoin == StrokeLineJoin.BEVEL) {
                buf.append("stroke-linejoin: bevel;");
            } else if (lineJoin == StrokeLineJoin.MITER) {
                buf.append("stroke-linejoin: miter;");
            } else if (lineJoin == StrokeLineJoin.ROUND) {
                buf.append("stroke-linejoin: round;");
            }
        }
        StrokeLineCap lineCap = shape.getStrokeLineCap();
        if (lineCap != DEFAULT_STROKE_LINE_CAP) {
            if (lineCap == StrokeLineCap.BUTT) {
                buf.append("stroke-linecap: butt;");
            } else if (lineCap == StrokeLineCap.ROUND) {
                buf.append("stroke-linecap: round;");
            } else if (lineCap == StrokeLineCap.SQUARE) {
                buf.append("stroke-linecap: square;");
            }
        }
        double miterLimit = shape.getStrokeMiterLimit();
        if (miterLimit != DEFAULT_STROKE_MITER_LIMIT) {
            buf.append("stroke-miterlimit:").append(miterLimit).append(";");
        }
        double dashoffset = shape.getStrokeDashOffset();
        if (dashoffset != DEFAULT_STROKE_DASH_OFFSET) {
            buf.append("stroke-dashoffset:").append(dashoffset).append(";");
        }
        double width = shape.getStrokeWidth();
        if (width != DEFAULT_STROKE_WIDTH) {
            buf.append("stroke-width:").append(width).append(";");
        }
        ObservableList<Double> array = shape.getStrokeDashArray();
        if (!array.isEmpty()) {
            buf.append("stroke-dasharray:");
            Iterator<Double> it = array.iterator();
            while (it.hasNext()) {
                double value = it.next();
                buf.append(value);
                if (it.hasNext()) {
                    buf.append(",");
                }
            }
            buf.append(";");
        }
    }

    private void setPath(Path path, XMLNode node) {
        StringBuilder buf = new StringBuilder();
        ObservableList<PathElement> elements = path.getElements();
        Iterator<PathElement> it = elements.iterator();
        while (it.hasNext()) {
            PathElement element = it.next();
            if (element instanceof MoveTo moveTo) {
                buf.append("M").append(moveTo.getX()).append(",").append(moveTo.getY());
            } else if (element instanceof ClosePath) {
                buf.append("Z");
            } else if (element instanceof HLineTo lineTo) {
                buf.append("H").append(lineTo.getX());
            } else if (element instanceof VLineTo lineTo) {
                buf.append("V").append(lineTo.getY());
            } else if (element instanceof LineTo lineTo) {
                buf.append("L").append(lineTo.getX()).append(",").append(lineTo.getY());
            } else if (element instanceof ArcTo arcTo) {
                buf.append("A").append(arcTo.getRadiusX()).append(",").append(arcTo.getRadiusY());
                buf.append(" ").append(arcTo.getXAxisRotation());
                buf.append(" ").append(arcTo.isLargeArcFlag() ? "1" : "0").append(",").append(arcTo.isSweepFlag() ? "1" : "0");
                buf.append(" ").append(arcTo.getX()).append(",").append(arcTo.getY());
            } else if (element instanceof QuadCurveTo curveTo) {
                buf.append("Q").append(curveTo.getControlX()).append(",").append(curveTo.getControlY());
                buf.append(" ").append(curveTo.getX()).append(",").append(curveTo.getY());
            } else if (element instanceof CubicCurveTo curveTo) {
                buf.append("C").append(curveTo.getControlX1()).append(",").append(curveTo.getControlY1());
                buf.append(" ").append(curveTo.getControlX2()).append(",").append(curveTo.getControlY2());
                buf.append(" ").append(curveTo.getX()).append(",").append(curveTo.getY());
            }
            if (it.hasNext()) {
                buf.append(" ");
            }
        }
        node.addAttribute("d", buf.toString());
    }

    /**
     * Convert the Shape. The currently suppported Shape classes are:
     * <ul>
     * <li>Line</li>
     * <li>Circle</li>
     * <li>Ellipse</li>
     * <li>Arc</li>
     * <li>Rectangle</li>
     * <li>Path</li>
     * <li>Polygon</li>
     * <li>Polyline</li>
     * <li>Text</li>
     * <li>QuadCurve</li>
     * <li>CubicCurve</li>
     * </ul>
     *
     * @return the XML node
     */
    @Override
    public XMLNode convert() {
        XMLNode node = null;
        if (shape instanceof Line line) {
            node = new XMLNode("line");
            node.addAttribute("x1", line.getStartX());
            node.addAttribute("y1", line.getStartY());
            node.addAttribute("x2", line.getEndX());
            node.addAttribute("y2", line.getEndY());
            xmlParent.addChild(node);
        } else if (shape instanceof Text text) {
            String str = text.getText();
            node = new XMLNode("text");
            node.addAttribute("xml:space", "preserve");
            node.addAttribute("x", text.getX());
            node.addAttribute("y", text.getY());
            node.setCDATA(str);
            xmlParent.addChild(node);
        } else if (shape instanceof Polygon polygon) {
            node = new XMLNode("polygon");
            appendPoints(polygon, node);
            xmlParent.addChild(node);
        } else if (shape instanceof Polyline polyline) {
            node = new XMLNode("polyline");
            appendPoints(polyline, node);
            xmlParent.addChild(node);
        } else if (shape instanceof SVGPath path) {
            node = new XMLNode("path");
            node.addAttribute("d", path.getContent());
            xmlParent.addChild(node);
        } else if (shape instanceof Path path) {
            node = new XMLNode("path");
            setPath(path, node);
            xmlParent.addChild(node);
        } else if (shape instanceof Circle circle) {
            node = new XMLNode("circle");
            node.addAttribute("cx", circle.getCenterX());
            node.addAttribute("cy", circle.getCenterY());
            node.addAttribute("r", circle.getRadius());
            xmlParent.addChild(node);
        } else if (shape instanceof Ellipse ellipse) {
            node = new XMLNode("ellipse");
            node.addAttribute("cx", ellipse.getCenterX());
            node.addAttribute("cy", ellipse.getCenterY());
            node.addAttribute("rx", ellipse.getRadiusX());
            node.addAttribute("ry", ellipse.getRadiusY());
            xmlParent.addChild(node);
        } else if (shape instanceof Arc arc) {
            Path path = new Path();
            double startX = arc.getCenterX() - arc.getRadiusX();
            double startY = arc.getCenterY() - arc.getRadiusY();
            MoveTo moveTo = new MoveTo();
            moveTo.setX(startX);
            moveTo.setY(startY);
            path.getElements().add(moveTo);
            double endX = arc.getCenterX() + arc.getRadiusX() * Math.cos(Math.toRadians(arc.getStartAngle() + arc.getLength()));
            double endY = arc.getCenterY() + arc.getRadiusY() * Math.sin(Math.toRadians(arc.getStartAngle() + arc.getLength()));
            ArcTo arcTo = new ArcTo();
            arcTo.setX(endX);
            arcTo.setY(endY);
            path.getElements().add(arcTo);
            if (arc.getType() == ArcType.CHORD) {
                path.getElements().add(new ClosePath());
            }
            node = new XMLNode("path");
            setPath(path, node);
            xmlParent.addChild(node);
        } else if (shape instanceof Rectangle rec) {
            double arcWidth = getArcWidth(rec);
            double arcHeight = getArcHeight(rec);
            node = new XMLNode("rect");
            node.addAttribute("width", rec.getWidth());
            node.addAttribute("height", rec.getHeight());
            node.addAttribute("x", rec.getX());
            node.addAttribute("y", rec.getY());
            if (arcWidth != 0) {
                node.addAttribute("rx", arcWidth / 2);
            }
            if (arcHeight != 0) {
                node.addAttribute("ry", arcHeight / 2);
            }
            xmlParent.addChild(node);
        } else if (shape instanceof QuadCurve curve) {
            Path path = new Path();
            MoveTo moveTo = new MoveTo();
            moveTo.setX(curve.getStartX());
            moveTo.setY(curve.getStartY());
            QuadCurveTo curveTo = new QuadCurveTo();
            curveTo.setX(curve.getEndX());
            curveTo.setY(curve.getEndY());
            curveTo.setControlX(curve.getControlX());
            curveTo.setControlY(curve.getControlY());
            path.getElements().add(moveTo);
            path.getElements().add(curveTo);
            node = new XMLNode("path");
            setPath(path, node);
            xmlParent.addChild(node);
        } else if (shape instanceof CubicCurve curve) {
            Path path = new Path();
            MoveTo moveTo = new MoveTo();
            moveTo.setX(curve.getStartX());
            moveTo.setY(curve.getStartY());
            CubicCurveTo curveTo = new CubicCurveTo();
            curveTo.setX(curve.getEndX());
            curveTo.setY(curve.getEndY());
            curveTo.setControlX1(curve.getControlX1());
            curveTo.setControlY1(curve.getControlY1());
            curveTo.setControlX2(curve.getControlX2());
            curveTo.setControlY2(curve.getControlY2());
            path.getElements().add(moveTo);
            path.getElements().add(curveTo);
            node = new XMLNode("path");
            setPath(path, node);
            xmlParent.addChild(node);
        }
        return node;
    }
}
