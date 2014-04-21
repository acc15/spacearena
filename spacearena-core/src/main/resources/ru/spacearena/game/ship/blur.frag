precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_Texture;
void main()
{
    vec4 v_Sum = vec4(0,0,0,0);
    for (float y=-4; y<=4; y++) {
        for (float x=-4; x<=4; x++) {
            v_Sum += texture2D(u_Texture, vec2(v_TexCoord.x + x/256, v_TexCoord.y + y/256));
        }
    }
    v_Sum /= 81.0;

    gl_FragColor = v_Sum;
}