uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;
attribute float a_Edge;

varying float v_Edge;

void main()
{
    v_Edge = a_Edge;
    gl_Position = u_MVPMatrix * a_Position;
}