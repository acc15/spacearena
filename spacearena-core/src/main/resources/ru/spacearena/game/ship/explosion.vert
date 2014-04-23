uniform vec2 u_Position;
uniform float u_Time;
uniform float u_PointSize;

attribute vec2 a_Destination;
attribute float a_TimeToLive;

varying vec4 v_Color;

void main() {
    float v = min(u_Time / a_TimeToLive, 1.0);
    v_Color = mix(vec4(1.0, 0.5, 0.0, 1.0), vec4(1.0, 0.0, 0.0, 0.0), v);
    gl_Position = vec4(mix(u_Position, a_Destination, v), 0.0, 1.0);
    gl_PointSize = u_PointSize;
}