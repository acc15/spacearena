uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;
attribute float a_Edge;
attribute float a_Time;

varying float v_Edge;
varying float v_Time;

void main()
{
    v_Edge = a_Edge * 1.5;
    v_Time = a_Time * a_Time;
    gl_Position = u_MVPMatrix * a_Position;
}