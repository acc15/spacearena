uniform sampler2D u_Texture;
varying vec2 v_TexCoords[9];

void main()
{
    vec4 v_Sum = vec4(0.0,0.0,0.0,0.0);
    v_Sum += texture2D(u_Texture, v_TexCoords[0]) * 0.5;
    v_Sum += texture2D(u_Texture, v_TexCoords[1]) * 0.25;
    v_Sum += texture2D(u_Texture, v_TexCoords[2]) * 0.25;
    v_Sum += texture2D(u_Texture, v_TexCoords[3]) * 0.25;
    v_Sum += texture2D(u_Texture, v_TexCoords[4]) * 0.25;
    v_Sum += texture2D(u_Texture, v_TexCoords[5]) * 0.2;
    v_Sum += texture2D(u_Texture, v_TexCoords[6]) * 0.2;
    v_Sum += texture2D(u_Texture, v_TexCoords[7]) * 0.2;
    v_Sum += texture2D(u_Texture, v_TexCoords[8]) * 0.2;

    gl_FragColor = v_Sum;
}