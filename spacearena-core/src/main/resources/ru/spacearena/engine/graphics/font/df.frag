precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_Texture;
uniform vec4 u_Color;
const float smoothing = 1.0/32.0;
void main()
{
    float t_Distance = texture2D(u_Texture, v_TexCoord).x;
    float t_Alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, t_Distance);
    gl_FragColor = u_Color;
    gl_FragColor.w *= t_Alpha;
}