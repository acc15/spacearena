/**
 *
 * Shape classes naming convention: {@code {PrimitiveName}[Dimension]{Precision}[Representation]}.
 * Where: <ol>
 *     <li>{@code PrimitiveName} - canonical name of shape (Circle, Ellipse, Point, AABB, Rectangle, OBB and so on</li>
 *     <li>{@code Dimension} - dimension of shape which class described. Possible values: 2, 3 and so on</li>
 *     <li>{@code Precision} - type of floating-point primitive used. Possible values: <ul>
 *         <li>F = float</li>
 *         <li>D = double</li>
 *     </ul></li>
 *     <li>{@code Representation} - abbreviated representation of shape. Possible letters: <ul>
 *         <li>P = Position</li>
 *         <li>C = Center</li>
 *         <li>D = Size (or Diameter)</li>
 *         <li>R = Radius (or half-width, half-height)</li></ul>
 *         Examples: <ul>
 *         <li>PP = Position, Position (AABB or Ellipse with left-top and right-bottom points)</li>
 *         <li>PD = Position, Size (AABB with left-top and width, height)</li>
 *         <li>CR = Center, Radius (AABB with center x,y and halfwidths), circle and ellipse with radius</li>
 *         </ul>
 *     </ul></li>
 * </ol>
 *
 * Examples: <ul>
 *     <li>Ellipse2FPD</li>
 *     <li>Circle2FPR</li>
 *     <li>Rectangle2DPP</li>
 *     <li>Point3D</li>
 *     <li>Point2F</li>
 * </ul>
 *
 * @author Vyacheslav Mayorov
 * @since 2014-09-03
 */
package ru.spacearena.engine.geometry.primitives;

