uniform mat4 u_MVPMatrix;
uniform float u_Depth;
attribute vec4 a_Position;
void main()
{
    gl_Position = u_MVPMatrix * a_Position;
    gl_Position.z = u_Depth;
}