uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;
attribute vec2 a_TexCoord;
varying vec2 v_TexCoords[9];
uniform vec2 u_Offset;

void main()
{
    v_TexCoords[0] = a_TexCoord;
    v_TexCoords[1] = a_TexCoord + u_Offset * 1.407333;
    v_TexCoords[2] = a_TexCoord - u_Offset * 1.407333;
    v_TexCoords[3] = a_TexCoord + u_Offset * 3.294215;
    v_TexCoords[4] = a_TexCoord - u_Offset * 3.294215;

    v_TexCoords[5] = a_TexCoord + u_Offset * 1.407333;
    v_TexCoords[6] = a_TexCoord - u_Offset * 1.407333;
    v_TexCoords[7] = a_TexCoord + u_Offset * 3.294215;
    v_TexCoords[8] = a_TexCoord - u_Offset * 3.294215;
    gl_Position = u_MVPMatrix * a_Position;
}