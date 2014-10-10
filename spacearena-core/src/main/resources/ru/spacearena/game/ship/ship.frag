varying vec2 v_TexCoord;
uniform float u_ShadeAmount;
uniform sampler2D u_Texture;

const vec3 c_ShadeColor = vec3(1,1,1);
void main()
{
    gl_FragColor = texture2D(u_Texture, v_TexCoord);
    gl_FragColor.rgb = mix(gl_FragColor.rgb, c_ShadeColor, u_ShadeAmount);
}