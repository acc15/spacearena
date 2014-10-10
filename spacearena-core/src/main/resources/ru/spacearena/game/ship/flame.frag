uniform vec4 u_CenterColor;
uniform vec4 u_EdgeColor;
varying float v_Edge;
varying float v_Time;
void main()
{
    gl_FragColor = mix(u_EdgeColor, u_CenterColor, (1.5 - abs(v_Edge))*v_Time);
}