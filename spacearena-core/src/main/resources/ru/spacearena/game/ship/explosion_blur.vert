uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;
attribute vec2 a_TexCoord;
varying vec2 v_TexCoords[9];
uniform vec2 u_Offset;

void main()
{
    v_TexCoords[0] = a_TexCoord;
    v_TexCoords[1] = vec2(a_TexCoord.x + u_Offset.x, a_TexCoord.y);
    v_TexCoords[2] = vec2(a_TexCoord.x - u_Offset.x, a_TexCoord.y);
    v_TexCoords[3] = vec2(a_TexCoord.x, a_TexCoord.y + u_Offset.y);
    v_TexCoords[4] = vec2(a_TexCoord.x, a_TexCoord.y - u_Offset.y);
    v_TexCoords[5] = vec2(a_TexCoord.x + u_Offset.x, a_TexCoord.y + u_Offset.y);
    v_TexCoords[6] = vec2(a_TexCoord.x - u_Offset.x, a_TexCoord.y + u_Offset.y);
    v_TexCoords[7] = vec2(a_TexCoord.x + u_Offset.x, a_TexCoord.y - u_Offset.y);
    v_TexCoords[8] = vec2(a_TexCoord.x - u_Offset.x, a_TexCoord.y - u_Offset.y);

    gl_Position = u_MVPMatrix * a_Position;
}