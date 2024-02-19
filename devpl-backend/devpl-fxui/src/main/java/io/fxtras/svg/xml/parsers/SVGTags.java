/*
Copyright (c) 2021, 2022 Hervé Girod
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
package io.fxtras.svg.xml.parsers;

/**
 * The list of svg tags handled by the library.
 *
 * @version 1.0
 */
public interface SVGTags {
    String VIEWBOX = "viewBox";
    String FILL = "fill";
    String CONTEXT_FILL = "context-fill";
    String STROKE = "stroke";
    String CONTEXT_STROKE = "context-stroke";
    String RECT = "rect";
    String CIRCLE = "circle";
    String ELLIPSE = "ellipse";
    String PATH = "path";
    String POLYGON = "polygon";
    String LINE = "line";
    String POLYLINE = "polyline";
    String TEXT = "text";
    String IMAGE = "image";
    String D = "d";
    String SVG = "svg";
    String USE = "use";
    String MARKER = "marker";
    String G = "g";
    String SYMBOL = "symbol";
    String DEFS = "defs";
    String NONE = "none";
    String CLIP_PATH_SPEC = "clipPath";
    String CLIP_PATH = "clip-path";
    String LINEAR_GRADIENT = "linearGradient";
    String RADIAL_GRADIENT = "radialGradient";
    String GRADIENT_UNITS = "gradientUnits";
    String SPREAD_METHOD = "spreadMethod";
    String SPREAD_PAD = "pad";
    String SPREAD_REFLECT = "reflect";
    String SPREAD_REPEAT = "repeat";
    String STOP = "stop";
    String STOP_COLOR = "stop-color";
    String STOP_OPACITY = "stop-opacity";
    String GRADIENT_TRANSFORM = "gradientTransform";
    String MARKER_START = "marker-start";
    String MARKER_MID = "marker-mid";
    String MARKER_END = "marker-end";
    String MARKER_WIDTH = "markerWidth";
    String MARKER_HEIGHT = "markerHeight";
    String REFX = "refX";
    String REFY = "refY";
    String TSPAN = "tspan";
    String ID = "id";
    String FILL_RULE = "fill-rule";
    String CLIP_RULE = "clip-rule";
    String CLIP_PATH_UNITS = "clipPathUnits";
    String USERSPACE_ON_USE = "userSpaceOnUse";
    String OBJECT_BOUNDINGBOX = "objectBoundingBox";
    String NON_ZERO = "nonzero";
    String EVEN_ODD = "evenodd";
    String XLINK_HREF = "xlink:href";
    String HREF = "href";
    String ANIMATE = "animate";
    String ANIMATE_MOTION = "animateMotion";
    String ANIMATE_TRANSFORM = "animateTransform";
    String SET = "set";
    String ATTRIBUTE_NAME = "attributeName";
    String REPEAT_COUNT = "repeatCount";
    String FROM = "from";
    String TO = "to";
    String VALUES = "values";
    String BEGIN = "begin";
    String DUR = "dur";
    String TYPE = "type";
    String ADDITIVE = "additive";
    String TRANSLATE = "translate";
    String SCALE = "scale";
    String ROTATE = "rotate";
    String SKEW_X = "skewX";
    String SKEW_Y = "skewY";
    String INDEFINITE = "indefinite";
    String FILTER = "filter";
    String FE_GAUSSIAN_BLUR = "feGaussianBlur";
    String FE_DROP_SHADOW = "feDropShadow";
    String FE_FLOOD = "feFlood";
    String FE_IMAGE = "feImage";
    String FE_OFFSET = "feOffset";
    String FE_MORPHOLOGY = "feMorphology";
    String FE_COMPOSITE = "feComposite";
    String FE_MERGE = "feMerge";
    String FE_MERGE_NODE = "feMergeNode";
    String FE_DISTANT_LIGHT = "feDistantLight";
    String FE_POINT_LIGHT = "fePointLight";
    String FE_SPOT_LIGHT = "feSpotLight";
    String FE_SPECULAR_LIGHTING = "feSpecularLighting";
    String FE_DIFFUSE_LIGHTING = "feDiffuseLighting";
    String FLOOD_COLOR = "flood-color";
    String FLOOD_OPACITY = "flood-opacity";
    String STD_DEVIATION = "stdDeviation";
    String PRESERVE_ASPECT_RATIO = "preserveAspectRatio";
    String IN = "in";
    String IN2 = "in2";
    String OPERATOR = "operator";
    String OPERATOR_OVER = "over";
    String OPERATOR_IN = "in";
    String OPERATOR_OUT = "out";
    String OPERATOR_ATOP = "atop";
    String OPERATOR_XOR = "xor";
    String OPERATOR_ARITHMETIC = "arithmetic";
    String SURFACE_SCALE = "surfaceScale";
    String DIFFUSE_CONSTANT = "diffuseConstant";
    String SPECULAR_CONSTANT = "specularConstant";
    String SPECULAR_EXPONENT = "specularExponent";
    String LIGHTING_COLOR = "lighting-color";
    String AZIMUTH = "azimuth";
    String ELEVATION = "elevation";
    String SOURCE_GRAPHIC = "SourceGraphic";
    String SOURCE_ALPHA = "SourceAlpha";
    String RESULT = "result";
    String FX = "fx";
    String FY = "fy";
    String CX = "cx";
    String CY = "cy";
    String DX = "dx";
    String DY = "dy";
    String RX = "rx";
    String RY = "ry";
    String R = "r";
    String X = "x";
    String Y = "y";
    String Z = "z";
    String X1 = "x1";
    String Y1 = "y1";
    String X2 = "x2";
    String Y2 = "y2";
    String POINTS = "points";
    String RADIUS = "radius";
    String POINT_AT_X = "pointsAtX";
    String POINT_AT_Y = "pointsAtY";
    String POINT_AT_Z = "pointsAtZ";
    String DILATE = "dilate";
    String OFFSET = "offset";
    String STYLE = "style";
    String SQUARE = "square";
    String ROUND = "round";
    String BUTT = "butt";
    String BEVEL = "bevel";
    String MITER = "miter";
    String FONT_FAMILY = "font-family";
    String FONT_STYLE = "font-style";
    String FONT_SIZE = "font-size";
    String FONT_WEIGHT = "font-weight";
    String TEXT_DECORATION = "text-decoration";
    String TEXT_ANCHOR = "text-anchor";
    String START = "start";
    String MIDDLE = "middle";
    String END = "end";
    String NORMAL = "normal";
    String BOLD = "bold";
    String BOLDER = "bolder";
    String LIGHTER = "lighter";
    String ITALIC = "italic";
    String OBLIQUE = "oblique";
    String LINE_THROUGH = "line-through";
    String BASELINE_SHIFT = "baseline-shift";
    String BASELINE_SUB = "sub";
    String BASELINE_SUPER = "super";
    String UNDERLINE = "underline";
    String WIDTH = "width";
    String HEIGHT = "height";
    String TRANSFORM = "transform";
    String OPACITY = "opacity";
    String VISIBILITY = "visibility";
    String VISIBLE = "visible";
    String HIDDEN = "hidden";
    String FILL_OPACITY = "fill-opacity";
    String STROKE_WIDTH = "stroke-width";
    String STROKE_LINECAP = "stroke-linecap";
    String STROKE_MITERLIMIT = "stroke-miterlimit";
    String STROKE_LINEJOIN = "stroke-linejoin";
    String STROKE_DASHARRAY = "stroke-dasharray";
    String STROKE_DASHOFFSET = "stroke-dashoffset";
    String CLASS = "class";
}
