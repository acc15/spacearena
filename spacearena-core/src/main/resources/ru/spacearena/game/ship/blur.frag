uniform sampler2D u_Texture;
varying vec2 v_TexCoords[5];

void main()
{
    vec4 v_Sum = vec4(0.0,0.0,0.0,0.0);
    v_Sum += texture2D(u_Texture, v_TexCoords[0]) * 0.204164;
    v_Sum += texture2D(u_Texture, v_TexCoords[1]) * 0.304005;
    v_Sum += texture2D(u_Texture, v_TexCoords[2]) * 0.304005;
    v_Sum += texture2D(u_Texture, v_TexCoords[3]) * 0.093913;
    v_Sum += texture2D(u_Texture, v_TexCoords[4]) * 0.093913;
    gl_FragColor = v_Sum;
}